package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.BranchService;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.rental.dto.request.PaymentStatusChangeRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalPeriodResponseDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.rental.exception.PaymentStatusChangeException;
import com.yourwatchrental.watchrental.rental.exception.RentalForbiddenExcpetion;
import com.yourwatchrental.watchrental.rental.exception.RentalNotFoundException;
import com.yourwatchrental.watchrental.rental.exception.RentalTooLateStatusChangeException;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import com.yourwatchrental.watchrental.user.exceptions.UserNotFoundException;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.WatchService;
import com.yourwatchrental.watchrental.watch.dto.request.WatchStatusUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchAvailabilityResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.Status;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        WatchFullInfoResponseDTO watchFullInfoResponseDTO = mock(WatchFullInfoResponseDTO.class);
        BranchResponseDTO branchResponseDTO = mock(BranchResponseDTO.class);
        UserResponseDTO userResponseDTO = mock(UserResponseDTO.class);

        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.CANCELLED,
                PaymentStatus.ON_SPOT,
                branchResponseDTO,
                userResponseDTO,
                watchFullInfoResponseDTO,
                LocalDateTime.now()
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

        UUID userId = UUID.randomUUID();
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(mock(User.class)));

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.cancelRental(rentalId));

        verify(rentalRepository).findById(rentalId);

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(any());
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
        UUID rentalUserUd = UUID.randomUUID();

        UUID userId = UUID.randomUUID();
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(mock(User.class)));


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
        verify(securityUtil).getCurrentUserId();
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
        verify(securityUtil).getCurrentUserId();
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

        WatchFullInfoResponseDTO watchFullInfoResponseDTO = mock(WatchFullInfoResponseDTO.class);
        BranchResponseDTO branchResponseDTO = mock(BranchResponseDTO.class);
        UserResponseDTO userResponseDTO = mock(UserResponseDTO.class);

        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.COMPLETED,
                PaymentStatus.ON_SPOT,
                branchResponseDTO,
                userResponseDTO,
                watchFullInfoResponseDTO,
                LocalDateTime.now()
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
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();
        User user = mock(User.class);

        UUID userId = UUID.randomUUID();
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        when(user.getId())
                .thenReturn(userId);

        rental.setUser(user);

        WatchFullInfoResponseDTO watchFullInfoResponseDTO = mock(WatchFullInfoResponseDTO.class);
        BranchResponseDTO branchResponseDTO = mock(BranchResponseDTO.class);
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                userId,
                "Jan",
                "Kowalski",
                LocalDate.of(2000, 1, 1),
                "jan.kowalski@example.com",
                "123456789",
                LocalDateTime.now(),
                Role.USER,
                UserStatus.ACTIVE
        );
        RentalResponseDTO rentalResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.COMPLETED,
                PaymentStatus.ON_SPOT,
                branchResponseDTO,
                userResponseDTO,
                watchFullInfoResponseDTO,
                LocalDateTime.now()
        );

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
        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(mock(User.class)));
        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.getRentalById(rentalId));

        verify(securityUtil).getCurrentUserId();
        verify(rentalRepository).findById(rentalId);
        verify(userRepository).findById(any());
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldThrowForbiddenWhenGettingById()
    {
        UUID rentalUserId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();

        Rental rental = new Rental();

        User rentalUser = mock(User.class);

        when(rentalUser.getId())
                .thenReturn(rentalUserId);

        rental.setUser(rentalUser);

        UUID userId = UUID.randomUUID();

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(mock(User.class)));

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));

        when(securityUtil.isAdmin())
                .thenReturn(false);


        assertThrows(RentalForbiddenExcpetion.class,
                () -> rentalService.getRentalById(rentalId));

        verify(securityUtil).getCurrentUserId();
        verify(rentalRepository).findById(rentalId);
        verify(userRepository).findById(any());
        verify(securityUtil).isAdmin();
        verify(rentalMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldGetMyRentals()
    {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        Rental rental = new Rental();
        rental.setUser(user);

        RentalResponseDTO rentalResponseDTO =
                mock(RentalResponseDTO.class);

        RentalFilterRequestDTO request = new RentalFilterRequestDTO(
                RentalStatus.IN_PROGRESS,
                PaymentStatus.PENDING,
                PaymentMethod.CARD,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Rental> rentalPage =
                new PageImpl<>(List.of(rental));

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rentalRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(rentalPage);

        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(rentalResponseDTO);

        Page<RentalResponseDTO> result =
                rentalService.getMyRentals(request, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(rentalResponseDTO, result.getContent().get(0));

        verify(securityUtil)
                .getCurrentUserId();

        verify(userRepository)
                .findById(userId);

        verify(rentalRepository)
                .findAll(any(Specification.class), eq(pageable));

        verify(rentalMapper)
                .toResponseDTO(rental);
    }

    @Test
    void shouldThrowUserNotFoundWhenGetMyRentals()
    {
        UUID userId = UUID.randomUUID();

        RentalFilterRequestDTO request = new RentalFilterRequestDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> rentalService.getMyRentals(request, pageable)
        );

        verify(securityUtil, times(2))
                .getCurrentUserId();

        verify(userRepository)
                .findById(userId);

        verify(rentalRepository, never())
                .findAll(any(Specification.class), any(Pageable.class));

        verify(rentalMapper, never())
                .toResponseDTO(any(Rental.class));
    }

    @Test
    void shouldGetAllRentals()
    {
        RentalFilterRequestDTO request = new RentalFilterRequestDTO(
                RentalStatus.IN_PROGRESS,
                PaymentStatus.PENDING,
                PaymentMethod.CARD,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        Rental rental = mock(Rental.class);
        RentalResponseDTO response = mock(RentalResponseDTO.class);

        when(rentalRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(new PageImpl<>(List.of(rental)));

        when(rentalMapper.toResponseDTO(rental))
                .thenReturn(response);

        Page<RentalResponseDTO> result =
                rentalService.getAllRentals(request, pageable);

        assertEquals(List.of(response), result.getContent());

        verify(rentalRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );

        verify(rentalMapper).toResponseDTO(rental);
    }

    @Test
    void shouldChangePaymentStatusToSuccessful()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();
        User user = mock(User.class);
        UUID userId = UUID.randomUUID();

        when(user.getId())
                .thenReturn(userId);
        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);
        rental.setUser(user);
        WatchFullInfoResponseDTO watchFullInfoResponseDTO = mock(WatchFullInfoResponseDTO.class);
        BranchResponseDTO branchResponseDTO = mock(BranchResponseDTO.class);
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                userId,
                "Jan",
                "Kowalski",
                LocalDate.of(2000, 1, 1),
                "jan.kowalski@example.com",
                "123456789",
                LocalDateTime.now(),
                Role.USER,
                UserStatus.ACTIVE
        );

        RentalResponseDTO rentalConfirmedResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.CONFIRMED,
                PaymentStatus.SUCCESSFUL,
                branchResponseDTO,
                userResponseDTO,
                watchFullInfoResponseDTO,
                LocalDateTime.now()
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

        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        RentalResponseDTO result = rentalService.changePaymentStatus(rentalId, paymentDTO);

        assertEquals(result, rentalConfirmedResponseDTO);

        assertEquals(PaymentStatus.SUCCESSFUL, result.paymentStatus());
        assertEquals(RentalStatus.CONFIRMED, result.rentalStatus());

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
        verify(userRepository).findById(any());
        verify(securityUtil).getCurrentUserId();
    }

    @Test
    void shouldChangePaymentStatusToFailed()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();
        User user = mock(User.class);
        UUID userId = UUID.randomUUID();

        when(user.getId())
                .thenReturn(userId);
        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);
        rental.setUser(user);
        WatchFullInfoResponseDTO watchFullInfoResponseDTO = mock(WatchFullInfoResponseDTO.class);
        BranchResponseDTO branchResponseDTO = mock(BranchResponseDTO.class);
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                userId,
                "Jan",
                "Kowalski",
                LocalDate.of(2000, 1, 1),
                "jan.kowalski@example.com",
                "123456789",
                LocalDateTime.now(),
                Role.USER,
                UserStatus.ACTIVE
        );

        RentalResponseDTO rentalConfirmedResponseDTO = new RentalResponseDTO(
                UUID.randomUUID(),
                LocalDate.now().plusWeeks(2),
                LocalDate.now().plusWeeks(3),
                new BigDecimal("560.00"),
                PaymentMethod.CASH,
                RentalStatus.PENDING,
                PaymentStatus.FAILED,
                branchResponseDTO,
                userResponseDTO,
                watchFullInfoResponseDTO,
                LocalDateTime.now()
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
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        RentalResponseDTO result = rentalService.changePaymentStatus(rentalId, paymentDTO);

        assertEquals(result, rentalConfirmedResponseDTO);

        assertEquals(PaymentStatus.FAILED, result.paymentStatus());
        assertEquals(RentalStatus.PENDING, result.rentalStatus());

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDTO(rental);
        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(any());
    }

    @Test
    void shouldThrowRentalNotFoundWhenChangingPaymentStatus()
    {
        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Rental rental = new Rental();
        User user = mock(User.class);
        rental.setPaymentStatus(PaymentStatus.PENDING);
        rental.setRentalStatus(RentalStatus.PENDING);
        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
                PaymentStatus.SUCCESSFUL
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.empty());
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));
        assertThrows(RentalNotFoundException.class,
                () -> rentalService.changePaymentStatus(rentalId, paymentDTO));

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
        verify(userRepository).findById(any());
        verify(securityUtil).getCurrentUserId();
    }

    @Test
    void shouldThrowPaymentStatusAlreadySuccessful()
    {
        UUID rentalId = UUID.randomUUID();
        Rental rental = new Rental();
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(user.getId())
                .thenReturn(userId);

        rental.setUser(user);
        rental.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        rental.setRentalStatus(RentalStatus.CONFIRMED);
        PaymentStatusChangeRequestDTO paymentDTO = new PaymentStatusChangeRequestDTO(
                PaymentStatus.SUCCESSFUL
        );

        when(rentalRepository.findById(rentalId))
                .thenReturn(Optional.of(rental));
        when(securityUtil.getCurrentUserId())
                .thenReturn(userId);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        assertThrows(PaymentStatusChangeException.class,
                () -> rentalService.changePaymentStatus(rentalId, paymentDTO));

        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository, never()).save(any());
        verify(rentalMapper, never()).toResponseDTO(any());
        verify(userRepository).findById(any());
        verify(securityUtil).getCurrentUserId();
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
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.PENDING
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
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.PENDING
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
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.PENDING

                )))
                .thenReturn(List.of());

        WatchAvailabilityResponseDTO result = rentalService.watchAvailabilityStatus(watchId, startDate, endDate);

        assertEquals(availability, result);

        assertEquals(0, result.unavailablePeriods().size());

        verify(rentalRepository).findActiveRentalsByWatchId(watchId, startDate, endDate,
                List.of(
                        RentalStatus.CONFIRMED,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.PENDING
                ));

    }
}
