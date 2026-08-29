package com.yourwatchrental.watchrental.watch;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchRepository  extends JpaRepository<Watch, UUID>,
        JpaSpecificationExecutor<Watch> {

    boolean existsBySerialNumber(String serialNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Watch w WHERE w.id = :id")
    Optional<Watch> findByIdWithLock(@Param("id") UUID id);
}
