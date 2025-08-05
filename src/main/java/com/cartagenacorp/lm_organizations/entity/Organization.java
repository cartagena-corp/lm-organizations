package com.cartagenacorp.lm_organizations.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization")
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
