package com.developer.smallRoom.view.member;

import com.developer.smallRoom.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String ACCESS_TOKEN = "access_token";
        String REFRESH_TOKEN = "refresh_token";
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/";
    }
}
