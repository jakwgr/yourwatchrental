package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoRequestDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchPhotoMapper {
    @Mapping(target = "watch", source = "watch")
    @Mapping(target = "description", source = "request.description")
    WatchPhoto toEntity(WatchPhotoRequestDTO request, Watch watch);

    @Mapping(target = "watchId", source = "watch.id")
    WatchPhotoResponseDTO toResponseDTO(WatchPhoto entity);
}
