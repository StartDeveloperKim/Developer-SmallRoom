package com.developer.smallRoom.dto.article.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResponse {
    private final String imageUrl;
    private final String message;
}
