package com.developer.smallRoom.view.member;

import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.ACCESS_TOKEN;
import static com.developer.smallRoom.application.auth.jwt.TokenInfo.REFRESH_TOKEN;

@PropertySource("classpath:application.properties")
@Controller
public class MemberController {

    @Value("${redirect.uri}")
    private String REDIRECT_PATH;

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN.getCookieName());
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getCookieName());

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:" + REDIRECT_PATH;
    }
}
