package melo.guilhermer.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import melo.guilhermer.license.model.License;
import melo.guilhermer.license.service.LicenseService;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService service;

    public LicenseController(final LicenseService service) {
        this.service = service;
    }

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
        @PathVariable("licenseId") String licenseId) {
        License license = service.getLicense(licenseId, organizationId);

        Link getMethod = linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId()))
            .withSelfRel();

        Link createMethod = linkTo(methodOn(LicenseController.class).createLicense(organizationId, license, null)).withRel(
            "createLicense");

        Link updateMethod = linkTo(methodOn(LicenseController.class)
            .updateLicense(organizationId, license))
            .withRel("updateLicense");

        Link deleteMethod = linkTo(methodOn(LicenseController.class)
            .deleteLicense(organizationId, license.getLicenseId()))
            .withRel("deleteLicense");

        license.add(getMethod, createMethod, updateMethod, deleteMethod);

        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId,
        @RequestBody License request) {
        return ResponseEntity.ok(service.updateLicense(request, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(
        @PathVariable("organizationId") String organizationId,
        @RequestBody License request, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(service.createLicense(request, organizationId, locale));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
        @PathVariable("organizationId") String organizationId,
        @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(service.deleteLicense(licenseId, organizationId));
    }
}
