package com.cartagenacorp.lm_organizations.controller;

import com.cartagenacorp.lm_organizations.dto.NotificationResponse;
import com.cartagenacorp.lm_organizations.dto.OrganizationRequestDto;
import com.cartagenacorp.lm_organizations.dto.OrganizationResponseDto;
import com.cartagenacorp.lm_organizations.service.OrganizationService;
import com.cartagenacorp.lm_organizations.util.ConstantUtil;
import com.cartagenacorp.lm_organizations.util.RequiresPermission;
import com.cartagenacorp.lm_organizations.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/createOrganization")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_CREATE"})
    public ResponseEntity<OrganizationResponseDto> createOrganization(@Valid @RequestBody OrganizationRequestDto organizationRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizationService.createOrganization(organizationRequestDto));
    }

    @GetMapping("/getOrganization/{id}")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_READ"})
    public ResponseEntity<OrganizationResponseDto> getOrganizationById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getOrganizationById(uuid));
    }

    @GetMapping("/getAllOrganizations")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_READ"})
    public ResponseEntity<List<OrganizationResponseDto>> getAllOrganizations(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getAllOrganizations());
    }

    @PutMapping("/updateOrganization/{id}")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_UPDATE"})
    public ResponseEntity<OrganizationResponseDto> updateOrganization(@PathVariable String id, @Valid @RequestBody OrganizationRequestDto organizationRequestDto){
        UUID uuid = UUID.fromString(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.updateOrganization(uuid, organizationRequestDto));
    }

    @DeleteMapping("/deleteOrganization/{id}")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_DELETE"})
    public ResponseEntity<NotificationResponse> deleteOrganization(@PathVariable String  id){
        UUID uuid = UUID.fromString(id);
        organizationService.deleteOrganization(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseUtil.success(ConstantUtil.Success.RESOURCE_DELETED_SUCCESSFULLY));
    }

    @GetMapping("/organizationExists/{id}")
    @RequiresPermission({"ORGANIZATION_CONTROL", "ORGANIZATION_READ"})
    public ResponseEntity<Boolean> organizationExists(@PathVariable String id){
        UUID uuid = UUID.fromString(id);
        boolean exists = organizationService.organizationExists(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exists);
    }

    @PutMapping("/project/{projectId}/change-organization/{newOrgId}")
    @RequiresPermission({"ORGANIZATION_CONTROL"})
    public ResponseEntity<NotificationResponse> changeProjectOrganization(
            @PathVariable UUID projectId,
            @PathVariable UUID newOrgId
    ) {
        organizationService.changeProjectOrganization(projectId, newOrgId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseUtil.success(ConstantUtil.Success.PROJECT_ORGANIZATION_CHANGED_SUCCESSFULLY));
    }
}
