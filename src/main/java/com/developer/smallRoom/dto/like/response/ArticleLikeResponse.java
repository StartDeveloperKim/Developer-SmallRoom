package com.developer.smallRoom.dto.like.response;

import lombok.Getter;

@Getter
public class ArticleLikeResponse {
    private final boolean result;


    public ArticleLikeResponse(boolean result) {
        this.result = result;
    }
}
