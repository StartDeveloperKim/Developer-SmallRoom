package com.developer.smallRoom.dto.article.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageRequest {
    private MultipartFile image;
}

