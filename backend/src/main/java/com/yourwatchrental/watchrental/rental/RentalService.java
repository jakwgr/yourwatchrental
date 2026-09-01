package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.BranchRepository;
import com.yourwatchrental.watchrental.branch.BranchService;
import com.yourwatchrental.watchrental.branch.exceptions.BranchNotFoundException;
import com.yourwatchrental.watchrental.email.EmailService;
import com.yourwatchrental.watchrental.rental.dto.request.PaymentStatusChangeRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalPeriodResponseDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.rental.exception.*;
import com.yourwatchrental.watchrental.rental.specifications.RentalSpecification;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import com.yourwatchrental.watchrental.user.UserService;
import com.yourwatchrental.watchrental.user.exceptions.UserNotFoundException;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.WatchService;
import com.yourwatchrental.watchrental.watch.dto.response.WatchAvailabilityResponseDTO;
import com.yourwatchrental.watchrental.watch.enums.Status;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    private final EmailService emailService;
    private final WatchService watchService;
    private final BranchService branchService;
    private final UserService userService;

    private final WatchRepository watchRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    private final SecurityUtil securityUtil;

    public boolean isWatchRented(UUID watchId, LocalDate startDate, LocalDate endDate)
    {
        watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));

        return rentalRepository.existRentalInDates(watchId, startDate, endDate,
                List.of(
                        RentalStatus.PENDING,
                        RentalStatus.IN_PROGRESS,
                        RentalStatus.CONFIRMED
                )
        );
    }




    @Transactional
    public RentalResponseDTO createRental(RentalRequestDTO request)
    {
        Watch watch = watchRepository.findByIdWithLock(request.watchId())
                .orElseThrow(() -> new WatchNotFoundException(request.watchId()));

        Branch branch = branchRepository.findById(watch.getBranch().getId())
                .orElseThrow(() -> new BranchNotFoundException(watch.getBranch().getId()));

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(securityUtil.getCurrentUserId()));

        if(watch.getStatus() == Status.UNAVAILABLE ||
                watch.getStatus() == Status.DISABLED ||
                watch.getStatus() == Status.IN_SERVICE)
        {
            throw new RentalWatchNotAvailableException();
        }
        if(isWatchRented(request.watchId(), request.startDate(), request.endDate()))
        {
            throw new RentalWatchNotAvailableException();
        }
        if(request.startDate().isAfter(request.endDate()))
        {
            throw new RentalBadDateRangeException(null);
        }
        if(!request.startDate().isAfter(LocalDate.now()))
        {
            throw new RentalBadDateRangeException(null);
        }
        if(request.endDate().isAfter(request.startDate().plusDays(20)))
        {
            throw new RentalBadDateRangeException(null);
        }

        BigDecimal daysBetween = BigDecimal.valueOf(ChronoUnit.DAYS.between(request.startDate(), request.endDate()) + 1);

        BigDecimal cost = watch.getPricePerDay().multiply(daysBetween);
        PaymentStatus status;
        RentalStatus rentalStatus;

        if(request.paymentMethod() == PaymentMethod.CASH)
        {
            status = PaymentStatus.ON_SPOT;
            rentalStatus = RentalStatus.CONFIRMED;
        }
        else
        {
            status = PaymentStatus.PENDING;
            rentalStatus = RentalStatus.PENDING;
        }

        Rental rental = new Rental(
                request.startDate(),
                request.endDate(),
                cost,
                request.paymentMethod(),
                rentalStatus,
                status,
                branch,
                user,
                watch
        );

        RentalResponseDTO response = rentalMapper.toResponseDTO(rentalRepository.save(rental));

