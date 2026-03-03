package com.backend.Entity;

import com.backend.Enum.PlanType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "subscription_plans",
        indexes = {
                @Index(name = "idx_plan_active", columnList = "active"),
                @Index(name = "idx_plan_type", columnList = "planType")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "plan_id", length = 36, updatable = false, nullable = false)
    private String planId;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 30)
    private PlanType planType;

    @Column(name = "total_washes")
    private Integer totalWashes;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
}