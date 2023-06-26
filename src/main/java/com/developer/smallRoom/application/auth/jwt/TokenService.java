package com.developer.smallRoom.application.auth.jwt;

import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshToken;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public String createNewAccessToken(String refreshToken) {
        if (refreshToken.isEmpty() || !tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 못한 토큰입니다.");
        }

        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 못한 토큰입니다."));
        Member member = memberRepository.findById(findRefreshToken.getMemberId()).
                orElseThrow(() -> new IllegalArgumentException("유효하지 않은 멤버ID입니다."));

        // TODO :: 사용자 경험 테스트
//        return tokenProvider.generateToken(member, Duration.ofHours(2));
        return tokenProvider.generateToken(member, Duration.ofMinutes(1));
    }
}
