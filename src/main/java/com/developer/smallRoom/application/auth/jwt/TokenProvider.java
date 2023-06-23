package com.developer.smallRoom.application.auth.jwt;

import com.developer.smallRoom.application.auth.oauth.CustomOAuth2Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(OAuth2User oAuth2User, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), (CustomOAuth2Member) oAuth2User);
    }

    private String makeToken(Date expiry, CustomOAuth2Member oAuth2Member) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(oAuth2Member.getGitHubId())
                .claim("id", oAuth2Member.getMemberId())
                .claim("name", oAuth2Member.getName())
                .claim("role", oAuth2Member.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Long memberId = getMemberId(claims);
        String gitHubId = getMemberGitHubId(claims);
        String role = getRole(claims);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(new MemberPrincipal(memberId, gitHubId, role, authorities), token, authorities);
    }

    private String getMemberGitHubId(Claims claims) {
        return claims.getSubject();
    }
    private String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    private Long getMemberId(Claims claims) {
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
