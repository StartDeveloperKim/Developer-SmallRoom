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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
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

    // TODO :: 좋아요 수, 댓글 수 통계컬럼을 두고 성능을 최적화하자.
//    @Column(name = "like_count", nullable = false)
//    private int likeCount;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    // TODO :: 댓글관련 코드 삭제 필요 - 깃허브 이슈 기능 사용함

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
//        this.likeCount = 0;
//        this.commentCount = 0;
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

    // TODO :: 어느정도 프론트엔드와 백엔드 모두가 구현되고 나면 Jmeter를 통해 성능을 측정해보고 최적화를 해보자
    public void increaseHit() {
        this.hit++;
    }

    public void addArticleLike(ArticleLike articleLike) {
        this.articleLikes.add(articleLike);
    }

//    public void addLikeCount() {
//        this.likeCount++;
//    }

    public List<String> getTags() {
        return this.boardTags.stream().map(BoardTag::getTagName)
                .collect(Collectors.toList());
    }

    public String getTagsString() {
        return this.tags;
    }

    public boolean isMemberArticle(Member member) {
        return Objects.equals(this.member.getId(), member.getId());
    }
}
