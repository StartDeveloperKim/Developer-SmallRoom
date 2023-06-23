package com.developer.smallRoom.dto.article.request;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
public class ArticleRequest {

    // TODO :: Spring Validation 어노테이션을 사용하자.
    // TODO :: 깃허브 링크와 태그배열을 받아오도록 DTO를 수정하자
    private String title;
    private String subTitle;
    private String content;
    private String gitHubLink;
    private String thumbnailUrl;
    private List<String> tags;

    public ArticleRequest(String title, String subTitle, String content, String gitHubLink, String thumbnailUrl, List<String> tags) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.gitHubLink = gitHubLink;
        this.thumbnailUrl = thumbnailUrl;
        this.tags = tags;
    }

    public Article toArticle(Member member) {
        return Article.builder()
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .githubLink(gitHubLink)
                .thumbnailUrl(thumbnailUrl)
                .member(member)
                .build();
    }
}
