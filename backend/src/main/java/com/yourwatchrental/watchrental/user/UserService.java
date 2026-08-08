package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.dto.request.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;

import com.yourwatchrental.watchrental.user.exceptions.*;
import com.yourwatchrental.watchrental.user.exceptions.userChangeEmail.UserUpdateSameEmailException;
import com.yourwatchrental.watchrental.user.exceptions.UserDisabledException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateNotSamePasswordException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdatePasswordChangeDoesNotMatchException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateSamePasswordException;
import com.yourwatchrental.watchrental.user.exceptions.userUpdate.UserSamePhoneNumberException;
import com.yourwatchrental.watchrental.user.exceptions.userUpdate.UserSameRoleException;
import com.yourwatchrental.watchrental.user.exceptions.userUpdate.UserSameStatusException;
import com.yourwatchrental.watchrental.user.exceptions.userUpdate.UserWrongPasswordException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final JwUtil jwUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final SecurityUtil securityUtil;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public UserResponseDTO registerUser(UserRequestDTO request)
    {
        if(userRepository.existsByEmail(request.email())) throw new UserEmailUsedException();
        if(userRepository.existsByPhoneNumber(request.phoneNumber())) throw new UserPhoneNumberUsedException();
        String encodedPassword = encoder.encode(request.password());
        User user = userMapper.toEntitySingup(request, encodedPassword);
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        User createdUser = userRepository.save(user);


        return userMapper.toResponseDTO(createdUser);
    }

    public String authenticateUser(UserLoginRequestDTO request)
    {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserWrongLoginException());
        if(encoder.matches(request.password(), user.getPassword()))
        {
            if(user.getStatus() == UserStatus.DISABLED) throw new UserDisabledException(user.getId());

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

//        System.out.println(criteria.id());
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
        }
        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateRequestDTO request)
    {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(null));

        //jedno i drugie nowe hasla sa rozne
        if(!request.newPassword().equals(request.newPassword1())) {
            throw new UserUpdateNotSamePasswordException(user.getId());
        }
        //stare haslo jest nieprawidłowe
        if(!encoder.matches(request.password(), user.getPassword())) {
            throw new UserUpdatePasswordChangeDoesNotMatchException(user.getId());
        }
        //nowe hasło = stare hasło
        if(encoder.matches(request.newPassword(), user.getPassword()))
        {
            throw new UserUpdateSamePasswordException();
        }
        user.setPassword(encoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updatePasswordAdmin(UUID userId, UserPasswordUpdateAdminRequestDTO request)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(null));

        if(!request.newPassword().equals(request.newPassword1())) {
            throw new UserUpdateNotSamePasswordException(user.getId());
        }
        if(!request.newPassword().equals(user.getPassword()))
        {
            throw new UserUpdateSamePasswordException();
        }
        user.setPassword(encoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(UserEmailUpdateRequestDTO request)
    {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
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

    @Transactional
    public void updateEmailAdmin(UUID userId, UserEmailUpdateAdminRequestDTO request)
    {

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(null));

        if(request.email().equals(user.getEmail()))
        {
            throw new UserUpdateSameEmailException();
        }

        if(userRepository.existsByEmail(request.email())) throw new UserEmailUsedException();

        user.setEmail(request.email());
        userRepository.save(user);
    }

    public UserResponseDTO getUser(UUID id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(null));

        return userMapper.toResponseDTO(user);
    }

//    public void logoutUser()
//    {
//
//    }

    @Transactional
    public void softDeleteUser(UserSoftDeleteRequestDTO request)
    {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException(null));

        if(securityUtil.isAdmin())
        {
            throw new UserAdminDeactivateException(securityUtil.getCurrentUserId());
        }

        if(!encoder.matches(request.password(), user.getPassword()))
        {
            throw new UserWrongPasswordException(user.getId());
        }
        user.setStatus(UserStatus.DISABLED);
    }

    @Transactional
    public UserResponseDTO updateUserStatus(UUID id, UserStatusChangeRequestDTO request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(user.getStatus() == request.status()) {
            throw new UserSameStatusException();
        }

        user.setStatus(request.status());
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUserRole(UUID id, UserRoleChangeRequestDTO request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(user.getRole() == request.role()) {
            throw new UserSameRoleException();
        }

        user.setRole(request.role());
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    public void hardDeleteUser(UUID id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}

