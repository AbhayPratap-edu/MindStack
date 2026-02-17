//package dev.abhaya.mindstack.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Chapter {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "notebook_id")
//    @JsonBackReference
//    private NoteBook noteBook;
//
//    private Integer chapterNumber;
//    private String chapterName;
//    private String description;
//    private String content;
//
//}
