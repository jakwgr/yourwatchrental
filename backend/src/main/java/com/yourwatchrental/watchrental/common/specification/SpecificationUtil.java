package com.yourwatchrental.watchrental.common.specification;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtil {

    public static  <E> Specification<E> containsStringIgnoreCase(String field, String value)
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

    public static <T, E> Specification<E> equalsEnum(String field, T value)
    {
        return ((root, query, criteriaBuilder) ->
        {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(field), value);
        });
    }

    public static <T extends Comparable<T>, E> Specification<E> betweenTwo(String field, T minValue, T maxValue)
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

    public static <E, V> Specification<E> equals(String field, V value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }

            if (field.contains(".")) {
                String[] fields = field.split("\\.");

                return cb.equal(
                        root.get(fields[0]).get(fields[1]),
                        value
                );
            }

            return cb.equal(root.get(field), value);
        };
    }
}
