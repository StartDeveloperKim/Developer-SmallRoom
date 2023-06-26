package com.developer.smallRoom.dto.article.response;

import lombok.Getter;

@Getter
public class ArticleRetouchResponse<T> {

    private final String message;
    private T responseData;

    public ArticleRetouchResponse(String message, T responseData) {
        this.message = message;
        this.responseData = responseData;
    }

    public ArticleRetouchResponse(String message) {
        this.message = message;
    }
}
