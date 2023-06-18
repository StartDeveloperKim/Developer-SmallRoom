package com.developer.smallRoom.dto.article.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
