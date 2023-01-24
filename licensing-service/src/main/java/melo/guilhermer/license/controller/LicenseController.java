package melo.guilhermer.license.controller;

import melo.guilhermer.license.model.License;
import melo.guilhermer.license.service.LicenseService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/licenses")
public class LicenseController {

    private final LicenseService service;

    public LicenseController(final LicenseService service) {
        this.service = service;
    }

    @GetMapping(value = "/{licenseId}/organization/{organizationId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
        @PathVariable("licenseId") Integer id) throws TimeoutException, InterruptedException {
        List<License> licenses = service.getLIcenseByOrganization(organizationId);
        // License license = service.getLicense(id, organizationId);

        License license = licenses.get(0);


        Link getMethod = linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getId()))
            .withSelfRel();

        Link createMethod = linkTo(methodOn(LicenseController.class).createLicense(license, null)).withRel(
            "createLicense");

        Link updateMethod = linkTo(methodOn(LicenseController.class)
            .updateLicense(organizationId, license))
            .withRel("updateLicense");

        Link deleteMethod = linkTo(methodOn(LicenseController.class)
            .deleteLicense(organizationId, String.valueOf(license.getId())))
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
    public ResponseEntity<String> createLicense(@RequestBody License request, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(service.createLicense(request, locale));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
        @PathVariable("organizationId") String organizationId,
        @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(service.deleteLicense(licenseId, organizationId));
    }

    @RequestMapping(value="/{licenseId}/{clientType}", method = RequestMethod.GET)
    public License getLicensesWithClient(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") Integer id,
            @PathVariable("clientType") String clientType) {
        return service.getLicense(id, organizationId, clientType);
    }
}
