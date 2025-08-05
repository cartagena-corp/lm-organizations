package com.cartagenacorp.lm_organizations.dto;

import com.cartagenacorp.lm_organizations.entity.Organization;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link Organization}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequestDto implements Serializable {
    @NotBlank(message = "El nombre de la organización no puede estar vacío")
    private String organizationName;
}