package dev.abhaya.mindstack.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookName;

    @OneToMany(mappedBy = "noteBook",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Chapter> chapters;

    public NoteBook(String bookName) {
        this.bookName = bookName;
    }

}
