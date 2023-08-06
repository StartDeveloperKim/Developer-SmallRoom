package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;

public interface ArticleManagementService {

    Long saveArticle(MemberPrincipal memberPrincipal, ArticleRequest request);

    Long updateArticle(ArticleUpdateRequest request, MemberPrincipal memberPrincipal);

    void deleteArticle(Long articleId, MemberPrincipal memberPrincipal);
}
