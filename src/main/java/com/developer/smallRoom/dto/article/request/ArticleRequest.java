package com.developer.smallRoom.dto.article.request;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.StringJoiner;

@ToString
@NoArgsConstructor
@Getter
public class ArticleRequest {

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

    @NotNull(message = "하나 이상의 태그를 등록해주세요.")
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
                .tags(tagsListToString())
                .member(member)
                .build();
    }

    private String tagsListToString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String tag : tags) {
            stringJoiner.add(tag);
        }
        return stringJoiner.toString();
    }
}
