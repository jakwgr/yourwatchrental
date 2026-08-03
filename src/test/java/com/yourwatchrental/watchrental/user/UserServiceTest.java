package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.dto.request.UserInformationUpdateRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserPasswordUpdateRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserRequestDTO;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import com.yourwatchrental.watchrental.user.exceptions.UserEmailUsedException;
import com.yourwatchrental.watchrental.user.exceptions.UserNotFoundException;
import com.yourwatchrental.watchrental.user.exceptions.UserPhoneNumberUsedException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateNotSamePasswordException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdatePasswordChangeDoesNotMatchException;
import com.yourwatchrental.watchrental.user.exceptions.userChangePassword.UserUpdateSamePasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwUtil jwUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private SecurityUtil securityUtil;
    @InjectMocks
    private UserService userService;

    UserRequestDTO requestDTO = new UserRequestDTO(
            "Jan",
            "Kowalski",
            LocalDate.of(2002, 5, 15),
            "123456789",
            "jan.kowalski@test.com",
            "Password123"
    );

    UserResponseDTO userResponseDTO = new UserResponseDTO(
            UUID.randomUUID(),
            "Jan",
            "Kowalski",
            LocalDate.of(2002, 5, 15),
            "jan.kowalski@test.com",
            "123456789",
            LocalDateTime.now(),
            Role.USER,
            UserStatus.ACTIVE
    );

    UserInformationUpdateRequestDTO requestInformationUpdate =
            new UserInformationUpdateRequestDTO(
                    "Jan",
                    "Nowak",
                    LocalDate.of(2002, 5, 15),
                    "987654321"
            );

    @Test
    void shouldRegisterUser() {
        User user = new User();

        user.setPassword(requestDTO.password());

        when(userRepository.existsByEmail(requestDTO.email()))
                .thenReturn(false);
        when(userRepository.existsByPhoneNumber(requestDTO.phoneNumber()))
                .thenReturn(false);
        when(encoder.encode(requestDTO.password()))
                .thenReturn("encodedPassword");
        when(userMapper.toEntitySingup(requestDTO, "encodedPassword"))
                .thenReturn(user);
        when(userMapper.toResponseDTO(user))
                .thenReturn(userResponseDTO);
        when(userRepository.save(user))
                .thenReturn(user);

        UserResponseDTO result = userService.registerUser(requestDTO);

        assertEquals(Role.USER, result.role());
        assertEquals(UserStatus.ACTIVE, result.status());

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userRepository).existsByPhoneNumber(requestDTO.phoneNumber());
        verify(encoder).encode(requestDTO.password());
        verify(userMapper).toEntitySingup(requestDTO, "encodedPassword");
        verify(userMapper).toResponseDTO(user);
        verify(userRepository).save(user);
        verify(encoder).encode(requestDTO.password());
    }

    @Test
    void shouldThrowUsedEmail1() {
        when(userRepository.existsByEmail(requestDTO.email()))
                .thenReturn(true);

        assertThrows(UserEmailUsedException.class,
                () -> userService.registerUser(requestDTO));

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowUsedPhone1() {
        when(userRepository.existsByEmail(requestDTO.email()))
                .thenReturn(false);
        when(userRepository.existsByPhoneNumber(requestDTO.phoneNumber()))
                .thenReturn(true);

        assertThrows(UserPhoneNumberUsedException.class,
                () -> userService.registerUser(requestDTO));

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userRepository).existsByPhoneNumber(requestDTO.phoneNumber());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGetUser() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUser(id);

        assertEquals(result, userResponseDTO);

        verify(userRepository).findById(id);
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void shouldThrowUserNotFound1() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(id));

        verify(userRepository).findById(id);
        verify(userMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldUpdateUser() {
        UUID id = UUID.randomUUID();

        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByPhoneNumber(requestInformationUpdate.phoneNumber()))
                .thenReturn(false);
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toResponseDTO(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO result =
                userService.updateUser(id, requestInformationUpdate);

        assertEquals(result, userResponseDTO);
        assertEquals(requestInformationUpdate.firstName(), user.getFirstName());
        assertEquals(requestInformationUpdate.lastName(), user.getLastName());
        assertEquals(requestInformationUpdate.dateOfBirth(), user.getDateOfBirth());
        assertEquals(requestInformationUpdate.phoneNumber(), user.getPhoneNumber());

        verify(userRepository).findById(id);
        verify(userRepository).existsByPhoneNumber(requestInformationUpdate.phoneNumber());
        verify(userRepository).save(user);
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void shouldThrowUserNotFound2() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(id, requestInformationUpdate));


        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }


    @Test
    void shouldThrowUsedPhone2() {
        UUID id = UUID.randomUUID();

        User user = new User();

        user.setPhoneNumber("123456789");

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByPhoneNumber(requestInformationUpdate.phoneNumber()))
                .thenReturn(true);


        assertThrows(UserPhoneNumberUsedException.class,
                () -> userService.updateUser(id, requestInformationUpdate));


        verify(userRepository).findById(id);
        verify(userRepository).existsByPhoneNumber(requestInformationUpdate.phoneNumber());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdatePassword() {
        User user = new User();

        user.setPassword("oldPassword");


        UUID id = UUID.randomUUID();

        UserPasswordUpdateRequestDTO request =
                new UserPasswordUpdateRequestDTO(
                        "newPassword",
                        "newPassword",
                        "oldPassword"
                );


        when(securityUtil.getCurrentUserId())
                .thenReturn(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(encoder.matches(request.password(), user.getPassword()))
                .thenReturn(true);
        when(encoder.matches(request.newPassword(), user.getPassword()))
                .thenReturn(false);

        when(encoder.encode(request.newPassword()))
                .thenReturn("encodedPassword");

        userService.updatePassword(request);

        assertEquals("encodedPassword", user.getPassword());

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(id);

        verify(encoder).matches(request.password(), "oldPassword");
        verify(encoder).matches(request.newPassword(), "oldPassword");

        verify(encoder).encode(request.newPassword());
        verify(userRepository).save(user);
    }


    @Test
    void shouldThrowUserNotFound3() {
        UUID id = UUID.randomUUID();

        UserPasswordUpdateRequestDTO request =
                new UserPasswordUpdateRequestDTO(
                        "newPassword",
                        "newPassword",
                        "oldPassword"
                );

        when(securityUtil.getCurrentUserId())
                .thenReturn(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updatePassword(request));

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrow1and2newPasswordDifferent() {
        UUID id = UUID.randomUUID();
        User user = new User();

        UserPasswordUpdateRequestDTO request =
                new UserPasswordUpdateRequestDTO(
                        "newPassword",
                        "newPassword1",
                        "oldPassword"
                );

        when(securityUtil.getCurrentUserId())
                .thenReturn(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        assertThrows(UserUpdateNotSamePasswordException.class,
                () -> userService.updatePassword(request));

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWrongOldPassword()
    {
        UUID id = UUID.randomUUID();
        User user = new User();

        UserPasswordUpdateRequestDTO request =
                new UserPasswordUpdateRequestDTO(
                        "newPassword",
                        "newPassword",
                        "oldPassword"
                );
        user.setPassword("oldPassword1");

        when(securityUtil.getCurrentUserId())
                .thenReturn(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword()))
                .thenReturn(false);

        assertThrows(UserUpdatePasswordChangeDoesNotMatchException.class,
                () -> userService.updatePassword(request));

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(id);
        verify(encoder).matches(request.password(),user.getPassword());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowNewPasswordEqualsOld()
    {
        UUID id = UUID.randomUUID();
        User user = new User();

        UserPasswordUpdateRequestDTO request =
                new UserPasswordUpdateRequestDTO(
                        "oldPassword",
                        "oldPassword",
                        "oldPassword"
                );
        user.setPassword("oldPassword");

        when(securityUtil.getCurrentUserId())
                .thenReturn(id);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword()))
                .thenReturn(true);
        when(encoder.matches(request.newPassword(), user.getPassword()))
                .thenReturn(true);

        assertThrows(UserUpdateSamePasswordException.class,
                () -> userService.updatePassword(request));

        verify(securityUtil).getCurrentUserId();
        verify(userRepository).findById(id);
        verify(encoder, times(2)).matches(request.password(), user.getPassword());
        verify(userRepository, never()).save(any());
    }

}
