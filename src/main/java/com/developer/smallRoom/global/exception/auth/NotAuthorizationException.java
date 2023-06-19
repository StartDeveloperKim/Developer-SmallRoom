package com.developer.smallRoom.global.exception.auth;

public class NotAuthorizationException extends RuntimeException{

    public NotAuthorizationException(String message) {
        super(message);
    }
    public NotAuthorizationException() {
        super("인증된 사용자만 이용가능합니다.");
    }
}
