package com.developer.smallRoom.global.exception.auth;

public class NotAuthorizationException extends RuntimeException{

    public NotAuthorizationException(String message) {
        super(message);
    }
}
