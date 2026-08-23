package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID>,
        JpaSpecificationExecutor<Rental> {
    List<Rental> findByRentalStatusAndEndDateLessThan(
            RentalStatus rentalStatus,
            LocalDate date
    );

    List<Rental> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Rental> findByRentalStatusAndStartDateLessThanEqual(
            RentalStatus rentalStatus,
            LocalDate date
    );

    Page<Rental> findByUserIdOrderByStartDateDesc(UUID userId, Pageable page);

    @Query("""
        SELECT r FROM Rental r
        WHERE r.watch.id = :watchId
        AND r.rentalStatus IN :statuses
        AND r.startDate <= :endDate
        AND r.endDate >= :startDate
        """)
    List<Rental> findActiveRentalsByWatchId(
            @Param("watchId") UUID watchId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("statuses") List<RentalStatus> statuses
    );

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
                        FROM Rental r
                        WHERE r.watch.id = :watchId
                        AND r.rentalStatus IN :statuses
                        AND r.startDate <= :endDate
                        AND r.endDate >= :startDate""")
    boolean existRentalInDates(
            @Param("watchId") UUID watchId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("statuses") List<RentalStatus> statuses
    );
}
