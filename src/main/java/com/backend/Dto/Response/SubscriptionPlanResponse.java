package com.backend.Dto.Response;

import com.backend.Entity.Subscription;
import com.backend.Enum.PlanType;
import com.backend.Enum.SubscriptionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {

    private String subscriptionId;
    private String planName;
    private PlanType planType;
    private Integer totalWashes;
    private Integer usedWashes;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private SubscriptionStatus subStatus;
    private String customerId;

    public static SubscriptionResponse convertToSubscriptionResponse(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getSubscriptionId())
                .planName(subscription.getPlanName())
                .planType(subscription.getPlanType())
                .totalWashes(subscription.getTotalWashes())
                .usedWashes(subscription.getUsedWashes())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .price(subscription.getPrice())
                .subStatus(subscription.getSubStatus())
                .customerId(subscription.getUser() != null ? subscription.getUser().getUserId() : null)
                .build();
    }
}