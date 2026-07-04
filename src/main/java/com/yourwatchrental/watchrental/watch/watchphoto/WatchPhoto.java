package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.Watch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "watch_photos")
public class WatchPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
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

    public WatchPhoto(String photoUrl, PhotoType photoType, String description, Watch watch) {
        this.photoUrl = photoUrl;
        this.photoType = photoType;
        this.description = description;
        this.watch = watch;
    }
}
