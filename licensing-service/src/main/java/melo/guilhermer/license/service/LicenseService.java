package melo.guilhermer.license.service;

import melo.guilhermer.license.ServiceConfig;
import melo.guilhermer.license.model.License;
import melo.guilhermer.license.repository.LicenseRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

    public License getLicense(Integer id, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndId(organizationId, id);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), id, organizationId));
        }
        return license.withComment(config.getProperty());
    }

    public String createLicense(License license, String organizationId, Locale locale) {
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
                "license.delete.message", null, null),licenseId);
        return responseMessage;
    }

    public License getLicense(Integer id, String organizationId, String clientType){
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
}
