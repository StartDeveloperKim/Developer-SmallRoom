package com.developer.smallRoom.dto.article.request;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticleRequest {

    // TODO :: Spring Validation 어노테이션을 사용하자.
    private String title;
    private String content;
    private String thumbnailUrl;

    public ArticleRequest(String title, String content, String thumbnailUrl) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Article toArticle(Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .member(member)
                .build();
    }
}
