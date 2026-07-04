package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.Watch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "watch_photos")
public class WatchPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false, name = "photo_url")
    private String photoUrl;

    @NotNull
    @Column(nullable = false, name = "photo_type")
    @Enumerated(EnumType.STRING)
    private PhotoType photoType;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;

    public WatchPhoto() {
    }

    public WatchPhoto(String photoUrl, PhotoType photoType, String description, Watch watch) {
        this.photoUrl = photoUrl;
        this.photoType = photoType;
        this.description = description;
        this.watch = watch;
    }

    public UUID getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public PhotoType getPhotoType() {
        return photoType;
    }

    public String getDescription() {
        return description;
    }

    public Watch getWatch() {
        return watch;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }
}
