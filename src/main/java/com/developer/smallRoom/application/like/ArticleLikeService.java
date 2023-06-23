package com.developer.smallRoom.application.like;

import com.developer.smallRoom.dto.like.response.ArticleLikeResponse;

public interface ArticleLikeService {

    boolean existArticleLike(Long articleId, Long memberId);
    ArticleLikeResponse saveArticleLike(Long articleId, Long memberId);

    ArticleLikeResponse removeArticleLike(Long articleId, Long memberId);
}
