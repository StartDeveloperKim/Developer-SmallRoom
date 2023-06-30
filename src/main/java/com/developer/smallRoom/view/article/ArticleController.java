package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.boardTag.BoardTagService;
import com.developer.smallRoom.application.like.ArticleLikeService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleRetouchResponse;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final BoardTagService boardTagService;
    private final ArticleLikeService articleLikeService;

    /*
    * TODO :: ControllerAdvice를 통해 다른사용자가 다른 사용자의 작성글에 접근하고자하면 이를 예외처리하여 막아야한다.
    * */

    // TODO :: 무한스크롤로 데이터를 가져올 떄 standard(기준)을 쿼리스트링으로 받아와야 한다.

    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> getaHomeArticle(@RequestParam(value = "page", required = true) int page,
                                                               @RequestParam(value = "standard", required = false) String standard) {
        List<HomeArticleResponse> homeArticleResponses = articleService.getHomeArticleResponses(page, "createAt");
        for (HomeArticleResponse homeArticleResponse : homeArticleResponses) {
            int count = articleLikeService.countArticleLikeByArticleId(homeArticleResponse.getArticleId());
            homeArticleResponse.setLikeCount(count);
        }
        return ResponseEntity.ok().body(homeArticleResponses);
    }

    @PostMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> postArticle(@Validated @RequestBody ArticleRequest articleRequest,
                                                              @LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        log.info("ArticleRequest : {} / {}", articleRequest.getTitle(), articleRequest.getTags());
        Long savedArticleId = articleService.saveArticle(memberPrincipal.getMemberId(), articleRequest);
        boardTagService.saveBoardTag(savedArticleId, articleRequest.getTags());

        return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 등록되었습니다.", savedArticleId));
    }

    @PutMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> updateArticle(@Validated @RequestBody ArticleUpdateRequest request,
                                                                      @LoginMember MemberPrincipal memberPrincipal) {
        validMember(memberPrincipal);
        try {
            Long articleId = articleService.updateArticle(request, memberPrincipal.getMemberId());
            log.info("ArticleUpdateRequest : {} / {}", request.getTitle(), request.getTags());
            boardTagService.updateBoardTag(request.getArticleId(), request.getTags());
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
            articleService.deleteArticle(id, memberPrincipal.getMemberId());
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
