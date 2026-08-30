package com.yourwatchrental.watchrental.watch.specification;


import com.yourwatchrental.watchrental.common.specification.SpecificationUtil;
import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class WatchSpecification {

    public static Specification<Watch> buildSpecification(WatchFilterRequestDTO request)
    {

        return Specification
                .<Watch>where(SpecificationUtil.equals("id", request.watchId()))
                        .and(SpecificationUtil.containsStringIgnoreCase("manufacturer", request.manufacturer()))
                .and(SpecificationUtil.containsStringIgnoreCase("model", request.model()))
                .and(SpecificationUtil.containsStringIgnoreCase("movement", request.movement()))
                .and(SpecificationUtil.containsStringIgnoreCase("referenceNumber", request.referenceNumber()))
                .and(SpecificationUtil.containsStringIgnoreCase("serialNumber", request.serialNumber()))
                .and(SpecificationUtil.equalsEnum("condition", request.condition()))
                .and(SpecificationUtil.equalsEnum("gender", request.gender()))
                .and(SpecificationUtil.equalsEnum("movementType", request.movementType()))
                .and(SpecificationUtil.equalsEnum("status", request.status()))
                .and(SpecificationUtil.equalsEnum("watchType", request.watchType()))
                .and(SpecificationUtil.betweenTwo(
                        "pricePerDay",
                        request.minPrice(),
                        request.maxPrice()
                ))
                .and(SpecificationUtil.betweenTwo(
                        "yearOfProduction",
                        request.minYear(),
                        request.maxYear()
                ))
                .and(SpecificationUtil.equals("branch.id", request.branchId()));
    }
}
