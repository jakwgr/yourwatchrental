package com.yourwatchrental.watchrental.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
