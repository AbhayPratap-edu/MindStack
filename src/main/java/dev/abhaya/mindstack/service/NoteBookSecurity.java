package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteBookSecurity {

    private final NoteBookRepository noteBookRepository;

    public boolean isOwner(Long noteBookId, Authentication authentication) {

        StackUser stackUser = (StackUser) authentication.getPrincipal();
        return noteBookRepository.existsByIdAndStackUser_UserId(noteBookId, stackUser.getUserId());
    }
}
