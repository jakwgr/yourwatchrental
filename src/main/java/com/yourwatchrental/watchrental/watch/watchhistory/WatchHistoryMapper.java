package com.yourwatchrental.watchrental.watch.watchhistory;

import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.dto.request.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.request.WatchUpdateRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.response.WatchResponseDTO;
import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryRequestDTO;
import com.yourwatchrental.watchrental.watch.watchhistory.dto.WatchHistoryResponseDTO;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchHistoryMapper {
    @Mapping(target = "watchId", source = "watch.id")
    WatchHistoryResponseDTO toResponseDTO(WatchHistory watchhistory);

    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "watch", source = "watch")
    WatchHistory toEntity(WatchHistoryRequestDTO request, Watch watch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(WatchHistoryRequestDTO request, @MappingTarget WatchHistory entity);
}
