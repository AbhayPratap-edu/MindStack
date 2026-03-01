package dev.abhaya.mindstack.repository;

import dev.abhaya.mindstack.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByNoteBook_Id(Long noteBookId);

    boolean existsByIdAndNoteBook_StackUser_UserId(Long id, Long userId);
}
