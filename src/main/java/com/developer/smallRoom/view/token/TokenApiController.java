package com.developer.smallRoom.view.token;

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

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/token")
public class TokenApiController {

    private final TokenService tokenService;

    @GetMapping
    public String generateNewAccessToken(HttpServletRequest request,
                                         HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookie(request, "refresh_token");
        String newAccessToken = tokenService.createNewAccessToken(refreshToken);
        log.info("/api/token : {}", newAccessToken);

        int cookieMaxAge = (int) Duration.ofHours(2).toSeconds();
        // TODO :: 엑세스토큰 쿠키 이름 관련해서 리팩토링이 필요하다. 다양한 클래스에서 사용되어서 중복발생
        CookieUtil.addCookie(response, "access_token", newAccessToken, cookieMaxAge);

        return "redirect:/";
    }
}
