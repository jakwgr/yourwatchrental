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
}
