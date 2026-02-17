package dev.abhaya.mindstack.repository;

import dev.abhaya.mindstack.model.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteBookRepository extends JpaRepository<NoteBook, Long> {

}
