package dev.abhaya.mindstack.controller;


import dev.abhaya.mindstack.dto.chapter.*;
import dev.abhaya.mindstack.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/notebooks/{notebookId}/chapters")
    public List<ChapterIndexResponse> findChaptersByNoteBookId(@PathVariable Long notebookId) {
        return chapterService.findChaptersByBookId(notebookId);
    }


    @PostMapping("/notebooks/{notebookId}/chapters")
    public ResponseEntity<ChapterIndexResponse> addChapter(@PathVariable Long notebookId,
                                                           @Valid @RequestBody AddChapterRequest addChapterRequest){
        return ResponseEntity.ok().body(chapterService.addChapter(notebookId, addChapterRequest));
    }

    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<ChapterResponse> getChapter(@PathVariable Long chapterId){
        return ResponseEntity.ok().body(chapterService.getChapter(chapterId));
    }

    @PatchMapping("/chapters/{chapterId}")
    public ResponseEntity<String> updateContent(@PathVariable Long chapterId,
                                                               @RequestBody UpdateContentRequest updateContentRequest){
        chapterService.updateContent(chapterId,updateContentRequest);
        return ResponseEntity.ok().body("Content updated");

    }

    @DeleteMapping ("/chapters/{chapterId}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long chapterId){
        chapterService.deleteChapter(chapterId);
        return ResponseEntity.noContent().build();
    }

}
