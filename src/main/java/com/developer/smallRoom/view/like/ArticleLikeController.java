package com.developer.smallRoom.view.like;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.like.ArticleLikeService;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.like.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/{id}")
    public ResponseEntity<ArticleLikeResponse> saveLike(@PathVariable("id") Long articleId,
                                                        @LoginMember MemberPrincipal memberPrincipal) {
        ArticleLikeResponse articleLikeResponse = articleLikeService.saveArticleLike(articleId, memberPrincipal.getMemberId());
        return ResponseEntity.ok().body(articleLikeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleLikeResponse> removeLike(@PathVariable("id") Long articleId,
                                                          @LoginMember MemberPrincipal memberPrincipal) {
        ArticleLikeResponse articleLikeResponse = articleLikeService.removeArticleLike(articleId, memberPrincipal.getMemberId());
        return ResponseEntity.ok().body(articleLikeResponse);
    }
}
