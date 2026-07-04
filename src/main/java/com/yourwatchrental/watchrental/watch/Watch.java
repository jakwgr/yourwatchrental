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

    public Watch() {
    }

    public Watch(String manufacturer, String model, String referenceNumber, String movement, int yearOfProduction, String description, BigDecimal pricePerDay, Condition condition, Gender gender, MovementType movementType, Status status, WatchType watchType) {
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
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public UUID getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getMovement() {
        return movement;
    }

    public String getDescription() {
        return description;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public Condition getCondition() {
        return condition;
    }

    public Gender getGender() {
        return gender;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public Status getStatus() {
        return status;
    }

    public WatchType getWatchType() {
        return watchType;
    }

    public List<WatchHistory> getHistory() {
        return history;
    }

    public List<WatchPhoto> getPhotos() {
        return photos;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setWatchType(WatchType watchType) {
        this.watchType = watchType;
    }
}
