package melo.guilhermer.license.service;

import java.util.Locale;
import java.util.UUID;

import melo.guilhermer.license.repository.LicenseRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import melo.guilhermer.license.model.License;

@Service
public class LicenseService {

    private final MessageSource messageSource;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;

    public LicenseService(final MessageSource messageSource, LicenseRepository licenseRepository) {
        this.messageSource = messageSource;
        this.licenseRepository = licenseRepository;
    }

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(
                    String.format(messageSource.getMessage(
                            "license.search.error.message", null, null),
                            licenseId, organizationId));
        }
        return license.withComment(config.getProperty());
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String updateLicense(License license, String organizationId) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage(
                "license.delete.message", null, null),licenseId);
        return responseMessage;
    }
}
