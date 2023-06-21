package com.developer.smallRoom.view;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.member.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String index(@LoginMember MemberPrincipal memberPrincipal,
                        Model model) {
        log.info("memberInfo : {}", memberPrincipal!=null ? memberPrincipal.toString() : "not memberPrincipal");

        return "index";
    }
}
