package com.developer.smallRoom.global.exception.image;

public class NotHaveImageException extends RuntimeException {
    public NotHaveImageException() {
        super("저장하려는 이미지가 비어있습니다.");
    }
}
