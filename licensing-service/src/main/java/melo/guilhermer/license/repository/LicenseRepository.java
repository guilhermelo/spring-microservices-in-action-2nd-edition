package melo.guilhermer.license.repository;

import melo.guilhermer.license.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);

    License findByOrganizationIdAndId(String organizationId, Integer id);

    List<License> findAll();
}
