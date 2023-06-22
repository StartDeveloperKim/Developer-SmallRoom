package com.developer.smallRoom.domain.boardTag;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tag_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BoardTag(Article article, Tag tag) {
        setArticle(article);
        this.tag = tag;
    }

    private void setArticle(Article article) {
        this.article = article;
        article.getBoardTags().add(this);
    }

    public String getTagName() {
        return this.tag.getName();
    }
}
