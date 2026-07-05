package com.yourwatchrental.watchrental.branch;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {
    BranchResponseDTO toResponseDTO(Branch branch);
    Branch toEntity(BranchRequestDTO request);

    void updateEntityFromDTO(BranchRequestDTO request, @MappingTarget Branch entity);
}
