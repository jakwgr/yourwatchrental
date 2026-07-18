package com.yourwatchrental.watchrental.user;

import com.yourwatchrental.watchrental.user.dto.request.UserFilterCriteriaRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserInformationUpdateRequestDTO;
import com.yourwatchrental.watchrental.user.dto.request.UserRequestDTO;
import com.yourwatchrental.watchrental.user.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(UserFilterCriteriaRequestDTO criteria)
    {
        return ResponseEntity.ok(userService.getUsers(criteria));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO request)
    {
        UserResponseDTO response = userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid UserInformationUpdateRequestDTO request)
    {
        UserResponseDTO response = userService.updateUser(id, request);

        return ResponseEntity
                .ok(response);
    }
}
