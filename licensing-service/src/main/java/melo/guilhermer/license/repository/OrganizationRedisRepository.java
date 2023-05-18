package melo.guilhermer.license.repository;

import melo.guilhermer.license.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {
}
