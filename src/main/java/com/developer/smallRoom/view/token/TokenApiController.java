package com.developer.smallRoom.view.token;

import com.developer.smallRoom.application.auth.jwt.TokenService;
import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.ACCESS_TOKEN;
import static com.developer.smallRoom.application.auth.jwt.TokenInfo.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@Controller
@RequestMapping("/api/token")
public class TokenApiController {

    private final TokenService tokenService;

    @Value("${redirect.uri}")
    private String REDIRECT_URI;

    @Value("${redirect.logout.uri}")
    private String LOGOUT_URI;

    @GetMapping
    public String generateNewAccessToken(HttpServletRequest request,
                                         HttpServletResponse response) {
        try {
            String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN.getCookieName());
            String newAccessToken = tokenService.createNewAccessToken(refreshToken);

            int cookieMaxAge = (int) ACCESS_TOKEN.getExpireDuration().toSeconds();
            CookieUtil.addCookie(response, ACCESS_TOKEN.getCookieName(), newAccessToken, cookieMaxAge);

            return "redirect:" + REDIRECT_URI;
        } catch (IllegalArgumentException e) {
            return "redirect:" + LOGOUT_URI;
        }

    }
}
