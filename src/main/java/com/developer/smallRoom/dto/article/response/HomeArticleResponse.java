package com.developer.smallRoom.dto.article.response;

import com.developer.smallRoom.domain.article.Article;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class HomeArticleResponse {

    private final Long articleId;
    private final String title;
    private final String subTitle;
    private final String thumbnailUrl;
    private final String memberGithubId;
    private final String createAt;
    private final int hit;
    private int likeCount;

    public HomeArticleResponse(Article article) {
        this.articleId = article.getId();
        this.title = article.getTitle();
        this.subTitle = article.getSubTitle();
        this.thumbnailUrl = article.getThumbnailUrl();
        this.memberGithubId = article.getMember().getGitHubId();
        this.createAt = article.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"));
        this.hit = article.getHit();
        this.likeCount = 0;
    }
}
