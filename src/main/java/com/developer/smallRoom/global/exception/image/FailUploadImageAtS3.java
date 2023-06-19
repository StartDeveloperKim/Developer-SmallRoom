package com.developer.smallRoom.global.exception.image;

public class FailUploadImageAtS3 extends RuntimeException {
    public FailUploadImageAtS3() {
        super("S3 파일 업로드에 실패했습니다.");
    }
}
