package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.dto.chapter.*;
import dev.abhaya.mindstack.model.Chapter;
import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.repository.ChapterRepository;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasAuthority('NOTEBOOK_VIEW') and @noteBookSecurity.isOwner(#noteBookId, authentication)")
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

    @PreAuthorize("hasAuthority('CHAPTER_CREATE') and @noteBookSecurity.isOwner(#notebookId, authentication)")
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

    @PreAuthorize("hasAuthority('CHAPTER_VIEW') and @chapterSecurity.isOwner(#chapterId, authentication)")
    public ChapterResponse getChapter(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).
                orElseThrow( () -> new EntityNotFoundException("Chapter Not Found") );
        return new ChapterResponse(chapter.getChapterName(),
                chapter.getChapterNumber(),
                chapter.getContent()
        );
    }

    @PreAuthorize("hasAuthority('CHAPTER_UPDATE') and @chapterSecurity.isOwner(#chapterId, authentication)")
    public void updateContent(Long chapterId, UpdateContentRequest updateContentRequest) {

        Chapter chapter = chapterRepository.findById(chapterId).
                orElseThrow( () -> new EntityNotFoundException("Chapter Not Found") );
        chapter.setContent(updateContentRequest.getContent());
        chapterRepository.save(chapter);
    }


    @PreAuthorize("hasAuthority('CHAPTER_DELETE') and @chapterSecurity.isOwner(#chapterId, authentication)")
    public void deleteChapter(Long chapterId) {
        chapterRepository.deleteById(chapterId);
    }
}
