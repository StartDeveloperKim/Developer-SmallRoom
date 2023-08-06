package com.developer.smallRoom.view.search;

import com.developer.smallRoom.application.article.service.ArticleQueryService;
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

    private final ArticleQueryService articleQueryService;

    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> searchArticle(@RequestParam("page") int page,
                                                                   @RequestParam("query") String query,
                                                                   @RequestParam("standard") String standard) {
        List<String> tags = Arrays.asList(query.split(","));
        List<HomeArticleResponse> articles = articleQueryService.searchArticlesByTags(page, tags, standard);
        return ResponseEntity.ok().body(articles);
    }
}
