package com.developer.smallRoom.view.search;

import com.developer.smallRoom.application.article.service.ArticleService;
import com.developer.smallRoom.application.boardTag.BoardTagService;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> searchArticle(@RequestParam("page") int page,
                                                                   @RequestParam("query") String query,
                                                                   @RequestParam("standard") String standard) {
        List<String> tags = Arrays.asList(query.split(","));
        List<HomeArticleResponse> articles = articleService.searchArticlesByTags(page, tags, standard);
        return ResponseEntity.ok().body(articles);
    }
}
