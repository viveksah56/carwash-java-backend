package com.backend.Dto.Response;

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
    private String planId;
    private String planName;
    private PlanType planType;
    private Integer totalWashes;
    private Integer usedWashes;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private SubscriptionStatus subStatus;
}