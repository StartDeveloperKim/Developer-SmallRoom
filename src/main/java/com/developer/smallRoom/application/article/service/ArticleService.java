package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleResponse;

public interface ArticleService {

    Long saveArticle(String memberId, ArticleRequest request);

    Long updateArticle(ArticleUpdateRequest request, String memberId);

    void deleteArticle(Long articleId, String memberId);

    ArticleResponse getArticleById(Long articleId);

    ArticleResponse getArticleByIdAndMember(Long articleId, String memberGitHubId);
}
