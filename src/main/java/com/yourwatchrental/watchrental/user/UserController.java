package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.user.dto.request.*;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
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

    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.name")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid UserInformationUpdateRequestDTO request)
    {
        UserResponseDTO response = userService.updateUser(id, request);

        return ResponseEntity
                .ok(response);
    }

    @PreAuthorize("#id.toString() == authentication.name")
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @RequestBody @Valid UserPasswordUpdateRequestDTO request)
    {
        userService.updatePassword(request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("#id.toString() == authentication.name")
    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> updateEmail(@PathVariable UUID id, @RequestBody @Valid UserEmailUpdateRequestDTO request)
    {
        userService.updateEmail(request);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("#id.toString() == authentication.name")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id)
    {
        UserResponseDTO response = userService.getUser();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("#id.toString() == authentication.name")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, UserDeleteRequestDTO request)
    {
        userService.deleteUser(request);
        return ResponseEntity
                .noContent()
                .build();
    }
}
