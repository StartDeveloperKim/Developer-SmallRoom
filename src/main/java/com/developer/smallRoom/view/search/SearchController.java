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

    //    private final BoardTagService boardTagService;
    private final ArticleService articleService;

    // TODO :: 검색 쿼리를 수정할 필요가 있다. 만약 Java, SpringBoot 태그가 입력으로 주어졌다면 둘 다 포함되는 글이 조회되어야 한다.
    @GetMapping
    public ResponseEntity<List<HomeArticleResponse>> searchArticle(@RequestParam("page") int page,
                                                                   @RequestParam("query") String query) {
        List<String> tags = Arrays.asList(query.split(","));
        List<HomeArticleResponse> articles = articleService.searchArticlesByTags(page, tags);
        return ResponseEntity.ok().body(articles);
    }
}
