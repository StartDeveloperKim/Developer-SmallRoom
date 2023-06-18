package com.developer.smallRoom.dto.article.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleUpdateRequest {

    private Long articleId;
    private String title;
    private String content;
    private String thumbnailUrl;

}
