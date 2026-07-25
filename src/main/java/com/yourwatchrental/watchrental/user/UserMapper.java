package com.yourwatchrental.watchrental.user;

import ch.qos.logback.core.model.ComponentModel;
import com.yourwatchrental.watchrental.user.dto.request.UserRequestDTO;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);
    User toEntity(UserRequestDTO request);

    @Mapping(target = "password", source = "password")
    User toEntitySingup(UserRequestDTO request, String password);
}
