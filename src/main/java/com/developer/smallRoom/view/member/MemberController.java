package com.developer.smallRoom.view.member;

import com.developer.smallRoom.application.auth.jwt.TokenInfo;
import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.developer.smallRoom.application.auth.jwt.TokenInfo.*;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN.getCookieName());
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getCookieName());

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/";
    }
}
