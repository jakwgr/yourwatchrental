package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.BranchService;
import com.yourwatchrental.watchrental.rental.dto.request.PaymentStatusChangeRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalPeriodResponseDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.rental.exception.PaymentStatusChangeException;
import com.yourwatchrental.watchrental.rental.exception.RentalForbiddenExcpetion;
import com.yourwatchrental.watchrental.rental.exception.RentalNotFoundException;
import com.yourwatchrental.watchrental.rental.exception.RentalTooLateStatusChangeException;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import com.yourwatchrental.watchrental.user.UserService;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.WatchService;
import com.yourwatchrental.watchrental.watch.dto.request.WatchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchAvailabilityResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.Status;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Test
    void shouldCancelRental()
    {
        User user = mock(User.class);
        Rental rental = new Rental();

        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.CANCELLED,
                PaymentStatus.ON_SPOT,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        rental.setRentalStatus(RentalStatus.PENDING);
        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(user.getId())
                .thenReturn(userId);

        rental.setUser(user);

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(securityUtil.isAdmin())
                .thenReturn(false);

        when(rentalRepository.save(rental))
                .thenReturn(rental);

        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalResponseDTO);

        RentalResponseDTO result = rentalService.cancelRental(rentalId);

        assertEquals(result, rentalResponseDTO);
        assertEquals(RentalStatus.CANCELLED, result.rentalStatus());

        verify(rentalRepository).findById(rentalId);
        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(securityUtil).isAdmin();
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldThrowRentalNotFoundWhenCancel()
    {
        Rental rental = new Rental();
        rental.setRentalStatus(RentalStatus.PENDING);
        UUID rentalId = UUID.randomUUID();

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.cancelRental(rentalId));

        verify(rentalRepository).findById(rentalId);

        verify(userRepository, never()).findById(any());
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldThrowForbiddenWhenCancel()
    {
        User user = mock(User.class);
        User rentalUser = mock(User.class);

        Rental rental = new Rental();

        rental.setRentalStatus(RentalStatus.PENDING);

        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID rentalUserUd = UUID.randomUUID();

        when(rentalUser.getId())
                .thenReturn(rentalUserUd);

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        rental.setUser(rentalUser);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(securityUtil.isAdmin())
                .thenReturn(false);

        assertThrows(RentalForbiddenExcpetion.class,
                () ->  rentalService.cancelRental(rentalId));

        verify(rentalRepository).findById(rentalId);
        verify(userRepository).findById(userId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
        verify(securityUtil).isAdmin();
    }

    @Test
    void shouldThrowTooLateWhenCancel()
    {
        User user = mock(User.class);

        Rental rental = new Rental();

        rental.setRentalStatus(RentalStatus.COMPLETED);

        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID rentalUserUd = UUID.randomUUID();

        when(user.getId())
                .thenReturn(rentalUserUd);

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        rental.setUser(user);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(securityUtil.isAdmin())
                .thenReturn(false);

        assertThrows(RentalTooLateStatusChangeException.class,
                () ->  rentalService.cancelRental(rentalId));

        verify(rentalRepository).findById(rentalId);
        verify(userRepository).findById(userId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
        verify(securityUtil).isAdmin();
    }

    @Test
    void shouldCompleteRental()
    {
        Rental rental = new Rental();
        UUID rentalId = UUID.randomUUID();

        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.COMPLETED,
                PaymentStatus.ON_SPOT,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental))
                .thenReturn(rental);
        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalResponseDTO);

        RentalResponseDTO result = rentalService.completeRental(rentalId);

        assertEquals(result, rentalResponseDTO);
        assertEquals(RentalStatus.COMPLETED, result.rentalStatus());
        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldThrowRentalNotFoundWhenComplete()
    {
        UUID rentalId = UUID.randomUUID();

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.completeRental(rentalId));

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldGetRentalById()
    {
        UUID userId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();
        User user = mock(User.class);

        when(user.getId())
                .thenReturn(userId);

        rental.setUser(user);

        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.COMPLETED,
                PaymentStatus.ON_SPOT,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(securityUtil.isAdmin())
                .thenReturn(false);
        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalResponseDTO);

        RentalResponseDTO result = rentalService.getRentalById(rentalId);

        assertEquals(result,rentalResponseDTO);

        verify(securityUtil).getCurrentUserId();
        verify(rentalRepository).findById(rentalId);
        verify(securityUtil).isAdmin();
        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldThrowRentalNotFoundWhenGettingById()
    {
        UUID userId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.getRentalById(rentalId));

        verify(securityUtil).getCurrentUserId();
        verify(rentalRepository).findById(rentalId);

        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldThrowForbiddenWhenGettingById()
    {
        UUID userId = UUID.randomUUID();
        UUID rentalUserId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();

        Rental rental = new Rental();

        User rentalUser = mock(User.class);

        when(rentalUser.getId())
                .thenReturn(rentalUserId);

        rental.setUser(rentalUser);

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));

        when(securityUtil.isAdmin())
                .thenReturn(false);


        assertThrows(RentalForbiddenExcpetion.class,
                () -> rentalService.getRentalById(rentalId));

        verify(securityUtil).getCurrentUserId();
        verify(rentalRepository).findById(rentalId);
        verify(securityUtil).isAdmin();
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldChangePaymentStatusToSuccessful()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();

        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);

        RentalResponseDTO rentalConfirmedResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.CONFIRMED,
                PaymentStatus.SUCCESSFUL,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
          PaymentStatus.SUCCESSFUL
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental))
                .thenReturn(rental);
        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalConfirmedResponseDTO);

        RentalResponseDTO result = rentalService.changePaymentStatus(rentalId, paymentDTO);

        assertEquals(result, rentalConfirmedResponseDTO);

        assertEquals(PaymentStatus.SUCCESSFUL, result.paymentStatus());
        assertEquals(RentalStatus.CONFIRMED, result.rentalStatus());

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldChangePaymentStatusToFailed()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();

        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);

        RentalResponseDTO rentalConfirmedResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.PENDING,
                PaymentStatus.FAILED,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
                PaymentStatus.FAILED
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental))
                .thenReturn(rental);
        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalConfirmedResponseDTO);

        RentalResponseDTO result = rentalService.changePaymentStatus(rentalId, paymentDTO);

        assertEquals(result, rentalConfirmedResponseDTO);

        assertEquals(PaymentStatus.FAILED, result.paymentStatus());
        assertEquals(RentalStatus.PENDING, result.rentalStatus());

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldThrowRentalNotFoundWhenChangingPaymentStatus()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();

        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);
        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
                PaymentStatus.SUCCESSFUL
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.changePaymentStatus(rentalId, paymentDTO));

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldThrowPaymentStatusAlreadySuccessful()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();

        rental.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        rental.setRentalStatus(RentalStatus.CONFIRMED);
        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
                PaymentStatus.SUCCESSFUL
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));

        assertThrows(PaymentStatusChangeException.class,
                () -> rentalService.changePaymentStatus(rentalId, paymentDTO));

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldReturnRentalPeriods()
    {
        UUID watchId = UUID.randomUUID();

        LocalDate startDate = LocalDate.parse("2026-10-01"),
                endDate = LocalDate.parse("2026-10-31");

        Rental rental = new Rental();
        rental.setStartDate(LocalDate.parse("2026-10-03"));
        rental.setEndDate(LocalDate.parse("2026-10-22"));

        RentalPeriodResponseDTO periodDTO = new RentalPeriodResponseDTO(
                rental.getStartDate(),
                rental.getEndDate()
        );

        WatchAvailabilityResponseDTO availability = new WatchAvailabilityResponseDTO(watchId, List.of(periodDTO));

        when(rentalRepository.findActiveRentalsByWatchId(watchId, startDate, endDate,
                List.of(
                        RentalStatus.CONFIRMED,
                        RentalStatus.IN_PROGRESS
                )))
                .thenReturn(List.of(rental));

        WatchAvailabilityResponseDTO result = rentalService.watchAvailabilityStatus(watchId, startDate, endDate);

        assertEquals(availability, result);

        assertEquals(1, result.unavailablePeriods().size());
        assertEquals(LocalDate.parse("2026-10-03"), result.unavailablePeriods().getFirst().startDate());
        assertEquals(LocalDate.parse("2026-10-22"), result.unavailablePeriods().getFirst().endDate());

        verify(rentalRepository).findActiveRentalsByWatchId(watchId, startDate, endDate,
                List.of(
                        RentalStatus.CONFIRMED,
                        RentalStatus.IN_PROGRESS
                ));

    }

    @Test
    void shouldReturnZeroRentalPeriods()
    {
        UUID watchId = UUID.randomUUID();
        Watch watch = mock(Watch.class);

        LocalDate startDate = LocalDate.parse("2036-10-01"),
                endDate = LocalDate.parse("2036-10-31");


        WatchAvailabilityResponseDTO availability = new WatchAvailabilityResponseDTO(watchId, List.of());

        when(rentalRepository.findActiveRentalsByWatchId(watchId, startDate, endDate,
                List.of(
                        RentalStatus.CONFIRMED,
                        RentalStatus.IN_PROGRESS
                )))
                .thenReturn(List.of());

        WatchAvailabilityResponseDTO result = rentalService.watchAvailabilityStatus(watchId, startDate, endDate);

        assertEquals(availability, result);

        assertEquals(0, result.unavailablePeriods().size());

        verify(rentalRepository).findActiveRentalsByWatchId(watchId, startDate, endDate,
                List.of(
                        RentalStatus.CONFIRMED,
                        RentalStatus.IN_PROGRESS
                ));

    }
}
