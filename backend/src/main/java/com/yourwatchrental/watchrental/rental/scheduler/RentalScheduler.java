package com.yourwatchrental.watchrental.rental.scheduler;


import com.yourwatchrental.watchrental.rental.PaymentStatus;
import com.yourwatchrental.watchrental.rental.RentalRepository;
import com.yourwatchrental.watchrental.rental.RentalStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RentalScheduler {
    private final RentalRepository rentalRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateRentalStatuses()
    {
        LocalDate today = LocalDate.now();

        rentalRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .forEach(rental -> {

                    if(rental.getRentalStatus() == RentalStatus.CONFIRMED) {
                        rental.setRentalStatus(RentalStatus.IN_PROGRESS);
                    }
                });


        rentalRepository
                .findByRentalStatusAndStartDateLessThanEqual(
                        RentalStatus.PENDING,
                        today
                )
                .forEach(rental -> {

                    if(rental.getPaymentStatus() == PaymentStatus.PENDING
                            || rental.getPaymentStatus() == PaymentStatus.FAILED) {

                        rental.setRentalStatus(RentalStatus.CANCELLED);
                    }
                });
    }

}
