package com.developer.smallRoom.application.auth.jwt.refreshToken;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(final Long memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(final String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
