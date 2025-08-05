package com.cartagenacorp.lm_organizations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.cartagenacorp.lm_organizations.entity.Organization}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDto implements Serializable {
    private UUID organizationId;
    private String organizationName;
    private LocalDateTime createdAt;
}