package com.yourwatchrental.watchrental.watch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchRepository  extends JpaRepository<Watch, UUID> {
}
