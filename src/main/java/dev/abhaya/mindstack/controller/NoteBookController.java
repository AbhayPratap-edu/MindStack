package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.model.NoteBook;
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
    public List<NoteBook> getNoteBooks(){
        return noteBookService.getNoteBooks();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<NoteBook> getNoteBookById(@PathVariable Long Id){
        return new ResponseEntity<>(noteBookService.getNoteBookById(Id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteBook> addNoteBook(@RequestBody NoteBook noteBook){
        NoteBook savedNoteBook = noteBookService.addNoteBook(noteBook);
        return new ResponseEntity<>(savedNoteBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deleteNoteBook(@PathVariable Long Id){
        noteBookService.deleteNoteBook(Id);
        return ResponseEntity.ok().body("Note Book has been deleted id: " + Id);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<NoteBook> updateNoteBook(@PathVariable Long Id,
                                                   @RequestBody NoteBook noteBook){
        noteBook.setId(Id);
        return new ResponseEntity<>(noteBookService.updateNoteBook(noteBook), HttpStatus.OK);
    }


}
