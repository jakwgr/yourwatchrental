package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.BranchService;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import com.yourwatchrental.watchrental.user.UserService;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.WatchService;
import com.yourwatchrental.watchrental.watch.dto.request.WatchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.enums.Status;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private WatchService watchService;
    @Mock
    private BranchService branchService;
    @Mock
    private UserService userService;

    @Mock
    private WatchRepository watchRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private RentalService rentalService;

    RentalRequestDTO requestCardDTO = new RentalRequestDTO(
            LocalDate.now().plusWeeks(2),
            LocalDate.now().plusWeeks(3),
            PaymentMethod.CARD,
            UUID.randomUUID()
    );

    RentalRequestDTO requestCashDTO = new RentalRequestDTO(
            LocalDate.now().plusWeeks(2),
            LocalDate.now().plusWeeks(3),
            PaymentMethod.CASH,
            UUID.randomUUID()
    );

    WatchStatusUpdateRequestDTO requestRentedDTO = new WatchStatusUpdateRequestDTO(
            Status.RENTED
    );

    @Test
    void shouldCheckIfWatchIsRentedTrue() {
        UUID watchId = UUID.randomUUID();
        LocalDate startDate = LocalDate.now().plusWeeks(2),
                endDate = LocalDate.now().plusWeeks(3);
        Watch watch = new Watch();

        when(watchRepository.findById(watchId))
                .thenReturn(Optional.of(watch));

        when(rentalRepository.existRentalInDates(
                watchId, startDate, endDate,
                (
                        List.of(
                                RentalStatus.PENDING,
                                RentalStatus.IN_PROGRESS,
                                RentalStatus.CONFIRMED
                        )
                )
        ))
                .thenReturn(true);

        boolean result = rentalService.isWatchRented(watchId, startDate, endDate);

        assertTrue(result);

        verify(watchRepository).findById(watchId);
        verify(rentalRepository).existRentalInDates(
                watchId, startDate, endDate,
                (
                        List.of(
                                RentalStatus.PENDING,
                                RentalStatus.IN_PROGRESS,
                                RentalStatus.CONFIRMED
                        )
                )
        );
    }

    @Test
    void shouldCheckIfWatchIsRentedFalse() {
        UUID watchId = UUID.randomUUID();
        LocalDate startDate = LocalDate.now().plusWeeks(2),
                endDate = LocalDate.now().plusWeeks(3);
        Watch watch = new Watch();

        when(watchRepository.findById(watchId))
                .thenReturn(Optional.of(watch));

        when(rentalRepository.existRentalInDates(
                watchId, startDate, endDate,
                (
                        List.of(
                                RentalStatus.PENDING,
                                RentalStatus.IN_PROGRESS,
                                RentalStatus.CONFIRMED
                        )
                )
        ))
                .thenReturn(false);

        boolean result = rentalService.isWatchRented(watchId, startDate, endDate);

        assertFalse(result);

        verify(watchRepository).findById(watchId);
        verify(rentalRepository).existRentalInDates(
                watchId, startDate, endDate,
                (
                        List.of(
                                RentalStatus.PENDING,
                                RentalStatus.IN_PROGRESS,
                                RentalStatus.CONFIRMED
                        )
                )
        );
    }

    @Test
    void shouldThrowWatchNotFoundIsWatchRented() {
        UUID watchId = UUID.randomUUID();
        Watch watch = new Watch();
        LocalDate startDate = LocalDate.now().plusWeeks(2),
                endDate = LocalDate.now().plusWeeks(3);

        when(watchRepository.findById(watchId))
                .thenReturn(Optional.empty());

        assertThrows(WatchNotFoundException.class,
                () -> rentalService.isWatchRented(watchId, startDate, endDate));

        verify(watchRepository).findById(watchId);
        verify(rentalRepository, never()).existRentalInDates(any(), any(), any(), any());
    }

}
