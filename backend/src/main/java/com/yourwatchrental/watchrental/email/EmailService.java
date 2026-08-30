package com.yourwatchrental.watchrental.email;

import com.yourwatchrental.watchrental.rental.dto.response.RentalResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, RentalResponseDTO response)
    {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            String text = """
            <h1>Welcome %s!</h1>

            <p>
            On %s, user %s %s, phone number %s, rented the following watch from your branch:
            %s %s, serial number %s, payment method: %s.
            </p>

            <h3>Rental period</h3>
            From %s to %s.

            <br>

            <p>
            If you have any problems, please contact us by email:
            <strong>yourwatchrental@op.pl</strong>
            </p>

            <h3>
            Best regards,<br>
            The YourWatchRental Team
            </h3>
            """.formatted(
                    response.branch().name(),
                    response.createdAt(),
                    response.user().firstName(),
                    response.user().lastName(),
                    response.user().phoneNumber()
                            .replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3"),
                    response.watch().manufacturer(),
                    response.watch().model(),
                    response.watch().serialNumber(),
                    response.paymentMethod(),
                    response.startDate(),
                    response.endDate()
            );

            helper.setFrom("yourwatchrental@op.pl");
            helper.setTo(to);
            helper.setSubject("New Rental!");
            helper.setText(text, true);

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new EmailSendException();
        }
    }
}
