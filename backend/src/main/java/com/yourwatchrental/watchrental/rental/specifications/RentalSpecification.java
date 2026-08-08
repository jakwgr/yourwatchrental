package com.yourwatchrental.watchrental.rental.specifications;

import com.yourwatchrental.watchrental.common.specification.SpecificationUtil;
import com.yourwatchrental.watchrental.rental.Rental;
import com.yourwatchrental.watchrental.rental.dto.request.RentalFilterRequestDTO;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class RentalSpecification {
    public static Specification<Rental> buildSpecification(RentalFilterRequestDTO request) {

        return Specification
                .<Rental>where(
                        SpecificationUtil.equalsEnum(
                                "rentalStatus",
                                request.rentalStatus()
                        )
                )
                .and(SpecificationUtil.equalsEnum(
                        "paymentStatus",
                        request.paymentStatus()
                ))
                .and(SpecificationUtil.equalsEnum(
                        "paymentMethod",
                        request.paymentMethod()
                ))
                .and(SpecificationUtil.equals(
                        "watch.id",
                        request.watchId()
                ))
                .and(SpecificationUtil.equals(
                        "user.id",
                        request.userId()
                ))
                .and(SpecificationUtil.equals(
                        "branch.id",
                        request.branchId()
                ))
                .and(SpecificationUtil.betweenTwo(
                        "startDate",
                        request.startDateFrom(),
                        request.startDateTo()
                ))
                .and(SpecificationUtil.betweenTwo(
                        "endDate",
                        request.endDateFrom(),
                        request.endDateTo()
                ));
    }
}
