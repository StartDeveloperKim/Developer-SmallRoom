package com.developer.smallRoom.application.auth.jwt;

import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshToken;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.global.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.*;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${redirect.logout.uri}")
    private String LOGOUT_URL;

    @Value("${redirect.token.uri}")
    private String TOKEN_URL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getCookie(request, ACCESS_TOKEN.getCookieName());
        String refreshToken = getCookie(request, REFRESH_TOKEN.getCookieName());

        log.info("tokenAuthenticationFilter start : {}", request.getRequestURI());

        try {
            if (accessToken.length() != 0 && tokenProvider.validToken(accessToken)) {
                log.info("token : {}", accessToken);
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (accessToken.isEmpty() && !refreshToken.isEmpty()) {
                checkRefreshToken(request, response);
                return;
            }
        } catch (ExpiredJwtException e) {
            checkRefreshToken(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void checkRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = getCookie(request, REFRESH_TOKEN.getCookieName());
        log.info("Expire AccessToken");
        if (!refreshToken.isEmpty()) {
            Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (refreshTokenEntity.isPresent() && tokenProvider.validToken(refreshToken)) {
                log.info("valid refreshToken / redirect /api/token");
                response.sendRedirect(TOKEN_URL);
            } else {
                response.sendRedirect(LOGOUT_URL);
            }
        }else{
            response.sendRedirect(LOGOUT_URL);
        }
    }

    private String getCookie(HttpServletRequest request, String cookieName) {
        return CookieUtil.getCookie(request, cookieName);
    }
}
