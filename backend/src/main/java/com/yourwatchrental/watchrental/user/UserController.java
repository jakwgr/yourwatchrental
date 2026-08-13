package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.security.JwUtil;
import com.yourwatchrental.watchrental.security.SecurityUtil;
import com.yourwatchrental.watchrental.user.dto.request.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(UserFilterCriteriaRequestDTO criteria)
    {
        return ResponseEntity.ok(userService.getUsers(criteria));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO request)
    {
        UserResponseDTO response = userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody @Valid UserLoginRequestDTO request)
    {
        return userService.authenticateUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserAdmin(@PathVariable UUID id, @RequestBody @Valid UserInformationUpdateRequestDTO request)
    {
        UserResponseDTO response = userService.updateUserAdmin(id, request);

        return ResponseEntity
                .ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserInformationUpdateRequestDTO request)
    {
        UserResponseDTO response = userService.updateUser(request);

        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UserPasswordUpdateRequestDTO request)
    {
        userService.updatePassword(request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}/password")
    public ResponseEntity<Void> updatePasswordAdmin(@PathVariable UUID id, @RequestBody @Valid UserPasswordUpdateAdminRequestDTO request)
    {
        userService.updatePasswordAdmin(id, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/me/email")
    public ResponseEntity<Void> updateEmail(@RequestBody @Valid UserEmailUpdateRequestDTO request)
    {
        userService.updateEmail(request);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("admin/{id}/email")
    public ResponseEntity<Void> updateEmailAdmin(@PathVariable UUID id, @RequestBody @Valid UserEmailUpdateAdminRequestDTO request)
    {
        userService.updateEmailAdmin(id, request);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserAdmin(@PathVariable UUID id)
    {
        UserResponseDTO response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUser()
    {
        UUID id = securityUtil.getCurrentUserId();
        UserResponseDTO response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> softDeleteUser(@RequestBody @Valid UserSoftDeleteRequestDTO request)
    {
        userService.softDeleteUser(request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> changeStatusUser(@PathVariable UUID id, @RequestBody @Valid UserStatusChangeRequestDTO request)
    {
        return ResponseEntity.ok(userService.updateUserStatus(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(@PathVariable UUID id, @RequestBody @Valid UserRoleChangeRequestDTO request)
    {
        return ResponseEntity.ok(userService.updateUserRole(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable UUID id)
    {
        userService.hardDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
