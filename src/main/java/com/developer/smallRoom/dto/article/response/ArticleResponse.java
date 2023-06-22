package com.developer.smallRoom.dto.article.response;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.domain.article.Article;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArticleResponse {
    /*
     * TODO :: DTO에 태그 리스트도 포함하자.
     *  */
    private final Long articleId;
    private final String title;
    private final String content;
    private final String thumbnailUrl;
    private final String createAt;
    private final String memberId;
    private List<String> tags;
    private boolean updatable;

    public ArticleResponse(Article article) {
        this.articleId = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.thumbnailUrl = article.getThumbnailUrl();
        this.createAt = article.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"));
//        this.tags = article.getTags();
        this.memberId = article.getMember().getGitHubId();
    }

    public void setUpdatable(MemberPrincipal memberPrincipal) {
        this.updatable = memberPrincipal != null && this.memberId.equals(memberPrincipal.getUsername());
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
