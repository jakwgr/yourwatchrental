package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.rental.Rental;
import com.yourwatchrental.watchrental.watch.enums.*;
import com.yourwatchrental.watchrental.watch.watchhistory.WatchHistory;
import com.yourwatchrental.watchrental.watch.watchphoto.PhotoType;
import com.yourwatchrental.watchrental.watch.watchphoto.WatchPhoto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "watches")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, name = "reference_number")
    private String referenceNumber;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String movement;

    private String description;

    @Column(nullable = false, name = "year_of_production")
    @PositiveOrZero
    private int yearOfProduction;

    @Column(nullable = false, name = "price_per_day")
    @PositiveOrZero
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, name = "movement_type")
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, name = "watch_type")
    @Enumerated(EnumType.STRING)
    private WatchType watchType;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchHistory> history = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchPhoto> photos = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "watch")
    private List<Rental> rentals = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    public Watch(String manufacturer, String model, String referenceNumber, String movement, int yearOfProduction, String description, BigDecimal pricePerDay, Condition condition, Gender gender, MovementType movementType, Status status, WatchType watchType, Branch branch) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.referenceNumber = referenceNumber;
        this.movement = movement;
        this.yearOfProduction = yearOfProduction;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.condition = condition;
        this.gender = gender;
        this.movementType = movementType;
        this.status = status;
        this.watchType = watchType;
        this.branch = branch;
    }

    public WatchPhoto getThumbnail() {
        return photos.stream()
                .filter(photo -> photo.getPhotoType() == PhotoType.FRONT)
                .findFirst()
                .orElseGet(() -> photos.stream()
                        .filter(photo -> photo.getPhotoType() == PhotoType.FULL)
                        .findFirst()
                        .orElseGet(() -> photos.stream()
                                .filter(photo -> photo.getPhotoType() == PhotoType.BACK)
                                .findFirst()
                                .orElse(null)
                        ));
    }
}
