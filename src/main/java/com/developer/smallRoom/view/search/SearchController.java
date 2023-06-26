package com.developer.smallRoom.view.search;

import com.developer.smallRoom.application.boardTag.BoardTagService;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import com.developer.smallRoom.dto.search.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final BoardTagService boardTagService;

    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> searchArticle(@RequestParam("page") int page,
                                                                   @RequestParam("query") String query) {
        List<String> tags = Arrays.asList(query.split(","));
        List<HomeArticleResponse> articles = boardTagService.searchBoardByTag(tags, page);
        return ResponseEntity.ok().body(articles);
    }
}
