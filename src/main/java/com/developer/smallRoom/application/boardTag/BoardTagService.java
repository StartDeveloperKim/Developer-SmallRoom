package com.developer.smallRoom.application.boardTag;

import java.util.List;

public interface BoardTagService {

    void saveBoardTag(Long articleId, List<String> tags);

    List<String> findBoardTagByArticleId(Long articleId);
}
