package dev.abhaya.mindstack.dto.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddChapterRequest {

    @NotNull
    private Integer chapterNumber;
    @NotBlank
    private String chapterName;
    private String description;

}
