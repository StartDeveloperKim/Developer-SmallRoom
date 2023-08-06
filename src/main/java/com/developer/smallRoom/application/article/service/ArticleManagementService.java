package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;

public interface ArticleManagementService {

    Long saveArticle(Long memberId, ArticleRequest request);

    Long updateArticle(ArticleUpdateRequest request, Long memberId);

    void deleteArticle(Long articleId, Long memberId);
}
