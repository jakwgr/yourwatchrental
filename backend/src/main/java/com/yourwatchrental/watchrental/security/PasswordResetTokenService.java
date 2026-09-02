package com.yourwatchrental.watchrental.security;

import com.yourwatchrental.watchrental.email.EmailService;
import com.yourwatchrental.watchrental.security.dto.UserPasswordResetRequest;
import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${app.password-reset.expiration-minutes}")
    private long windowMinutes;

    @Value("${app.password-reset.secret}")
    private String secret;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public String generateToken(User user) {

        long timeWindow = getCurrentTimeWindow();

        String data = buildData(user, timeWindow);

        return generateHmac(data);
    }

    public boolean isValid(User user, String token) {

        long currentWindow = getCurrentTimeWindow();

        String currentToken = generateHmac(
                buildData(user, currentWindow)
        );

        String previousToken = generateHmac(
                buildData(user, currentWindow - 1)
        );

        return MessageDigest.isEqual(
                token.getBytes(StandardCharsets.UTF_8),
                currentToken.getBytes(StandardCharsets.UTF_8)
        ) || MessageDigest.isEqual(
                token.getBytes(StandardCharsets.UTF_8),
                previousToken.getBytes(StandardCharsets.UTF_8)
        );
    }

    private String buildData(User user, long timeWindow) {

        return user.getId()
                + ":"
                + user.getPassword()
                + ":"
                + timeWindow;
    }

    private long getCurrentTimeWindow() {

        long windowSeconds = windowMinutes * 60;

        return Instant.now().getEpochSecond() / windowSeconds;
    }

    private String generateHmac(String data) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec key = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(key);

            byte[] hash = mac.doFinal(
                    data.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Could not generate password reset token",
                    e
            );
        }
    }

    public void forgotPassword(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            System.out.println("USER NOT FOUND");
            return;
        }

        User user = optionalUser.get();

        String token = generateToken(user);

        String resetUrl = frontendUrl
                + "/reset-password?userId="
                + user.getId()
                + "&token="
                + URLEncoder.encode(
                token,
                StandardCharsets.UTF_8
        );

        // emailService.sendPasswordResetEmail(
        //         user.getEmail(),
        //         resetUrl
        // );
    }

    public void resetPassword(UserPasswordResetRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(
                        InvalidPasswordResetTokenException::new
                );

        if (!isValid(user, request.token())) {
            throw new InvalidPasswordResetTokenException();
        }

        user.setPassword(
                encoder.encode(request.newPassword())
        );

        userRepository.save(user);
    }
}
