package com.developer.smallRoom.application.article.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.developer.smallRoom.global.exception.image.FailUploadImageAtS3;
import com.developer.smallRoom.global.exception.image.NotHaveImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3ImageStore implements ImageStore {

    private final ImageUploadFile imageUploadFile;
    private final S3Properties s3Properties;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String saveImage(MultipartFile image, String memberGitHubId) throws FailUploadImageAtS3, NotHaveImageException, IOException {
        if (image.isEmpty()) {
            throw new NotHaveImageException();
        }
        String uploadImageName = imageUploadFile.getUploadImageName(image.getOriginalFilename(), memberGitHubId);
        return uploadImage(image, uploadImageName);
    }

    private String uploadImage(MultipartFile image, String uploadImageName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(s3Properties.getBucket(), uploadImageName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FailUploadImageAtS3();
        }
        return amazonS3Client.getUrl(s3Properties.getBucket(), uploadImageName).toString();
    }
}
