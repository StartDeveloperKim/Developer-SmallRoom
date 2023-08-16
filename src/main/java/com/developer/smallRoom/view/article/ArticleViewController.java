package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleQueryService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.like.ArticleLikeService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/article")
public class ArticleViewController {

    private final ArticleQueryService articleService;
    private final ArticleLikeService articleLikeService;

    @GetMapping("/{id}")
    public String articleViewById(@PathVariable(name = "id") Long id,
                                  @LoginMember MemberPrincipal memberPrincipal,
                                  Model model) {
        ArticleResponse article = articleService.getArticleById(id);
        article.setUpdatable(memberPrincipal);

        addModel("article", model, article);

        if (memberPrincipal != null) {
            boolean isLike = articleLikeService.existArticleLike(id, memberPrincipal.getMemberId());
            addModel("isLike", model, isLike);
        }

        return "articleDetail";
    }

    @GetMapping("/post")
    public String articlePostingView(@LoginMember MemberPrincipal memberPrincipal) {
        return "articlePost";
    }

    @GetMapping("/post/{id}")
    public String articleUpdateView(@PathVariable("id") Long id,
                                    @LoginMember MemberPrincipal memberPrincipal,
                                    Model model) {
        ArticleResponse article = articleService.getArticleByIdAndMember(id, memberPrincipal);
        article.setUpdatable(memberPrincipal);

        addModel("article", model, article);
        return "articlePost";
    }

    private <T> void addModel(String key, Model model, T object) {
        model.addAttribute(key, object);
    }
}
