package com.developer.smallRoom.view;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.like.ArticleLikeService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final ArticleService articleService;
    private final ArticleLikeService articleLikeService;

    @GetMapping("/")
    public String index(@LoginMember MemberPrincipal memberPrincipal, Model model) {
        log.info("memberInfo : {}", memberPrincipal!=null ? memberPrincipal.toString() : "not memberPrincipal");
        // TODO :: 첫 Index 페이지는 0으로 고정 글의 개수가 늘어나면 8개씩 로딩으로 수정
        List<HomeArticleResponse> articles = articleService.getRandomArticles();
        model.addAttribute("articles", articles);

        return "index";
    }
}