//        emailService.sendEmail("yourwatchrental@interia.pl", response);
        return response;
    }

    @Transactional
    public RentalResponseDTO cancelRental(UUID rentalId) {

        User userId = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(securityUtil.getCurrentUserId()));



        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if (!securityUtil.isAdmin()) {
            if (!rental.getUser().getId().equals(userId.getId())) {
                throw new RentalForbiddenExcpetion(userId.getId());
            }
        }

        if (rental.getRentalStatus() == RentalStatus.CANCELLED ||
                rental.getRentalStatus() == RentalStatus.IN_PROGRESS ||
                rental.getRentalStatus() == RentalStatus.COMPLETED) {

            throw new RentalTooLateStatusChangeException(null);
        }

        rental.setRentalStatus(RentalStatus.CANCELLED);

        if (rental.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
            rental.setPaymentStatus(PaymentStatus.REFUNDED);
        } else {
            rental.setPaymentStatus(PaymentStatus.CANCELLED);
        }

        Rental savedRental = rentalRepository.save(rental);

        return rentalMapper.toResponseDTO(savedRental);
    }

    @Transactional
    public RentalResponseDTO completeRental(UUID rentalId) {

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        rental.setRentalStatus(RentalStatus.COMPLETED);
        Rental savedRental = rentalRepository.save(rental);

        return rentalMapper.toResponseDTO(savedRental);
    }

    public RentalResponseDTO getRentalById(UUID rentalId)
    {
        User userId = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(securityUtil.getCurrentUserId()));

        Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if(!securityUtil.isAdmin())
        {
            if(!rental.getUser().getId().equals(userId.getId()))
            {
                throw new RentalForbiddenExcpetion(userId.getId());
            }
        }

        return rentalMapper.toResponseDTO(rental);
    }

    @Transactional
    public Page<RentalResponseDTO> getMyRentals(
            RentalFilterRequestDTO request,
            Pageable page)
    {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(securityUtil.getCurrentUserId()));

        Specification<Rental> specification =
                RentalSpecification.buildSpecification(request);

        specification = specification.and(
                (root, query, cb) ->
                        cb.equal(root.get("user"), user)
        );

        return rentalRepository.findAll(specification, page)
                .map(rentalMapper::toResponseDTO);
    }

    @Transactional
    public Page<RentalResponseDTO> getAllRentals(RentalFilterRequestDTO request,
                                                 Pageable page)
    {
        Specification<Rental> specification = RentalSpecification.buildSpecification(request);

        return rentalRepository.findAll(specification, page)
                .map(rentalMapper::toResponseDTO);
    }

    @Transactional
    public RentalResponseDTO changePaymentStatus(UUID id, PaymentStatusChangeRequestDTO request)
    {
        User userId = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(securityUtil.getCurrentUserId()));

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(null));

        if(!securityUtil.isAdmin())
        {
            if(!rental.getUser().getId().equals(userId.getId()))
            {
                throw new RentalNotFoundException(null);
            }
        }

        PaymentStatus currentStatus = rental.getPaymentStatus();
        PaymentStatus newStatus = request.paymentStatus();

        if(currentStatus == PaymentStatus.ON_SPOT ||
                currentStatus == PaymentStatus.SUCCESSFUL)
        {
            throw new PaymentStatusChangeException(id);
        }

        if(currentStatus == PaymentStatus.PENDING &&
                newStatus != PaymentStatus.SUCCESSFUL &&
                newStatus != PaymentStatus.FAILED)
        {
            throw new PaymentStatusChangeException(id);
        }

        if(currentStatus == PaymentStatus.FAILED &&
                newStatus != PaymentStatus.SUCCESSFUL)
        {
            throw new PaymentStatusChangeException(id);
        }

        rental.setPaymentStatus(newStatus);

        if(newStatus == PaymentStatus.SUCCESSFUL &&
                rental.getRentalStatus() == RentalStatus.PENDING)
        {
            rental.setRentalStatus(RentalStatus.CONFIRMED);
        }

        return rentalMapper.toResponseDTO(
                rentalRepository.save(rental)
        );
    }

    public WatchAvailabilityResponseDTO watchAvailabilityStatus(UUID id,
                                                                LocalDate startDate,
                                                                LocalDate endDate)
    {
        List<Rental> rentals = rentalRepository.findActiveRentalsByWatchId(id, startDate, endDate, List.of(
                RentalStatus.CONFIRMED,
                RentalStatus.IN_PROGRESS,
                RentalStatus.PENDING
        ));

        return new WatchAvailabilityResponseDTO(
                id,
                rentals.stream()
                        .map(r -> new RentalPeriodResponseDTO(
                                r.getStartDate(),
                                r.getEndDate()
                        ))
                        .toList()
                );

    }
}
