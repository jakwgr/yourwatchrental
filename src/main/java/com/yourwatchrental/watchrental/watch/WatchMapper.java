package com.yourwatchrental.watchrental.watch;

import com.yourwatchrental.watchrental.branch.Branch;
import com.yourwatchrental.watchrental.branch.dto.BranchRequestDTO;
import com.yourwatchrental.watchrental.branch.dto.BranchResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchPageResponseDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchRequestDTO;
import com.yourwatchrental.watchrental.watch.dto.WatchResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchMapper {
    WatchResponseDTO toResponseDTO(Watch watch);
    Watch toEntity(WatchRequestDTO watch);
    WatchPageResponseDTO toWatchPageResponseDTO(Watch watch);
}
