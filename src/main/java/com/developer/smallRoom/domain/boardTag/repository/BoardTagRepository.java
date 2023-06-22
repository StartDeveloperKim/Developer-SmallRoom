package com.developer.smallRoom.domain.boardTag.repository;

import com.developer.smallRoom.domain.boardTag.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {

    @Query("select boardTag from BoardTag boardTag join fetch boardTag.tag where boardTag.article.id=:articleId")
    List<BoardTag> findBoardTagsByArticleId(@Param("articleId") Long articleId);
}
