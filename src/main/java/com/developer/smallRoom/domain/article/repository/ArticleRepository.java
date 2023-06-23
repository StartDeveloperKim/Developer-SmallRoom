package com.developer.smallRoom.domain.article.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.id=:articleId and a.member.id=:memberId")
    Optional<Article> findByIdAndMemberId(@Param("articleId") Long articleId, @Param("memberId") Long memberId);

    boolean existsByIdAndMember(Long id, Member member);
}
