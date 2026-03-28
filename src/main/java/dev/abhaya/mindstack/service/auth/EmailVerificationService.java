package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.model.EmailToken;
import dev.abhaya.mindstack.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final EmailTokenRepository emailTokenRepository;

    public void sendVerificationEmail(String email) {

        String token = UUID.randomUUID().toString();

        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(email);
        emailToken.setExpiry(LocalDateTime.now().plusMinutes(15));
        System.out.println(emailTokenRepository.save(emailToken));
        emailTokenRepository.save(emailToken);

        String link = "https://mindstack-ubuy.onrender.com/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Email");
        message.setText("Click here to verify your email.\n" + link);

        try {
            mailSender.send(message);
        }
        catch (Exception e) {
            throw new CustomMessageException("Mail server Error: Email verification failed");
        }

    }

    public void verifyEmail(String token) {

        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow( () -> new CustomMessageException("Invalid Email Token"));

        if(emailToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new CustomMessageException("Email Token Expired");
        }

        emailToken.setVerified(true);

    }
}
