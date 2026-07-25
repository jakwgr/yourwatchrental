package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.user.dto.request.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;

import com.yourwatchrental.watchrental.user.exceptions.*;
import com.yourwatchrental.watchrental.user.exceptions.userChangeEmail.UserUpdateSameEmailException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateNotSamePasswordException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdatePasswordChangeDoesNotMatchException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateSamePasswordException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwUtil jwUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public UserResponseDTO registerUser(UserRequestDTO request)
    {
        if(userRepository.existsByEmail(request.email())) throw new UserEmailUsedException();
        if(userRepository.existsByPhoneNumber(request.phoneNumber())) throw new UserPhoneNumberUsedException();
        String encodedPassword = encoder.encode(request.password());
        User user = userMapper.toEntitySingup(request, encodedPassword);
        user.setRole(Role.USER);
        User createdUser = userRepository.save(user);


        return userMapper.toResponseDTO(createdUser);
    }

    public String authenticateUser(UserLoginRequestDTO request)
    {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserWrongLoginException());
        if(encoder.matches(request.password(), user.getPassword()))
        {
            Authentication authentication = authenticationManager.authenticate(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            user.getId(),
                            request.password()

                    )
            );

            return jwUtil
                    .generateToken(user.getId());
        }
        else
        {
            throw new UserWrongLoginException();
        }

    }

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
        if(request.phoneNumber() != null)
        {
            if(!Objects.equals(user.getPhoneNumber(), request.phoneNumber()))
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
        }
        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateRequestDTO request)
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository.findById(UUID.fromString(authentication.getName()))
                .orElseThrow(() -> new UserNotFoundException(null));
        if(!request.newPassword().equals(request.newPassword1())) {
            throw new UserUpdateNotSamePasswordException(user.getId());
        }

        if(!encoder.matches(request.password(), user.getPassword())) {
            throw new UserUpdatePasswordChangeDoesNotMatchException(user.getId());
        }

        if(encoder.matches(request.newPassword(), user.getPassword()))
        {
            throw new UserUpdateSamePasswordException();
        }
        user.setPassword(encoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(UserEmailUpdateRequestDTO request)
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository.findById(UUID.fromString(authentication.getName()))
                .orElseThrow(() -> new UserNotFoundException(null));

        if(!encoder.matches(request.password(), user.getPassword()))
        {
            throw new UserWrongPasswordException(user.getId());
        }

        if(request.email().equals(user.getEmail()))
        {
            throw new UserUpdateSameEmailException();
        }

        if(userRepository.existsByEmail(request.email())) throw new UserEmailUsedException();

        user.setEmail(request.email());
        userRepository.save(user);
    }

    public UserResponseDTO getUser()
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository.findById(UUID.fromString(authentication.getName()))
                .orElseThrow(() -> new UserNotFoundException(null));

        return userMapper.toResponseDTO(user);
    }

//    public void logoutUser()
//    {
//
//    }


    @Transactional
    public void deleteUser(UserDeleteRequestDTO request)
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository.findById(UUID.fromString(authentication.getName()))
                .orElseThrow(() -> new UserNotFoundException(null));

        if(!encoder.matches(request.password(), user.getPassword()))
        {
            throw new UserWrongPasswordException(user.getId());
        }
        if(!Objects.equals(request.deleteConfirm(), "Delete my account"))
        {
            throw new UserDeleteConfirmationException(user.getId());
        }
        userRepository.delete(user);
    }
}

