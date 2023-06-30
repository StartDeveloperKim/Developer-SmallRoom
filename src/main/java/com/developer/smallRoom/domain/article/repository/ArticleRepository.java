package com.developer.smallRoom.domain.article.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.id=:articleId and a.member.id=:memberId")
    Optional<Article> findByIdAndMemberId(@Param("articleId") Long articleId, @Param("memberId") Long memberId);

    Page<Article> findArticlesBy(Pageable pageable);

    @Query("select distinct a from Article a " +
            "inner join a.boardTags bt " +
            "inner join bt.tag t where t.name in :tags")
    List<Article> findArticlesByTags(@Param("tags") List<String> tags, Pageable pageable);

    boolean existsByIdAndMember(Long id, Member member);

    @Query(value = "SELECT * FROM ARTICLE ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Article> findArticlesRandom();
}
