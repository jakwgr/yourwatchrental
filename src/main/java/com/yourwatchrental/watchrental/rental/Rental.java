package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.watch.Watch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @NotNull
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(nullable = false, name = "total_price")
    @PositiveOrZero
    private BigDecimal totalPrice;

    @NotNull
    @Column(nullable = false, name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull
    @Column(nullable = false, name = "rental_status")
    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    @NotNull
    @Column(nullable = false, name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;
}
