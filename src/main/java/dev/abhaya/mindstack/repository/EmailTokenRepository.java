package dev.abhaya.mindstack.repository;

import dev.abhaya.mindstack.model.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);

    Optional<EmailToken> findByEmail(String email);

    Optional<EmailToken> findByEmailAndVerifiedTrue(String email);

    void deleteByEmail(String email);
}