package com.backend.Entity;

import com.backend.Enum.PlanType;
import com.backend.Enum.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "subscriptions",
        indexes = {
                @Index(name = "idx_subscription_customer", columnList = "customer_id"),
                @Index(name = "idx_subscription_status", columnList = "sub_status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", length = 36, updatable = false, nullable = false)
    private String subscriptionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Column(nullable = false, length = 100)
    private String planName;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 20)
    private PlanType planType;

    @Column(name = "total_washes")
    private Integer totalWashes;

    @Column(name = "used_washes", nullable = false)
    private Integer usedWashes;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_status", nullable = false)
    private SubscriptionStatus subStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        if (usedWashes == null) {
            usedWashes = 0;
        }
        if (subStatus == null) {
            subStatus = SubscriptionStatus.ACTIVE;
        }
        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }

    public boolean isExpired() {
        return endDate != null && LocalDate.now().isAfter(endDate);
    }

    public boolean isWashUnavailable() {
        updateStatusIfExpired();

        if (subStatus != SubscriptionStatus.ACTIVE) {
            return true;
        }

        if (planType == PlanType.UNLIMITED) {
            return false;
        }

        return totalWashes == null
                || usedWashes == null
                || usedWashes >= totalWashes;
    }

    public void useWash() {
        if (isWashUnavailable()) {
            throw new IllegalStateException("Subscription cannot be used");
        }

        if (planType != PlanType.UNLIMITED) {
            usedWashes = (usedWashes == null ? 0 : usedWashes) + 1;
        }
    }

    public void updateStatusIfExpired() {
        if (isExpired()) {
            subStatus = SubscriptionStatus.EXPIRED;
        }
    }
}