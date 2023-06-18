package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleRetouchResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    /*
    * TODO :: ControllerAdvice를 통해 다른사용자가 다른 사용자의 작성글에 접근하고자하면 이를 예외처리하여 막아야한다.
    * */

    @PostMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> postArticle(@RequestBody ArticleRequest articleRequest,
                                                              @LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        Long savedArticleId = articleService.saveArticle(memberPrincipal.getUsername(), articleRequest);
        return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 등록되었습니다.", savedArticleId));
    }

    @PutMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> updateArticle(@RequestBody ArticleUpdateRequest request,
                                                                      @LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        try {
            Long articleId = articleService.updateArticle(request, memberPrincipal.getUsername());
            return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 수정되었습니다.", articleId));
        } catch (NotAuthorizationException e) {
            return ResponseEntity.badRequest().body(new ArticleRetouchResponse<>(e.getMessage()));
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleRetouchResponse<Void>> removeArticle(@PathVariable("id") Long id,
                                                @LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        try {
            articleService.deleteArticle(id, memberPrincipal.getUsername());
            return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 삭제되었습니다."));
        } catch (NotAuthorizationException e) {
            return ResponseEntity.badRequest().body(new ArticleRetouchResponse<>(e.getMessage()));
        }
    }

    private void validMember(MemberPrincipal memberPrincipal) {
        if (memberPrincipal == null) {
            throw new NotAuthorizationException("인증된 사용자가 아닙니다.");
        }
    }
}
