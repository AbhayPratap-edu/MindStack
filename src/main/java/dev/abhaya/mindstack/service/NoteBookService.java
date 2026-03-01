package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.dto.notebook.NoteBookRequest;
import dev.abhaya.mindstack.dto.notebook.NoteBookResponse;
import dev.abhaya.mindstack.model.NoteBook;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.NoteBookRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class NoteBookService  {

    private final NoteBookRepository noteBookRepository;

    public NoteBookService(NoteBookRepository noteBookRepository) {
        this.noteBookRepository = noteBookRepository;
    }

    @PreAuthorize("hasAuthority('NOTEBOOK_CREATE')")
    public NoteBookResponse createNoteBook(NoteBookRequest noteBookRequest, Authentication authentication) {

        StackUser stackUser = (StackUser) authentication.getPrincipal();
        NoteBook noteBook = new NoteBook();
        noteBook.setBookName(noteBookRequest.getBookName());
        noteBook.setStackUser(stackUser);
        NoteBook savedNoteBook = noteBookRepository.save(noteBook);
        return new NoteBookResponse(savedNoteBook.getId(),
                savedNoteBook.getBookName()
        );
    }

    @PreAuthorize("hasAuthority('NOTEBOOK_VIEW') and @noteBookSecurity.isOwner(#id, authentication)")
    public NoteBookResponse getNoteBook(Long id){
        NoteBook noteBook = noteBookRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Note Book Not Found") );

        return new NoteBookResponse(noteBook.getId(),
                noteBook.getBookName()
        );
    }

    @PreAuthorize("hasAuthority('NOTEBOOK_VIEW')")
    public List<NoteBookResponse> getNoteBooks(Authentication authentication) {
        StackUser stackUser = (StackUser) authentication.getPrincipal();
        List<NoteBook> noteBookList = noteBookRepository.findAllByStackUser_UserId(stackUser.getUserId());
        List<NoteBookResponse> noteBookResponses = new ArrayList<>();
        noteBookList.forEach(noteBook -> noteBookResponses
                .add(new NoteBookResponse(noteBook.getId(),
                        noteBook.getBookName())));
        return noteBookResponses;
    }

    @PreAuthorize("hasAuthority('NOTEBOOK_DELETE') and @noteBookSecurity.isOwner(#id, authentication)")
    public void deleteNoteBook(Long id) throws IllegalArgumentException{
        noteBookRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('NOTEBOOK_UPDATE') and @noteBookSecurity.isOwner(#id, authentication)")
    public NoteBookResponse updateNoteBook(Long Id,NoteBookRequest noteBookRequest) {
        NoteBook noteBook = noteBookRepository.findById(Id)
                .orElseThrow( () -> new RuntimeException("Note Book Not Found") );
        noteBook.setBookName(noteBookRequest.getBookName());
        noteBookRepository.save(noteBook);
        return new NoteBookResponse(noteBook.getId(),noteBook.getBookName());
    }


}
