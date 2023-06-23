package com.developer.smallRoom.dto.article.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleUpdateRequest {

    private Long articleId;
    private String title;
    private String subTitle;
    private String gitHubLink;
    private String content;
    private String thumbnailUrl;
    private List<String> tags;

}
