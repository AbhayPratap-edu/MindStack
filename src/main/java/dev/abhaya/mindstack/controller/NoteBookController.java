package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.notebook.NoteBookRequest;
import dev.abhaya.mindstack.dto.notebook.NoteBookResponse;
import dev.abhaya.mindstack.service.NoteBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notebooks")
public class NoteBookController {

    private final NoteBookService noteBookService;

    public NoteBookController(NoteBookService noteBookService) {
        this.noteBookService = noteBookService;
    }

    @GetMapping
    public List<NoteBookResponse> getNoteBooks(Authentication authentication) {
        return noteBookService.getNoteBooks(authentication);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<NoteBookResponse> getNoteBookById(@PathVariable Long Id){
        return new ResponseEntity<>(noteBookService.getNoteBook(Id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteBookResponse> createNoteBook(@Valid @RequestBody NoteBookRequest noteBookRequest,
                                                           Authentication authentication){
        return ResponseEntity.ok(noteBookService.createNoteBook(noteBookRequest,authentication));
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deleteNoteBook(@PathVariable Long Id){
        noteBookService.deleteNoteBook(Id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<NoteBookResponse> updateNoteBook(@PathVariable Long Id,
                                                   @Valid @RequestBody NoteBookRequest noteBookRequest){
        return new ResponseEntity<>(noteBookService.updateNoteBook(Id,noteBookRequest), HttpStatus.OK);
    }

}
