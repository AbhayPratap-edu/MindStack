package dev.abhaya.mindstack.dto.chapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddChapterRequest {

    private Integer chapterNumber;
    private String chapterName;
    private String description;

}
