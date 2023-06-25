package com.developer.smallRoom.jwt;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.oauth.CustomOAuth2Member;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.Role;
import com.developer.smallRoom.factory.MemberFactory;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;

    private final TestJwtProperties testJwtProperties = new TestJwtProperties();

    @BeforeEach
    void setUp() {
        given(jwtProperties.getIssuer()).willReturn(testJwtProperties.getIssuer());
        given(jwtProperties.getSecretKey()).willReturn(testJwtProperties.getSecretKey());
    }

    @DisplayName("generateToken() : 유저 정보와 만료시간을 전달하여 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        //given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("login", "testId");
        attributes.put("name", "testName");
        attributes.put("avatar_url", "testImage");

        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(attributes);
        customOAuth2Member.setRole(Role.USER);
        //when
        String token = tokenProvider.generateToken(customOAuth2Member, Duration.ofDays(14));

        //then
        // TODO :: 실제 배포환경에서는 application-jwt 프로퍼티파일을 가져갈 수 없다. 그래서 아마 이 테스트는 실패할 것 같다. 그래서 테스트용 시크릿키를 설정해야한다.
        String name = Jwts.parser()
                .setSigningKey(testJwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("name", String.class);

        assertThat(name).isEqualTo(customOAuth2Member.getName());
    }

    @DisplayName("invalidToken() : 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void invalidToken() {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(testJwtProperties);
        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken() : 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken() {
        //given
        String token = JwtFactory
                .withDefaultValues()
                .createToken(testJwtProperties);
        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication() : 토큰 기반으로 인증정보를 가져온다.")
    @Test
    void getAuthentication() {
        //given
        String subject = "gitHubId";
        Member member = MemberFactory.getMemberDefaultValue();

        String accessToken = JwtFactory.builder()
                .subject("gitHubId")
                .member(member).build().createToken(testJwtProperties);
        //when
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        //then
        assertThat(((MemberPrincipal) authentication.getPrincipal()).getUsername()).isEqualTo(subject);
        assertThat(((MemberPrincipal) authentication.getPrincipal()).getRole()).isEqualTo(Role.USER.getKey());
    }
}
