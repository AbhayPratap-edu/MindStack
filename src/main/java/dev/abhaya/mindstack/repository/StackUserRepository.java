package dev.abhaya.mindstack.repository;

import dev.abhaya.mindstack.model.StackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StackUserRepository extends JpaRepository<StackUser, Long> {
    Optional<StackUser> findByEmail(String email);

    String email(String email);

}