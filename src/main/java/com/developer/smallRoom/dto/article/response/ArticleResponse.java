package com.developer.smallRoom.dto.article.response;

import com.developer.smallRoom.domain.article.Article;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ArticleResponse {

    private final Long articleId;
    private final String title;
    private final String content;
    private final String thumbnailUrl;
    private final String createAt;
    private final String memberId;

    public ArticleResponse(Article article) {
        this.articleId = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.thumbnailUrl = article.getThumbnailUrl();
        this.createAt = article.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"));
        this.memberId = article.getMember().getGitHubId();
    }
}
