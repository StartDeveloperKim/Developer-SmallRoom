package com.developer.smallRoom.domain.article;

import com.developer.smallRoom.domain.boardTag.BoardTag;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ARTICLE")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "sub_title", nullable = false, length = 100)
    private String subTitle;

    @Column(name = "content", nullable = false, length = 20000)
    private String content;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "github_link", nullable = false)
    private String githubLink;

    @Column(name = "hit", nullable = false)
    private int hit;

    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "tags")
    private String tags;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BoardTag> boardTags = new ArrayList<>();

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    @Builder
    public Article(String title, String subTitle, String content, String thumbnailUrl, String githubLink, String tags, Member member) {
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.thumbnailUrl = thumbnailUrl.isEmpty() ? "https://yozm.wishket.com/media/news/1674/image001.png" : thumbnailUrl;
        this.githubLink = githubLink;
        this.tags = tags;
        setMember(member);
        this.hit = 0;
        this.likeCount = 0;
    }

    private void setMember(Member member) {
        this.member = member;
        member.getArticles().add(this);
    }

    public void update(ArticleUpdateRequest request) {
        this.title = request.getTitle();
        this.subTitle = request.getSubTitle();
        this.githubLink = request.getGitHubLink();
        this.content = request.getContent();
        this.thumbnailUrl = request.getThumbnailUrl();
        this.tags = request.tagsListToString();
    }

    public void increaseHit() {
        this.hit++;
    }

    public void addArticleLike(ArticleLike articleLike) {
        this.articleLikes.add(articleLike);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void updateLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<String> getTagsStringToList() {
        return this.tags == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(tags.split(",")));
    }
}
