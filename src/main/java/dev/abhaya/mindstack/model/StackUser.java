package dev.abhaya.mindstack.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StackUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserID;

    @Column(unique = true,nullable = false, length = 255)
    private String email;

    @Column()
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "stackUser",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<NoteBook> noteBooks;

}
