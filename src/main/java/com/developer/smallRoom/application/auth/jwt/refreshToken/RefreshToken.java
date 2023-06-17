package com.developer.smallRoom.application.auth.jwt.refreshToken;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(final String memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(final String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
