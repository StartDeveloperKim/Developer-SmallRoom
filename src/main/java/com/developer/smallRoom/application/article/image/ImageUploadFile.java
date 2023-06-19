package com.developer.smallRoom.application.article.image;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ImageUploadFile {

    public String getUploadImageName(String originalName, String memberGitHubId) {
        String ext = extractExt(originalName);
        return LocalDateTime.now().toString() + memberGitHubId + "." + ext;
    }

    private static String extractExt(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos + 1);
    }
}
