package com.yourwatchrental.watchrental.rental;

import com.yourwatchrental.watchrental.branch.BranchMapper;
import com.yourwatchrental.watchrental.rental.dto.request.RentalRequestDTO;
import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import com.yourwatchrental.watchrental.user.UserMapper;
import com.yourwatchrental.watchrental.watch.WatchMapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                BranchMapper.class,
                UserMapper.class,
                WatchMapper.class
        })
public interface RentalMapper {

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