package com.yourwatchrental.watchrental.watch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WatchRepository  extends JpaRepository<Watch, UUID>,
        JpaSpecificationExecutor<Watch> {

    boolean existsBySerialNumber(String serialNumber);
}
