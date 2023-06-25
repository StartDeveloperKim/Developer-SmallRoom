package com.developer.smallRoom.factory;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import lombok.Builder;

public class ArticleFactory {

    private String title = "title";
    private String subTitle = "subTitle";
    private String content  = "content";
    private String thumbnailUrl = "thumbnailUrl";
    private String githubLink = "githubLink";
    private final Member member;

    @Builder
    public ArticleFactory(String title, String subTitle, String content, String thumbnailUrl, String githubLink, Member member) {
        this.title = title != null ? title : this.title;
        this.subTitle = subTitle != null ? subTitle : this.subTitle;
        this.content = content != null ? content : this.content;
        this.thumbnailUrl = thumbnailUrl != null ? thumbnailUrl : this.thumbnailUrl;
        this.githubLink = githubLink != null ? githubLink : this.githubLink;
        this.member = member;
    }

    public static Article getDefaultValueArticle(Member member) {
        return ArticleFactory.builder().member(member).build().getMember();
    }

    public Article getMember() {
        return Article.builder()
                .title(this.title)
                .subTitle(this.subTitle)
                .content(this.content)
                .thumbnailUrl(this.thumbnailUrl)
                .githubLink(this.githubLink)
                .member(this.member)
                .build();
    }
}
