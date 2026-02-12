package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.model.Chapter;
import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.repository.ChapterRepository;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final NoteBookRepository noteBookRepository;


    public ChapterService(ChapterRepository chapterRepository, NoteBookRepository noteBookRepository) {
        this.chapterRepository = chapterRepository;
        this.noteBookRepository = noteBookRepository;
    }


    public List<Chapter> findChaptersByBook_Id(Long noteBookId) {
        return chapterRepository.findByNoteBook_Id(noteBookId);
    }

    public Chapter addChapter(Long notebookId, Chapter chapter) {

        NoteBook noteBook = noteBookRepository.getNoteBookById(notebookId);
        chapter.setNoteBook(noteBook);
        return chapterRepository.save(chapter);

    }
}
