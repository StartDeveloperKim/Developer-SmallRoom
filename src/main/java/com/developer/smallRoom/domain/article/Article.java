package com.developer.smallRoom.domain.article;

import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "hit", nullable = false)
    private int hit;

    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Article(String title, String content, String thumbnailUrl, Member member) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.member = member;
        this.hit = 0;
    }

    public void update(ArticleUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.thumbnailUrl = request.getThumbnailUrl();
    }

    // TODO :: 어느정도 프론트엔드와 백엔드 모두가 구현되고 나면 Jmeter를 통해 성능을 측정해보고 최적화를 해보자
    public void increaseHit() {
        this.hit++;
    }


}
