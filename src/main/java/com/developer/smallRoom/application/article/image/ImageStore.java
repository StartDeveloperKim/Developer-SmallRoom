package com.developer.smallRoom.application.article.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStore {

    String saveImage(MultipartFile image, String memberGitHubId) throws IOException;
}
