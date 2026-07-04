package com.yourwatchrental.watchrental.watch.watchphoto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchPhotoRepository  extends JpaRepository<WatchPhoto, UUID> {
}
