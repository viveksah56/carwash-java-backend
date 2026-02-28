package com.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "branches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String branchId;
    @Column(name = "branch_name", nullable = false, length = 150)
    private String branchName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;
    @Column(name = "contact_number", length = 20)
    private String contactNumber;
    @Column(name = "working_hours", length = 100)
    private String workingHours;
    @Column(name = "is_active")
    private Boolean isActive = true;


}
