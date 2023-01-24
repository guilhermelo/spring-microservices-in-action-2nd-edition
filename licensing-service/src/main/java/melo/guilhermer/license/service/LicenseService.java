package melo.guilhermer.license.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import melo.guilhermer.license.ServiceConfig;
import melo.guilhermer.license.model.License;
import melo.guilhermer.license.repository.LicenseRepository;
import org.slf4j.Logger;
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

    public LicenseService(final MessageSource messageSource, LicenseRepository licenseRepository, ServiceConfig config) {
        this.messageSource = messageSource;
        this.licenseRepository = licenseRepository;
        this.config = config;
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

        return new License();
//        Organization organization = retrieveOrganizationInfo(organizationId,
//                clientType);
//        if (null != organization) {
//            license.setOrganizationName(organization.getName());
//            license.setContactName(organization.getContactName());
//            license.setContactEmail(organization.getContactEmail());
//            license.setContactPhone(organization.getContactPhone());
//        }
//        return license.withComment(config.getExampleProperty());
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
}
