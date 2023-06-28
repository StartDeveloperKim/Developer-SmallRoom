package com.developer.smallRoom.view.token;

import com.developer.smallRoom.application.auth.jwt.TokenInfo;
import com.developer.smallRoom.application.auth.jwt.TokenService;
import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/token")
public class TokenApiController {

    private final TokenService tokenService;

    @GetMapping
    public String generateNewAccessToken(HttpServletRequest request,
                                         HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN.getCookieName());
        String newAccessToken = tokenService.createNewAccessToken(refreshToken);

        int cookieMaxAge = (int) ACCESS_TOKEN.getExpireDuration().toSeconds();
        CookieUtil.addCookie(response, ACCESS_TOKEN.getCookieName(), newAccessToken, cookieMaxAge);

        return "redirect:/";
    }
}
