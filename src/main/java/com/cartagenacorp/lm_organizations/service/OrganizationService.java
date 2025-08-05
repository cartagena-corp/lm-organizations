package com.cartagenacorp.lm_organizations.service;

import com.cartagenacorp.lm_organizations.dto.OrganizationRequestDto;
import com.cartagenacorp.lm_organizations.dto.OrganizationResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto);

    OrganizationResponseDto getOrganizationById(UUID id);

    List<OrganizationResponseDto> getAllOrganizations();

    OrganizationResponseDto updateOrganization(UUID id, OrganizationRequestDto organizationRequestDto);

    void deleteOrganization(UUID id);

    boolean organizationExists(UUID id);
}
