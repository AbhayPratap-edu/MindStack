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

    @OneToMany(mappedBy = "noteBook",
            cascade = {CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Chapter> chapters;

}
