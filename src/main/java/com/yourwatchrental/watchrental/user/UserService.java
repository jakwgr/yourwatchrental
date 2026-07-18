package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.user.dto.request.UserFilterCriteriaRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserInformationUpdateRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserRequestDTO;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;

import com.yourwatchrental.watchrental.user.exceptions.UserIdNotFoundExcpetion;
import com.yourwatchrental.watchrental.user.exceptions.UserNotFoundException;
import com.yourwatchrental.watchrental.user.exceptions.UserPhoneNumberUsedException;
import com.yourwatchrental.watchrental.user.exceptions.UserSamePhoneNumberException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public UserResponseDTO registerUser(UserRequestDTO request)
    {
        User user = userMapper.toEntity(request);
        user.setRole(Role.USER);
        User createdUser = userRepository.save(user);

        return userMapper.toResponseDTO(createdUser);
    }

//    public UserResponseDTO loginUser(UserRequestDTO request)
//    {
//
//    }

    public List<UserResponseDTO> getUsers(UserFilterCriteriaRequestDTO criteria){
//        tylko dla admina

        System.out.println(criteria.id());
        if(criteria.id() != null && !criteria.id().isEmpty())
        {
            if(UUID_PATTERN.matcher(criteria.id().trim()).matches()) {
                UUID searchUUID = UUID.fromString(criteria.id());
                return userRepository.findById(searchUUID)
                        .stream()
                        .map(userMapper::toResponseDTO)
                        .toList();
            }
            else
            {
                throw new UserIdNotFoundExcpetion();
            }
        }
        User probe = new User();
        probe.setEmail(criteria.email());
        probe.setFirstName(criteria.firstName());
        probe.setLastName(criteria.lastName());
        probe.setPhoneNumber(criteria.phoneNumber());

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<User> example = Example.of(probe, exampleMatcher);

        List<User> users = userRepository.findAll(example);

        return users
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    };

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserInformationUpdateRequestDTO request)
    {
        User user = userRepository
                .findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));

        if(request.firstName() != null) user.setFirstName(request.firstName());
        if(request.lastName() != null) user.setLastName(request.lastName());
        if(request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());
        if(request.phoneNumber() != null && !Objects.equals(user.getPhoneNumber(), request.phoneNumber()))
        {
            if (!userRepository.existsByPhoneNumber(request.phoneNumber()))
            {
                user.setPhoneNumber(request.phoneNumber());
            }
            else
            {
                throw new UserPhoneNumberUsedException();
            }
        }
        else
        {
            throw new UserSamePhoneNumberException();
        }


        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

//    @Transactional
//    public UserResponseDTO updateUserEmail(UUID id, String email)
//    {
//     if(userRepository.existsByEmail(email));
//    }
}

