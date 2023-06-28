package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.boardTag.BoardTagService;
import com.developer.smallRoom.application.like.ArticleLikeService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/article")
public class ArticleViewController {

    private final ArticleService articleService;
    private final BoardTagService boardTagService;
    private final ArticleLikeService articleLikeService;

    @GetMapping("/{id}")
    public String articleViewById(@PathVariable(name = "id") Long id,
                                  @LoginMember MemberPrincipal memberPrincipal,
                                  Model model) {
        ArticleResponse article = articleService.getArticleById(id);

        article.setUpdatable(memberPrincipal);

//        List<String> tags = boardTagService.findBoardTagByArticleId(id);
//        article.setTags(tags);

        model.addAttribute("article", article);

        if (memberPrincipal != null) {
            boolean isLike = articleLikeService.existArticleLike(id, memberPrincipal.getMemberId());
            model.addAttribute("isLike", isLike);
        }

        return "articleDetail";
    }

    @GetMapping("/post")
    public String articlePostingView(@LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        return "articlePost";
    }

    @GetMapping("/post/{id}")
    public String articleUpdateView(@PathVariable("id") Long id,
                                    @LoginMember MemberPrincipal memberPrincipal,
                                    Model model) {
        validMember(memberPrincipal);
        ArticleResponse article = articleService.getArticleByIdAndMember(id, memberPrincipal.getMemberId());
        article.setUpdatable(memberPrincipal);

        model.addAttribute("article", article);
        return "articlePost";
    }

    private void validMember(MemberPrincipal memberPrincipal) {
        if (memberPrincipal == null) {
            throw new NotAuthorizationException("인증된 사용자가 아닙니다.");
        }
    }
}
