package com.backend.Dto.Response;

import com.backend.Entity.SubscriptionPlan;
import com.backend.Enum.PlanType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {

    private String planId;
    private String name;
    private PlanType planType;
    private Integer totalWashes;
    private Integer durationDays;
    private BigDecimal price;
    private Boolean active;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private  String createdBy;

    public static SubscriptionPlanResponse convertTo(SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) return null;
        return SubscriptionPlanResponse.builder()
                .planId(subscriptionPlan.getPlanId())
                .name(subscriptionPlan.getName())
                .planType(subscriptionPlan.getPlanType())
                .totalWashes(subscriptionPlan.getTotalWashes())
                .durationDays(subscriptionPlan.getDurationDays())
                .price(subscriptionPlan.getPrice())
                .active(subscriptionPlan.getActive())
                .description(subscriptionPlan.getDescription())
                .createdAt(subscriptionPlan.getCreatedAt())
                .updatedAt(subscriptionPlan.getUpdatedAt())
                .createdBy(subscriptionPlan.getCreatedBy())
                .build();
    }
}