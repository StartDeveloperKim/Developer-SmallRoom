package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;

import java.util.List;

public interface ArticleQueryService {

    ArticleResponse getArticleById(Long articleId);

    ArticleResponse getArticleByIdAndMember(Long articleId, MemberPrincipal memberPrincipal);

    List<HomeArticleResponse> getHomeArticleResponses(int page, String standard);

    List<HomeArticleResponse> searchArticlesByTags(int page, List<String> tags, String standard);

    List<HomeArticleResponse> getRandomArticles();
}
