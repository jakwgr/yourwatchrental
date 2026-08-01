package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchPhotoRepository  extends JpaRepository<WatchPhoto, UUID> {

    List<WatchPhoto> findAllByWatch(Watch watch);

    Optional<WatchPhoto> findByWatchAndPhotoType(Watch watch, PhotoType photoType);

    boolean existsByWatchAndPhotoType(Watch watch, PhotoType photoType);
}
