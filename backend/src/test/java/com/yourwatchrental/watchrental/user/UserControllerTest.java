package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.security.CustomUserDetailsService;
import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.user.dto.request.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwUtil jwUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;


    @Test
    void shouldGetUsers() throws Exception
    {
        UserResponseDTO response = mock(UserResponseDTO.class);

        when(userService.getUsers(any(UserFilterCriteriaRequestDTO.class)))
                .thenReturn(List.of(response));

        mockMvc.perform(
                get("/users")
        ).andExpect(status().isOk());

        verify(userService)
                .getUsers(any(UserFilterCriteriaRequestDTO.class));
    }

    @Test
    void shouldRegisterUser() throws Exception
    {
        UserRequestDTO request = new UserRequestDTO(
                "Jan",
                "Kowalski",
                LocalDate.of(2000, 1, 1),
                "123456789",
                "jan@gmail.com",
                "password"
        );

        UserResponseDTO response = mock(UserResponseDTO.class);

        when(userService.registerUser(any(UserRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated());

        verify(userService)
                .registerUser(any(UserRequestDTO.class));
    }

    @Test
    void shouldAuthenticateUser() throws Exception
    {
        UserLoginRequestDTO request = new UserLoginRequestDTO(
                "jan@gmail.com",
                "password"
        );

        when(userService.authenticateUser(any(UserLoginRequestDTO.class)))
                .thenReturn("jwt-token");

        mockMvc.perform(
                post("/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(userService)
                .authenticateUser(any(UserLoginRequestDTO.class));
    }

    @Test
    void shouldUpdateUser() throws Exception
    {
        UserResponseDTO response = mock(UserResponseDTO.class);

        UUID userId = UUID.randomUUID();

        UserInformationUpdateRequestDTO request = new UserInformationUpdateRequestDTO(
                "Jan",
                "Kowalski",
                LocalDate.of(2000, 1, 1),
                "123456789"
        );

        when(userService.updateUserAdmin(userId, request))
                .thenReturn(response);

        mockMvc.perform(
                put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(userService)
                .updateUserAdmin(any(), any());
    }

    @Test
    void shouldUpdatePassword() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserPasswordUpdateRequestDTO request = new UserPasswordUpdateRequestDTO(
                "newPassword",
                "newPassword",
                "oldPassword"
        );

        doNothing().when(userService)
                .updatePassword(request);

        mockMvc.perform(
                patch("/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        verify(userService)
                .updatePassword(any());
    }

    @Test
    void shouldUpdatePasswordAdmin() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserPasswordUpdateAdminRequestDTO request = new UserPasswordUpdateAdminRequestDTO(
                "newPassword",
                "newPassword"
        );

        doNothing().when(userService)
                .updatePasswordAdmin(userId, request);

        mockMvc.perform(
                patch("/users/admin/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        verify(userService)
                .updatePasswordAdmin(any(), any());
    }

    @Test
    void shouldUpdateEmail() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserEmailUpdateRequestDTO request = new UserEmailUpdateRequestDTO(
                "new@gmail.com",
                "password"
        );

        doNothing().when(userService)
                .updateEmail(request);

        mockMvc.perform(
                patch("/users/{id}/email", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        verify(userService)
                .updateEmail(any());
    }

    @Test
    void shouldUpdateEmailAdmin() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserEmailUpdateAdminRequestDTO request = new UserEmailUpdateAdminRequestDTO(
                "new@gmail.com"
        );

        doNothing().when(userService)
                .updateEmailAdmin(userId, request);

        mockMvc.perform(
                patch("/users/admin/{id}/email", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        verify(userService)
                .updateEmailAdmin(any(), any());
    }
    @Test
    void shouldGetUserById() throws Exception
    {
        UserResponseDTO response = mock(UserResponseDTO.class);

        UUID userId = UUID.randomUUID();

        when(userService.getUser(userId))
                .thenReturn(response);

        mockMvc.perform(
                get("/users/{id}", userId)
        ).andExpect(status().isOk());

        verify(userService)
                .getUser(any());
    }

    @Test
    void shouldSoftDeleteUser() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserSoftDeleteRequestDTO request = new UserSoftDeleteRequestDTO(
                "password"
        );

        doNothing().when(userService)
                .softDeleteUser(request);

        mockMvc.perform(
                delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        verify(userService)
                .softDeleteUser(any());
    }

    @Test
    void shouldChangeUserStatus() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserResponseDTO response = mock(UserResponseDTO.class);

        UserStatusChangeRequestDTO request = new UserStatusChangeRequestDTO(
                UserStatus.ACTIVE
        );

        when(userService.updateUserStatus(userId, request))
                .thenReturn(response);

        mockMvc.perform(
                patch("/users/{id}/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(userService)
                .updateUserStatus(any(), any());
    }

    @Test
    void shouldChangeUserRole() throws Exception
    {
        UUID userId = UUID.randomUUID();

        UserResponseDTO response = mock(UserResponseDTO.class);

        UserRoleChangeRequestDTO request = new UserRoleChangeRequestDTO(
                Role.ADMIN
        );

        when(userService.updateUserRole(userId, request))
                .thenReturn(response);

        mockMvc.perform(
                patch("/users/{id}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(userService)
                .updateUserRole(any(), any());
    }

    @Test
    void shouldHardDeleteUser() throws Exception
    {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService)
                .hardDeleteUser(userId);

        mockMvc.perform(
                delete("/users/{id}/delete", userId)
        ).andExpect(status().isNoContent());

        verify(userService)
                .hardDeleteUser(any());
    }
}