package melo.guilhermer.license.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import melo.guilhermer.license.ServiceConfig;
import melo.guilhermer.license.model.License;
import melo.guilhermer.license.model.Organization;
import melo.guilhermer.license.repository.LicenseRepository;
import melo.guilhermer.license.service.client.OrganizationDiscoveryClient;
import melo.guilhermer.license.service.client.OrganizationFeignClient;
import melo.guilhermer.license.service.client.OrganizationRestTemplateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseService {

    private final MessageSource messageSource;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;
    OrganizationFeignClient organizationFeignClient;
    OrganizationRestTemplateClient organizationRestClient;
    OrganizationDiscoveryClient organizationDiscoveryClient;

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    public LicenseService(final MessageSource messageSource, LicenseRepository licenseRepository, ServiceConfig config,
                          OrganizationFeignClient organizationFeignClient,
                          OrganizationRestTemplateClient organizationRestClient,
                          OrganizationDiscoveryClient organizationDiscoveryClient) {
        this.messageSource = messageSource;
        this.licenseRepository = licenseRepository;
        this.config = config;
        this.organizationFeignClient = organizationFeignClient;
        this.organizationRestClient = organizationRestClient;
        this.organizationDiscoveryClient = organizationDiscoveryClient;
    }

    @CircuitBreaker(name = "licenseService")
    public License getLicense(Integer id, String organizationId) throws InterruptedException, TimeoutException {

        //simulando circuit breaker
        sleep();
        License license = licenseRepository.findByOrganizationIdAndId(organizationId, id);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), id, organizationId));
        }
        return license.withComment(config.getProperty());
    }

    private void sleep() throws TimeoutException, InterruptedException {
            Thread.sleep(1000);
            throw new java.util.concurrent.TimeoutException();
    }

    public String createLicense(License license, Locale locale) {
        licenseRepository.save(license);
        return config.getProperty();
    }

    public String updateLicense(License license, String organizationId) {
        licenseRepository.save(license);
        return config.getProperty();
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage = null;
        License license = new License();
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage(
                "license.delete.message", null, null), licenseId);
        return responseMessage;
    }

    public License getLicense(Integer id, String organizationId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndId(organizationId, id);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), id, organizationId));
        }

        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {

        Organization organization = null;

        switch (clientType) {
            case "feign":
                logger.debug("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                logger.debug("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                logger.debug("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
                break;
        }

        return organization;
    }

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLIcenseByOrganization(String organizationId) throws TimeoutException, InterruptedException {
        System.out.println("getLicensesByOrganization Correlation id: {}" + new Random().nextInt());
        sleep();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t){
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setId(0);
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    public List<License> getLicenses() {
        return licenseRepository.findAll();
    }
}
