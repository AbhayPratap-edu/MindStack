package dev.abhaya.mindstack.service.mailService;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.model.EmailToken;
import dev.abhaya.mindstack.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailTokenRepository emailTokenRepository;
    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(EmailVerificationService.class);

    @Value("${SENDGRID_API_KEY}")//environment variable comes from os/render
    private String sendGridApiKey;

    @Value("${SERVER_EMAIL}")
    private String serverEmail;

    @Value("${server.host.url}")
    private String serverHostUrl;

    @Value("${app.frontend.url}")//application property Comes from:application.properties OR env (if mapped)
    private String frontendUrl;

    @Transactional
    public void sendVerificationEmail(String userEmail) {

        // 1. Save token
        //String token = UUID.randomUUID().toString(); -> UUID is predictable enough for some attack models

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);


        //Clean old/expired email tokens
        emailTokenRepository.deleteByEmail(userEmail);
        //synchronize any changes made to entities managed by persistence context with underlying database
        emailTokenRepository.flush();


        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(userEmail);
        emailToken.setExpiry(Instant.now().plus(15, ChronoUnit.MINUTES));

        emailTokenRepository.save(emailToken);

        // 2. Build verification link
        String link = frontendUrl + "/verify?token=" + token;

        Mail mail = buildVerificationEmail(userEmail, link);


        mailService.sendMail(mail);


    }

    public String verifyEmail(String token) {

        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow( () -> new CustomMessageException("Invalid Email Token"));

        if(emailToken.getExpiry().isBefore(Instant.now())) {
            throw new CustomMessageException("Email Token Expired");
        }

        emailToken.setVerified(true);
        emailTokenRepository.save(emailToken);

        return emailToken.getEmail();

    }

    private Mail buildVerificationEmail(String userEmail, String link) {

        Email from = new Email(serverEmail);
        String subject = "Verify your MindStack account";
        Email to = new Email(userEmail);
        String htmlContent =
                "<h2>Verify your MindStack account</h2>" +
                        "<p>Hello,</p>" +
                        "<p>Thank you for registering on <b>MindStack</b>.</p>" +
                        "<p>Please verify your email by clicking the button below:</p>" +
                        "<a href='" + link + "' " +
                        "style='display:inline-block;padding:10px 20px;" +
                        "background-color:#4CAF50;color:white;text-decoration:none;" +
                        "border-radius:5px;'>Verify Email</a>" +
                        "<p>If you did not request this, you can ignore this email.</p>" +
                        "<br>" +
                        "<p>Regards,<br>MindStack Team</p>";
        Content body = new Content("text/html", htmlContent);

        return new Mail(from, subject, to, body);
    }


}
