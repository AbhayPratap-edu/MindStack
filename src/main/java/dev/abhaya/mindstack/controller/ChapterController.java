package dev.abhaya.mindstack.controller;


import dev.abhaya.mindstack.model.Chapter;
import dev.abhaya.mindstack.repository.ChapterRepository;
import dev.abhaya.mindstack.service.ChapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/notebooks/{notebookId}/chapters")
    public List<Chapter> findChaptersByNoteBook_Id(Long noteBookId) {
        return chapterService.findChaptersByBook_Id(noteBookId);
    }

    @PostMapping("/notebooks/{notebookId}/chapters")
    public ResponseEntity<Chapter> addChapter(@PathVariable Long notebookId,
                                              @RequestBody Chapter chapter) {
        Chapter savedChapter = chapterService.addChapter(notebookId,chapter);
        System.out.println(savedChapter);
        return new ResponseEntity<>(savedChapter, HttpStatus.CREATED);
    }
}
