package dev.abhaya.mindstack.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StackUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserID;

    @Column(unique = true,nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "stackUser",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<NoteBook> noteBooks;

}
