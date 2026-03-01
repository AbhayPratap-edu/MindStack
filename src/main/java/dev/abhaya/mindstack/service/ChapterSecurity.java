package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterSecurity {

    private final ChapterRepository chapterRepository;

    public boolean isOwner(Long chapterId, Authentication authentication) {

        StackUser stackUser = (StackUser) authentication.getPrincipal();
        return chapterRepository.existsByIdAndNoteBook_StackUser_UserId(chapterId, stackUser.getUserId());
    }
}
