package com.yourwatchrental.watchrental.security;

import com.yourwatchrental.watchrental.security.dto.UserForgotPasswordRequest;
import com.yourwatchrental.watchrental.security.dto.UserPasswordResetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody UserForgotPasswordRequest request
    ) {
        passwordResetTokenService.forgotPassword(request.email());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody UserPasswordResetRequest request
    ) {
        passwordResetTokenService.resetPassword(request);

        return ResponseEntity.ok().build();
    }
}
