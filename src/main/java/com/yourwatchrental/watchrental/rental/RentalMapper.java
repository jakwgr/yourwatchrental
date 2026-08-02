package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalMapper {

    @Mapping(source = "watch.id", target = "watchId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "branch.id", target = "branchId")
    RentalResponseDTO toResponseDTO(Rental rental);


    @Mapping(target = "watch", ignore = true)
    @Mapping(target = "user", ignore = true)
    Rental toEntity(RentalRequestDTO request);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "watch", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDTO(
            RentalRequestDTO request,
            @MappingTarget Rental entity
    );
}