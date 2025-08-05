package com.cartagenacorp.lm_organizations.mapper;

import com.cartagenacorp.lm_organizations.dto.OrganizationRequestDto;
import com.cartagenacorp.lm_organizations.dto.OrganizationResponseDto;
import com.cartagenacorp.lm_organizations.entity.Organization;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {
    Organization toEntity(OrganizationRequestDto organizationDto);

    OrganizationResponseDto toDto(Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Organization partialUpdate(OrganizationRequestDto organizationDto, @MappingTarget Organization organization);
}