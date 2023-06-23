package com.developer.smallRoom.domain.like;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsArticleLikeByArticleAndMember(Article article, Member member);

    Optional<ArticleLike> findByArticleAndMember(Article article, Member member);
}
