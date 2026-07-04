package com.yourwatchrental.watchrental.watch.watchhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, UUID> {
}
