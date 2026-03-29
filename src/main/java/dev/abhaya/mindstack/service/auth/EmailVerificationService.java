package dev.abhaya.mindstack.service.auth;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.model.EmailToken;
import dev.abhaya.mindstack.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailTokenRepository emailTokenRepository;
    private static final Logger log = LoggerFactory.getLogger(EmailVerificationService.class);

    @Value("${SENDGRID_API_KEY}")//environment variable comes from os/render
    private String sendGridApiKey;

    @Value("${SERVER_EMAIL}")
    private String serverEmail;

    @Value("${server.host.url}")
    private String serverHostUrl;

    @Value("${app.frontend.url}")//application property Comes from:application.properties OR env (if mapped)
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String userEmail) {

        // 1. Save token
        String token = UUID.randomUUID().toString();

        //Clean old/expired email tokens
        emailTokenRepository.deleteByEmail(userEmail);

        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(userEmail);
        emailToken.setExpiry(LocalDateTime.now().plusMinutes(15));
        emailTokenRepository.save(emailToken);

        // 2. Build verification link
        String link = frontendUrl + "/verify?token=" + token;

        Mail mail = buildVerificationEmail(userEmail, link);

        try {
            sendMail(mail);// 3. Send via SendGrid
        }
        catch (IOException e) {
            log.error("Error while sending email via SendGrid", e);
        }

    }

    public String verifyEmail(String token) {

        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow( () -> new CustomMessageException("Invalid Email Token"));

        if(emailToken.getExpiry().isBefore(LocalDateTime.now())) {
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

    private void sendMail(Mail mail) throws IOException {
        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        log.info("SendGrid status: {}, body: {}",
                response.getStatusCode(), response.getBody());

    }
}
