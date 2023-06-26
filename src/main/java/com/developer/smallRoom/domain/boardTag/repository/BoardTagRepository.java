package com.developer.smallRoom.domain.boardTag.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.boardTag.BoardTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {

    @Query("select boardTag from BoardTag boardTag join fetch boardTag.tag where boardTag.article.id=:articleId")
    List<BoardTag> findBoardTagsByArticleId(@Param("articleId") Long articleId);

    @Query("select b from BoardTag b " +
            "join b.tag " +
            "join fetch b.article " +
            "where b.tag.name=:tag " +
            "order by b.article.createAt desc")
    List<BoardTag> findBoardTagByTagName(Pageable pageable, @Param("tag") String tag);

    void deleteByArticle(Article article);
}
