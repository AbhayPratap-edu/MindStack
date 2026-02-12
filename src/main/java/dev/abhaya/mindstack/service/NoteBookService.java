package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteBookService {



    private final NoteBookRepository noteBookRepository;


    public NoteBookService(NoteBookRepository noteBookRepository) {
        this.noteBookRepository = noteBookRepository;
    }


    public NoteBook addNoteBook(NoteBook noteBook) {
        noteBookRepository.save(noteBook);
        return noteBook;
    }

    public List<NoteBook> getNoteBooks(){
        return noteBookRepository.findAll();
    }

    public NoteBook getNoteBookById(Long id) {
        return noteBookRepository.getNoteBookById(id);
    }

    public void deleteNoteBook(Long id) {
        noteBookRepository.deleteById(id);
    }

    public NoteBook updateNoteBook(NoteBook noteBook) {
        noteBookRepository.save(noteBook);
        return noteBook;
    }



}
