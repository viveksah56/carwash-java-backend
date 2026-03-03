package com.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "wash_services")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashService {
    @Id
    @Column(name = "service_id", length = 36, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serviceId;
    @Column(name = "service_name", nullable = false, length = 150)
    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "duration_mins")
    private Integer durationMins = 30;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
