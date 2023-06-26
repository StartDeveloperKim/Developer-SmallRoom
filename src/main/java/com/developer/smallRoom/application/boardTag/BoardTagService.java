package com.developer.smallRoom.application.boardTag;

import com.developer.smallRoom.dto.article.response.HomeArticleResponse;

import java.util.List;

public interface BoardTagService {

    void saveBoardTag(Long articleId, List<String> tags);

    void updateBoardTag(Long articleId, List<String> tags);

    List<String> findBoardTagByArticleId(Long articleId);

    List<HomeArticleResponse> searchBoardByTag(List<String> tags, int page);
}
