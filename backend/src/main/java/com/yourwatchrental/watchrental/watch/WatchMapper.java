package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.BranchMapper;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchShortResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchCardResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchFullInfoResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.WatchPhoto;
import com.yourwatchrental.watchrental.watch.watchphoto.WatchPhotoMapper;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoShortResponseDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {WatchPhotoMapper.class, BranchMapper.class})

public interface WatchMapper {

    WatchResponseDTO toResponseDTO(Watch watch);
    Watch toEntity(WatchRequestDTO watch);
    WatchPageResponseDTO toWatchPageResponseDTO(Watch watch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(WatchUpdateRequestDTO request, @MappingTarget Watch entity);

    default WatchCardResponseDTO toCardDTO(Watch watch) {

        WatchPhoto thumbnail = watch.getThumbnail();

        return new WatchCardResponseDTO(
                watch.getId(),
                watch.getManufacturer(),
                watch.getModel(),
                watch.getPricePerDay(),
                watch.getStatus(),
                watch.getBranch().getName(),
                thumbnail != null ? thumbnail.getPhotoUrl() : null
        );
    }

    default WatchFullInfoResponseDTO toFullInfoDTO(Watch watch) {


        return new WatchFullInfoResponseDTO(
                watch.getId(),
                watch.getManufacturer(),
                watch.getModel(),
                watch.getReferenceNumber(),
                watch.getSerialNumber(),
                watch.getMovement(),
                watch.getDescription(),
                watch.getYearOfProduction(),
                watch.getPricePerDay(),
                watch.getCondition(),
                watch.getGender(),
                watch.getMovementType(),
                watch.getStatus(),
                watch.getWatchType(),
                new BranchShortResponseDTO(
                        watch.getBranch().getId(),
                        watch.getBranch().getCity(),
                        watch.getBranch().getName(),
                        watch.getBranch().getAddress(),
                        watch.getBranch().getPhoneNumber()
                ),
                watch.getPhotos()
                        .stream()
                        .map(photo -> new WatchPhotoShortResponseDTO(
                                photo.getId(),
                                photo.getPhotoUrl(),
                                photo.getPhotoType(),
                                photo.getDescription()
                        ))
                        .toList()
        );
    }

}
