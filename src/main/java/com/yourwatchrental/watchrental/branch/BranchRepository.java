package com.yourwatchrental.watchrental.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

        boolean existsByEmail(String email);
        boolean existsByPhoneNumber(String phoneNumber);
}
