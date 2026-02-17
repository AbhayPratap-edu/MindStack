package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.dto.notebook.NoteBookRequest;
import dev.abhaya.mindstack.dto.notebook.NoteBookResponse;
import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteBookService  {



    private final NoteBookRepository noteBookRepository;


    public NoteBookService(NoteBookRepository noteBookRepository) {
        this.noteBookRepository = noteBookRepository;
    }

    public NoteBookResponse createNoteBook(NoteBookRequest noteBookRequest) {
        NoteBook noteBook = new NoteBook();
        noteBook.setBookName(noteBookRequest.getBookName());
        NoteBook savedNoteBook = noteBookRepository.save(noteBook);
        return new NoteBookResponse(
                savedNoteBook.getBookName()
        );
    }

    public NoteBookResponse getNoteBook(Long id){
        NoteBook noteBook = noteBookRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Note Book Not Found") );

        return new NoteBookResponse(
                noteBook.getBookName()
        );
    }

    public List<NoteBookResponse> getNoteBooks(){
        List<NoteBook> noteBooks = noteBookRepository.findAll();
        List<NoteBookResponse> noteBookResponses = new ArrayList<>();
        noteBooks.forEach(noteBook -> noteBookResponses
                .add(new NoteBookResponse(noteBook.getBookName())));
        return noteBookResponses;
    }

    public void deleteNoteBook(Long id) throws IllegalArgumentException{
        noteBookRepository.deleteById(id);
    }

    public NoteBookResponse updateNoteBook(Long Id,NoteBookRequest noteBookRequest) {
        NoteBook noteBook = noteBookRepository.findById(Id)
                .orElseThrow( () -> new RuntimeException("Note Book Not Found") );
        noteBook.setBookName(noteBookRequest.getBookName());
        noteBookRepository.save(noteBook);
        return new NoteBookResponse(noteBook.getBookName());
    }



}
