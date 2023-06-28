package com.developer.smallRoom.application.auth.jwt;

import lombok.Getter;

import java.time.Duration;

@Getter
public enum TokenInfo {
    ACCESS_TOKEN("access_token", Duration.ofHours(2)),
    REFRESH_TOKEN("refresh_token", Duration.ofDays(14));

    private final String cookieName;
    private final Duration expireDuration;

    TokenInfo(String cookieName, Duration expireDuration) {
        this.cookieName = cookieName;
        this.expireDuration = expireDuration;
    }
}
