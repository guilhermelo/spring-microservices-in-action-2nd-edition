package melo.guilhermer.license.service.client;

import brave.ScopedSpan;
import brave.Tracer;
import melo.guilhermer.license.model.Organization;
import melo.guilhermer.license.repository.OrganizationRedisRepository;
import melo.guilhermer.license.utils.UserContext;
//import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

//    @Autowired
//    KeycloakRestTemplate restTemplate;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrganizationRedisRepository redisRepository;

    @Autowired
    UserContext userContext;

    @Autowired
    Tracer tracer;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private Organization checkRedisCache(String organizationId) {

        // Cria um span customizado
        ScopedSpan newSpan = tracer.startScopedSpan("readLicesingDataFromRedis");

        try {
            return redisRepository.findById(organizationId).orElse(null);
        }catch (Exception ex){
            logger.error("Error encountered while trying to retrieve organization{} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        } finally {
            // nomeia o serviço que o zipkin vai capturar
            newSpan.tag("peer.service", "redis");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
    }
    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
        }catch (Exception ex){
            logger.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), ex);
        }
    }
    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization: {}", userContext.getCorrelationId());

        Organization organization = checkRedisCache(organizationId);
        if (organization != null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }
        logger.debug("Unable to locate organization from the redis cache: {}.",organizationId);
        ResponseEntity<Organization> restExchange = restTemplate.exchange("http://gateway-server:8072/organization/v1/organization/{organizationId}",HttpMethod.GET, null, Organization.class, organizationId);

        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }

        return restExchange.getBody();
    }
}
