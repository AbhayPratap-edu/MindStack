package dev.abhaya.mindstack.dto.notebook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoteBookResponse {
    Long noteBookId;
    private String bookName;
}
