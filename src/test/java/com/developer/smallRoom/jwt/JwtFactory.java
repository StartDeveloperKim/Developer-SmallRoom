package com.developer.smallRoom.jwt;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.domain.member.Member;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
public class JwtFactory {

    private String subject = "test@email.com";

    private Date issuedAt = new Date();

    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());

    private Map<String, Object> claims = emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration,
                      Member member) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = member != null ? makeClaims(member) : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    public String createToken(TestJwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    private Map<String, Object> makeClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("role", member.getRole().getKey());
        return claims;
    }
}
