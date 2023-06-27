package com.developer.smallRoom.dto.article.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.StringJoiner;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleUpdateRequest {

    @NotNull
    private Long articleId;

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    @Size(max=255, message = "제목은 255자 이내입니다.")
    private String title;

    @NotBlank(message = "부제목은 비어있을 수 없습니다.")
    @Size(max=100, message = "부제목은 100자 이내입니다.")
    private String subTitle;

    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    @Size(max=20000, message = "내용은 20000만 이내입니다.")
    private String content;

    private String gitHubLink;
    private String thumbnailUrl;
    private List<String> tags;

    public String tagsListToString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String tag : tags) {
            stringJoiner.add(tag);
        }
        return stringJoiner.toString();
    }

}
