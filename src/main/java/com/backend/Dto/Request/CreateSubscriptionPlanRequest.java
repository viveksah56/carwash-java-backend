package com.backend.Dto.Request;

import com.backend.Enum.PlanType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionPlanRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private PlanType planType;

    @PositiveOrZero
    private Integer totalWashes;

    @Positive
    private Integer durationDays;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull
    private Boolean active;

    @Size(max = 500)
    private String description;
}