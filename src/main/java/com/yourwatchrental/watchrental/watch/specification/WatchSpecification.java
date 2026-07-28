package com.yourwatchrental.watchrental.watch.specification;


import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.dto.request.WatchFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class WatchSpecification {

    public static Specification<Watch> containsStringIgnoreCase(String field, String value)
    {
        return ((root, query, criteriaBuilder) ->
        {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(field)),
                    "%" + value.toLowerCase() + "%"
            );
        });
    }

    public static <T> Specification<Watch> equalsEnum(String field, T value)
    {
        return ((root, query, criteriaBuilder) ->
        {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(field), value);
        });
    }

    public static <T extends Comparable<T>> Specification<Watch> betweenTwo(String field, T minValue, T maxValue)
    {
        return (root, query, criteriaBuilder) ->
        {
            if(maxValue != null && minValue != null)
            {
                return criteriaBuilder.between(root.get(field), minValue, maxValue);
            }

            if(maxValue != null)
            {
                return criteriaBuilder.lessThanOrEqualTo(root.get(field), maxValue);
            }

            if(minValue != null)
            {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(field), minValue);
            }

            return null;
        };
    }

    public static Specification<Watch> buildSpecification(WatchFilterRequestDTO request)
    {

        return Specification
                .where(WatchSpecification.containsStringIgnoreCase("manufacturer", request.manufacturer()))
                .and(WatchSpecification.containsStringIgnoreCase("model", request.model()))
                .and(WatchSpecification.containsStringIgnoreCase("movement", request.movement()))
                .and(WatchSpecification.containsStringIgnoreCase("referenceNumber", request.referenceNumber()))
                .and(WatchSpecification.containsStringIgnoreCase("serialNumber", request.serialNumber()))
                .and(WatchSpecification.equalsEnum("condition", request.condition()))
                .and(WatchSpecification.equalsEnum("gender", request.gender()))
                .and(WatchSpecification.equalsEnum("movementType", request.movementType()))
                .and(WatchSpecification.equalsEnum("status", request.status()))
                .and(WatchSpecification.equalsEnum("watchType", request.watchType()))
                .and(WatchSpecification.betweenTwo(
                        "pricePerDay",
                        request.minPrice(),
                        request.maxPrice()
                ))
                .and(WatchSpecification.betweenTwo(
                        "yearOfProduction",
                        request.minYear(),
                        request.maxYear()
                ));
    }
}
