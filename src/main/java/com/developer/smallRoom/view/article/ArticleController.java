package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.service.ArticleManagementService;
import com.developer.smallRoom.application.article.service.ArticleQueryService;
import com.developer.smallRoom.application.article.service.ArticleServiceFactory;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.boardTag.BoardTagService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleRetouchResponse;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
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

    private final ArticleQueryService articleQueryService;
    private final ArticleServiceFactory articleServiceFactory;
    private final BoardTagService boardTagService;

    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> getaHomeArticle(@RequestParam(value = "page") int page,
                                                               @RequestParam(value = "standard") String standard) {
        log.info("page = {} // standard = {}", page, standard);

        List<HomeArticleResponse> homeArticleResponses = articleQueryService.getHomeArticleResponses(page, standard);
        return ResponseEntity.ok().body(homeArticleResponses);
    }

    @PostMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> postArticle(@Validated @RequestBody ArticleRequest articleRequest,
                                                              @LoginMember MemberPrincipal memberPrincipal) {
        log.info("ArticleRequest : {} / {}", articleRequest.getTitle(), articleRequest.getTags());

        ArticleManagementService articleManagementService = getArticleManagementService(memberPrincipal);
        Long savedArticleId = articleManagementService.saveArticle(memberPrincipal, articleRequest);
        boardTagService.saveBoardTag(savedArticleId, articleRequest.getTags());

        return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 등록되었습니다.", savedArticleId));
    }

    @PutMapping
    public ResponseEntity<ArticleRetouchResponse<Long>> updateArticle(@Validated @RequestBody ArticleUpdateRequest request,
                                                                      @LoginMember MemberPrincipal memberPrincipal) {
        try {
            ArticleManagementService articleManagementService = getArticleManagementService(memberPrincipal);
            Long articleId = articleManagementService.updateArticle(request, memberPrincipal);

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
        try {
            ArticleManagementService articleManagementService = getArticleManagementService(memberPrincipal);
            articleManagementService.deleteArticle(id, memberPrincipal);
            return ResponseEntity.ok().body(new ArticleRetouchResponse<>("게시글이 삭제되었습니다."));
        } catch (NotAuthorizationException e) {
            return ResponseEntity.badRequest().body(new ArticleRetouchResponse<>(e.getMessage()));
        }
    }

    private ArticleManagementService getArticleManagementService(MemberPrincipal memberPrincipal) {
        return articleServiceFactory.getArticleManagementService(memberPrincipal.isAdmin());
    }
}
