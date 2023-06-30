package com.developer.smallRoom.domain.like;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsArticleLikeByArticleAndMember(Article article, Member member);

    Optional<ArticleLike> findByArticleAndMember(Article article, Member member);

    @Query("select count(al) from ArticleLike al where al.article.id=:articleId")
    int countByArticleId(@Param("articleId") Long articleId);
}
