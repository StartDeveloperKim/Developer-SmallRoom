package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/article")
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public String articleViewById(@PathVariable(name = "id") Long id, Model model) {
        ArticleResponse article = articleService.getArticleById(id);
        model.addAttribute("article", article);

        return "articleDetail";
    }

    @GetMapping("/post")
    public String articlePostingView(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        return "articlePost";
    }

    @GetMapping("/post/{id}")
    public String articleUpdateView(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                    Model model) {
        validMember(memberPrincipal);
        ArticleResponse article = articleService.getArticleByIdAndMember(id, memberPrincipal.getUsername());
        model.addAttribute("article", article);
        return "articlePost";
    }

    private void validMember(MemberPrincipal memberPrincipal) {
        if (memberPrincipal == null) {
            throw new NotAuthorizationException("인증된 사용자가 아닙니다.");
        }
    }
}
