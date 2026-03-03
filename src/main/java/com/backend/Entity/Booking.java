package com.backend.Entity;

import com.backend.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", nullable = false, updatable = false)
    private String bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_customer"))
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_vehicle"))
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_service"))
    private WashService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_branch"))
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private BookingStatus bookingStatus = BookingStatus.PENDING;

    @Column( nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column( nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal weekendSurcharge = BigDecimal.ZERO;

    @Column()
    private Integer pointsRedeemed = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime paymentDeadline;

    @Column(columnDefinition = "TEXT")
    private String notes;
}