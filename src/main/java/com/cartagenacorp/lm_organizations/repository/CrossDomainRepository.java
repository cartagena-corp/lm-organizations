package com.cartagenacorp.lm_organizations.repository;

import com.cartagenacorp.lm_organizations.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CrossDomainRepository extends JpaRepository<Organization, UUID> {

    @Modifying
    @Query(value = "UPDATE project SET organization_id = :newOrgId, status = NULL WHERE id = :projectId", nativeQuery = true)
    void updateProjectOrganizationAndResetStatus(UUID projectId, UUID newOrgId);

    @Modifying
    @Query(value = "UPDATE project_config SET organization_id = :newOrgId WHERE project_id = :projectId", nativeQuery = true)
    void updateProjectConfigOrganization(UUID projectId, UUID newOrgId);

    @Modifying
    @Query(value = "UPDATE issue SET organization_id = :newOrgId WHERE project_id = :projectId", nativeQuery = true)
    void updateIssuesOrganization(UUID projectId, UUID newOrgId);

    @Modifying
    @Query(value = "UPDATE comment SET organization_id = :newOrgId WHERE issue_id IN " +
            "(SELECT id FROM issue WHERE project_id = :projectId)", nativeQuery = true)
    void updateCommentsOrganization(UUID projectId, UUID newOrgId);
}