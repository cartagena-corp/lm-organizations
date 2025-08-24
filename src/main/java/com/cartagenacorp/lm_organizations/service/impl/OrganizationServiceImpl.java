package com.cartagenacorp.lm_organizations.service.impl;

import com.cartagenacorp.lm_organizations.dto.OrganizationRequestDto;
import com.cartagenacorp.lm_organizations.dto.OrganizationResponseDto;
import com.cartagenacorp.lm_organizations.entity.Organization;
import com.cartagenacorp.lm_organizations.exception.BaseException;
import com.cartagenacorp.lm_organizations.mapper.OrganizationMapper;
import com.cartagenacorp.lm_organizations.repository.CrossDomainRepository;
import com.cartagenacorp.lm_organizations.repository.OrganizationRepository;
import com.cartagenacorp.lm_organizations.service.ConfigExternalService;
import com.cartagenacorp.lm_organizations.service.OrganizationService;
import com.cartagenacorp.lm_organizations.service.RoleExternalService;
import com.cartagenacorp.lm_organizations.util.ConstantUtil;
import com.cartagenacorp.lm_organizations.util.JwtContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final RoleExternalService roleExternalService;
    private final ConfigExternalService configExternalService;
    private final CrossDomainRepository crossDomainRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper,
                                   RoleExternalService roleExternalService, ConfigExternalService configExternalService,
                                   CrossDomainRepository crossDomainRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.roleExternalService = roleExternalService;
        this.configExternalService = configExternalService;
        this.crossDomainRepository = crossDomainRepository;
    }

    @Override
    @Transactional
    public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto){
        String token = JwtContextHolder.getToken();
        if (organizationRepository.existsByOrganizationName(organizationRequestDto.getOrganizationName())) {
            throw new BaseException(ConstantUtil.ORGANIZATION_NAME_ALREADY_EXISTS, HttpStatus.CONFLICT.value());
        }
        Organization organization = organizationMapper.toEntity(organizationRequestDto);
        Organization savedOrganization = organizationRepository.save(organization);

        roleExternalService.initializeDefaultRoles(savedOrganization.getOrganizationId(), token);
        configExternalService.initializeDefaultProjectStatus(savedOrganization.getOrganizationId(), token);
        return organizationMapper.toDto(savedOrganization);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponseDto getOrganizationById(UUID id){
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BaseException(ConstantUtil.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponseDto> getAllOrganizations(){
        List<Organization> organizations = organizationRepository.findAll();
        return organizations.stream()
                .map(organization -> organizationMapper.toDto(organization))
                .toList();
    }

    @Override
    @Transactional
    public OrganizationResponseDto updateOrganization(UUID id, OrganizationRequestDto organizationRequestDto){
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BaseException(ConstantUtil.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        if (organizationRepository.existsByOrganizationName(organizationRequestDto.getOrganizationName())) {
            throw new BaseException(ConstantUtil.ORGANIZATION_NAME_ALREADY_EXISTS, HttpStatus.CONFLICT.value());
        }

        Organization updatedOrganization = organizationMapper.partialUpdate(organizationRequestDto, organization);
        organizationRepository.save(updatedOrganization);
        return organizationMapper.toDto(updatedOrganization);
    }

    @Override
    @Transactional
    public void deleteOrganization(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BaseException(ConstantUtil.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        String token = JwtContextHolder.getToken();

        roleExternalService.deleteByOrganizationId(organization.getOrganizationId(), token);
        configExternalService.deleteByOrganizationId(organization.getOrganizationId(), token);
        organizationRepository.delete(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean organizationExists(UUID id) {
        return organizationRepository.existsById(id);
    }

    @Override
    @Transactional
    public void changeProjectOrganization(UUID projectId, UUID newOrgId) {
        crossDomainRepository.updateProjectOrganizationAndResetStatus(projectId, newOrgId);
        crossDomainRepository.updateProjectConfigOrganization(projectId, newOrgId);
        crossDomainRepository.updateIssuesOrganization(projectId, newOrgId);
        crossDomainRepository.updateCommentsOrganization(projectId, newOrgId);
    }
}
