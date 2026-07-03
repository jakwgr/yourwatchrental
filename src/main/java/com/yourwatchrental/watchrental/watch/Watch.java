package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.rental.Rental;
import com.yourwatchrental.watchrental.watch.watchhistory.WatchHistory;
import com.yourwatchrental.watchrental.watch.watchphoto.WatchPhoto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "watches")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String manufacturer;

    @NotBlank
    @Column(nullable = false)
    private String model;

    @NotBlank
    @Column(nullable = false, name = "reference_number")
    private String referenceNumber;

    @NotBlank
    @Column(nullable = false)
    private String movement;

    private String description;

    @NotNull
    @Column(nullable = false, name = "year_of_production")
    @PositiveOrZero
    private int yearOfProduction;

    @NotNull
    @Column(nullable = false, name = "price_per_day")
    @PositiveOrZero
    private BigDecimal pricePerDay;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(nullable = false, name = "movement_type")
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Column(nullable = false, name = "watch_type")
    @Enumerated(EnumType.STRING)
    private WatchType watchType;

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchHistory> history = new ArrayList<>();

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchPhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();
}
