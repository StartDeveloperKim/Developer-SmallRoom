package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;

import java.util.List;

public interface ArticleService {

    Long saveArticle(Long memberId, ArticleRequest request);

    Long updateArticle(ArticleUpdateRequest request, Long memberId);

    void deleteArticle(Long articleId, Long memberId);

    ArticleResponse getArticleById(Long articleId);

    ArticleResponse getArticleByIdAndMember(Long articleId, Long memberId);

    List<HomeArticleResponse> getHomeArticleResponses(int page, String standard);

    List<HomeArticleResponse> searchArticlesByTags(int page, List<String> tags);

    List<HomeArticleResponse> getRandomArticles();
}
