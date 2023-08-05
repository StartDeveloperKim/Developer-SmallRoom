package com.developer.smallRoom.application.auth.oauth;

import com.developer.smallRoom.application.auth.jwt.TokenInfo;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshToken;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.*;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${redirect.uri}")
    private String REDIRECT_PATH;

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        log.info("OAuth2Member : {}", oAuth2Member.getName());
        String refreshToken = tokenProvider.generateToken(oAuth2Member, REFRESH_TOKEN.getExpireDuration());
        saveRefreshToken(oAuth2Member.getMemberId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = tokenProvider.generateToken(oAuth2Member, ACCESS_TOKEN.getExpireDuration());
        addAccessTokenToCookie(request, response, accessToken);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_PATH);
    }

    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN.getExpireDuration().toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getCookieName());
        CookieUtil.addCookie(response, REFRESH_TOKEN.getCookieName(), refreshToken, cookieMaxAge);
    }

    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        int cookieMaxAge = (int) ACCESS_TOKEN.getExpireDuration().toSeconds();

        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN.getCookieName());
        CookieUtil.addCookie(response, ACCESS_TOKEN.getCookieName(), accessToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
