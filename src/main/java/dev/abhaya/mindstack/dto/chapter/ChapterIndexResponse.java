package dev.abhaya.mindstack.dto.chapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChapterIndexResponse {

    private Long chapterId;
    private Integer chapterNumber;
    private String chapterName;
    private String description;

}
