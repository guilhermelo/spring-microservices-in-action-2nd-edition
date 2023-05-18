package com.optimagrowth.organization.service;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.organization.events.source.ActionEnum;
import com.optimagrowth.organization.events.source.SimpleSourceBean;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
	
    @Autowired
    private OrganizationRepository repository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    @Autowired
    Tracer tracer;

    private final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public Organization findById(String organizationId) {
        Optional<Organization> opt;
         ScopedSpan newSpan = tracer.startScopedSpan("getOrgDBCall");
        try {
            opt = repository.findById(organizationId);
            simpleSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
            if (opt.isEmpty()) {
                String message = String.format("Unable to find an organization with theOrganization id %s", organizationId);
                logger.error(message);
                throw new IllegalArgumentException(message);
            }

            var organization = opt.get().getName();

            logger.debug("Retrieving Organization: {0}", organization);

        } finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }

        return opt.orElseThrow(() -> new EntityNotFoundException("Organization not found!"));
    }

    public Organization create(Organization organization){
    	organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
        return organization;

    }

    public void update(Organization organization){
    	repository.save(organization);
    }

    public void delete(Organization organization){
    	repository.deleteById(organization.getId());
    }
}