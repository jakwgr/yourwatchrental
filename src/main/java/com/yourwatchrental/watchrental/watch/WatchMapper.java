package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.watch.dto.response.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchUpdateRequestDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchMapper {
    WatchResponseDTO toResponseDTO(Watch watch);
    Watch toEntity(WatchRequestDTO watch);
    WatchPageResponseDTO toWatchPageResponseDTO(Watch watch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(WatchUpdateRequestDTO request, @MappingTarget Watch entity);
}
