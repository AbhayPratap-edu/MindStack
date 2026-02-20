package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.notebook.NoteBookRequest;
import dev.abhaya.mindstack.dto.notebook.NoteBookResponse;
import dev.abhaya.mindstack.service.NoteBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<NoteBookResponse> getNoteBooks(){
        return noteBookService.getNoteBooks();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<NoteBookResponse> getNoteBookById(@PathVariable Long Id){
        return new ResponseEntity<>(noteBookService.getNoteBook(Id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteBookResponse> createNoteBook(@RequestBody NoteBookRequest noteBookRequest){
        return ResponseEntity.ok(noteBookService.createNoteBook(noteBookRequest));
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deleteNoteBook(@PathVariable Long Id){
        noteBookService.deleteNoteBook(Id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<NoteBookResponse> updateNoteBook(@PathVariable Long Id,
                                                   @RequestBody NoteBookRequest noteBookRequest){
        return new ResponseEntity<>(noteBookService.updateNoteBook(Id,noteBookRequest), HttpStatus.OK);
    }

}
