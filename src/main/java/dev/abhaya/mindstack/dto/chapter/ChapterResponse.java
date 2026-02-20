package dev.abhaya.mindstack.dto.chapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChapterResponse {

    private String chapterName;
    private Integer chapterNumber;
    private String content;

}
