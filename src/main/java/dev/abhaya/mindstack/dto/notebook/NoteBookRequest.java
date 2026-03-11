package dev.abhaya.mindstack.dto.notebook;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteBookRequest {

    @NotBlank
    private String bookName;
}
