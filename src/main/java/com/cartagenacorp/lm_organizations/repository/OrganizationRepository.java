package com.cartagenacorp.lm_organizations.repository;

import com.cartagenacorp.lm_organizations.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByOrganizationName(String organizationName);

    boolean existsByOrganizationId(UUID organizationId);
}