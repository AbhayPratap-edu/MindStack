package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.dto.chapter.*;
import dev.abhaya.mindstack.model.Chapter;
import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.repository.ChapterRepository;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final NoteBookRepository noteBookRepository;

    public ChapterService(ChapterRepository chapterRepository, NoteBookRepository noteBookRepository) {
        this.chapterRepository = chapterRepository;
        this.noteBookRepository = noteBookRepository;
    }


    public List<ChapterIndexResponse> findChaptersByBookId(Long noteBookId) {
        List<Chapter> chapters = chapterRepository.findAllByNoteBook_Id(noteBookId);
        return chapters.stream()
                .map(chapter -> new ChapterIndexResponse(
                        chapter.getId(),
                        chapter.getChapterNumber(),
                        chapter.getChapterName(),
                        chapter.getDescription()
                ))
                .toList();
    }


    public ChapterIndexResponse addChapter(Long notebookId, AddChapterRequest addChapterRequest) {

        Chapter newChapter = new Chapter();
        newChapter.setChapterName(addChapterRequest.getChapterName());
        newChapter.setChapterNumber(addChapterRequest.getChapterNumber());
        newChapter.setDescription(addChapterRequest.getDescription());

        NoteBook noteBook = noteBookRepository.findById(notebookId).
                orElseThrow(() -> new EntityNotFoundException("Notebook not found"));

        newChapter.setNoteBook(noteBook);
        chapterRepository.save(newChapter);

        return new ChapterIndexResponse(newChapter.getId(),
                newChapter.getChapterNumber(),
                newChapter.getChapterName(),
                newChapter.getDescription()
        );

    }

    public ChapterResponse getChapter(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).
                orElseThrow( () -> new EntityNotFoundException("Chapter Not Found") );
        return new ChapterResponse(chapter.getChapterName(),
                chapter.getChapterNumber(),
                chapter.getContent()
        );
    }

    public UpdateContentResponse updateContent(Long chapterId, UpdateContentRequest updateContentRequest) {

        Chapter chapter = chapterRepository.findById(chapterId).
                orElseThrow( () -> new EntityNotFoundException("Chapter Not Found") );
        chapter.setContent(updateContentRequest.getContent());
        chapterRepository.save(chapter);
        return new UpdateContentResponse(chapter.getContent());
    }


    public void deleteChapter(Long chapterId) {
        chapterRepository.deleteById(chapterId);
    }
}
