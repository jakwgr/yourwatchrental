package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.BranchService;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.rental.exception.RentalBadDateRangeException;
import com.yourwatchrental.watchrental.rental.exception.RentalWatchNotAvailableException;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import com.yourwatchrental.watchrental.user.UserService;
import com.yourwatchrental.watchrental.user.exceptions.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RentalCreateServiceTest {

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

    @Test
    void shouldCreateRentalCash() {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        User user = new User();
        UUID watchId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        RentalResponseDTO rentalResponseCashDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal(
                        String.valueOf(BigDecimal.valueOf(80).multiply(
                                BigDecimal.valueOf(
                                        ChronoUnit.DAYS.between(
                                                requestCashDTO.startDate(),
                                                requestCashDTO.endDate()
                                        ) + 1
                                )
                        ))),
                PaymentMethod.CASH,
                RentalStatus.CONFIRMED,
                PaymentStatus.ON_SPOT,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(watch.getId())
                .thenReturn(watchId);

        when(watch.getBranch())
                .thenReturn(branch);

        when(watch.getPricePerDay())
                .thenReturn(BigDecimal.valueOf(80.00));


        when(watchRepository.findById(requestCashDTO.watchId()))
                .thenReturn(Optional.of(watch));

        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));


        when(rentalRepository.existRentalInDates(
                requestCashDTO.watchId(),
                requestCashDTO.startDate(),
                requestCashDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        ))
                .thenReturn(false);


        when(watchService.updateWatchStatus(
                eq(watchId),
                any(WatchStatusUpdateRequestDTO.class)
        ))
                .thenReturn(null);


        when(rentalRepository.save(any(Rental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(rentalMapper.toResponseDTO(any(Rental.class)))
                .thenReturn(rentalResponseCashDTO);


        RentalResponseDTO result = rentalService.createRental(requestCashDTO);


        assertEquals(result, rentalResponseCashDTO);
        assertEquals(PaymentMethod.CASH, result.paymentMethod());
        assertEquals(RentalStatus.CONFIRMED, result.rentalStatus());


        assertEquals(
                rentalResponseCashDTO.totalPrice(),
                result.totalPrice()
        );


        verify(watchRepository, times(2)).findById(requestCashDTO.watchId());
        verify(branchRepository).findById(branch.getId());
        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(userId);

        verify(rentalRepository).existRentalInDates(
                requestCashDTO.watchId(),
                requestCashDTO.startDate(),
                requestCashDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );

        verify(watchService).updateWatchStatus(
                watchId,
                new WatchStatusUpdateRequestDTO(Status.RENTED)
        );

        verify(rentalRepository).save(any(Rental.class));
        verify(rentalMapper).toResponseDTO(any(Rental.class));
    }

    @Test
    void shouldCreateRentalCard() {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        User user = new User();
        UUID watchId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        RentalResponseDTO rentalResponseCardDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal(
                        String.valueOf(BigDecimal.valueOf(80).multiply(
                                BigDecimal.valueOf(
                                        ChronoUnit.DAYS.between(
                                                requestCardDTO.startDate(),
                                                requestCardDTO.endDate()
                                        ) + 1
                                )
                        ))),
                PaymentMethod.CARD,
                RentalStatus.PENDING,
                PaymentStatus.ON_SPOT,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(watch.getId())
                .thenReturn(watchId);

        when(watch.getBranch())
                .thenReturn(branch);

        when(watch.getPricePerDay())
                .thenReturn(BigDecimal.valueOf(80.00));


        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));

        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));


        when(rentalRepository.existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        ))
                .thenReturn(false);


        when(watchService.updateWatchStatus(
                eq(watchId),
                any(WatchStatusUpdateRequestDTO.class)
        ))
                .thenReturn(null);


        when(rentalRepository.save(any(Rental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(rentalMapper.toResponseDTO(any(Rental.class)))
                .thenReturn(rentalResponseCardDTO);


        RentalResponseDTO result = rentalService.createRental(requestCardDTO);


        assertEquals(result, rentalResponseCardDTO);
        assertEquals(PaymentMethod.CARD, result.paymentMethod());
        assertEquals(RentalStatus.PENDING, result.rentalStatus());


        assertEquals(
                rentalResponseCardDTO.totalPrice(),
                result.totalPrice()
        );


        verify(watchRepository, times(2)).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(branch.getId());
        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(userId);

        verify(rentalRepository).existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );

        verify(watchService).updateWatchStatus(
                watchId,
                new WatchStatusUpdateRequestDTO(Status.RENTED)
        );

        verify(rentalRepository).save(any(Rental.class));
        verify(rentalMapper).toResponseDTO(any(Rental.class));
    }

    @Test
    void shouldThrowWatchNotFoundOnCreate()
    {
        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.empty());

        assertThrows(WatchNotFoundException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository).findById(requestCardDTO.watchId());

        verify(branchRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());

        verify(rentalRepository, never()).save(any());

    }

    @Test
    void shouldThrowBranchNotFoundOnCreate()
    {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();

        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));

        when(watch.getBranch())
                .thenReturn(branch);

        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(watch.getBranch().getId());
        verify(userRepository, never()).findById(any());
        verify(rentalRepository, never()).save(any());

    }

    @Test
    void shouldThrowUserNotFoundOnCreate()
    {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        UUID userId = UUID.randomUUID();

        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));
        when(watch.getBranch())
                .thenReturn(branch);
        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(watch.getBranch().getId());
        verify(userRepository).findById(userId);
        verify(rentalRepository, never()).save(any());

    }

    @Test
    void shouldThrowWatchNotAvailableOnCreate()
    {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        User user = new User();
        UUID userId = UUID.randomUUID();

        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));
        when(watch.getBranch())
                .thenReturn(branch);
        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(rentalRepository.existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        )).thenReturn(true);

        assertThrows(RentalWatchNotAvailableException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository, times(2)).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(watch.getBranch().getId());
        verify(userRepository).findById(userId);
        verify(rentalRepository).existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );
        verify(rentalRepository, never()).save(any());

    }

    @Test
    void shouldThrowBadDateRangeStartBiggerThanEndOnCreate()
    {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        User user = new User();
        UUID userId = UUID.randomUUID();

        RentalRequestDTO requestCardDTO = new RentalRequestDTO(
                LocalDate.now().plusWeeks(3),
                LocalDate.now().plusWeeks(2),
                PaymentMethod.CARD,
                UUID.randomUUID()
        );

        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));
        when(watch.getBranch())
                .thenReturn(branch);
        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(rentalRepository.existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        )).thenReturn(false);


        assertThrows(RentalBadDateRangeException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository, times(2)).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(watch.getBranch().getId());
        verify(userRepository).findById(userId);
        verify(rentalRepository).existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );
        verify(rentalRepository, never()).save(any());

    }

    @Test
    void shouldThrowBadDateRangeStartEarlierThanToday()
    {
        Watch watch = mock(Watch.class);
        Branch branch = new Branch();
        User user = new User();
        UUID userId = UUID.randomUUID();

        RentalRequestDTO requestCardDTO = new RentalRequestDTO(
                LocalDate.now().minusWeeks(3),
                LocalDate.now().plusWeeks(2),
                PaymentMethod.CARD,
                UUID.randomUUID()
        );

        when(watchRepository.findById(requestCardDTO.watchId()))
                .thenReturn(Optional.of(watch));
        when(watch.getBranch())
                .thenReturn(branch);
        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(rentalRepository.existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        )).thenReturn(false);


        assertThrows(RentalBadDateRangeException.class,
                () -> rentalService.createRental(requestCardDTO));

        verify(watchRepository, times(2)).findById(requestCardDTO.watchId());
        verify(branchRepository).findById(watch.getBranch().getId());
        verify(userRepository).findById(userId);
        verify(rentalRepository).existRentalInDates(
                requestCardDTO.watchId(),
                requestCardDTO.startDate(),
                requestCardDTO.endDate(),
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );
        verify(rentalRepository, never()).save(any());

    }
}
