package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleQueryServiceImpl implements ArticleQueryService {

    private final ArticleRepository articleRepository;

    @Transactional
    @Override
    public ArticleResponse getArticleById(Long articleId) {
        Article article = findArticleById(articleId);
        article.increaseHit();
        return new ArticleResponse(article);
    }

    @Override
    public ArticleResponse getArticleByIdAndMember(Long articleId, Long memberId) {
        return new ArticleResponse(findArticleByArticleAndMember(articleId, memberId));
    }

    @Override
    public List<HomeArticleResponse> getHomeArticleResponses(int page, String standard) {
        Page<Article> result = articleRepository.findArticlesBy(PageRequest.of(page, 4, Sort.by(standard).descending()));
        return result.getContent().stream()
                .map(HomeArticleResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<HomeArticleResponse> searchArticlesByTags(int page, List<String> tags, String standard) {
        List<Article> articles = articleRepository.
                findArticlesByTags(tags, PageRequest.of(page, 4, Sort.by(Sort.Direction.DESC, standard)));
        return articles.stream().map(HomeArticleResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<HomeArticleResponse> getRandomArticles() {
        return articleRepository.findArticlesRandom().stream()
                .map(HomeArticleResponse::new).collect(Collectors.toList());
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("not fount articleId"));
    }

    private Article findArticleByArticleAndMember(Long articleId, Long memberId) {
        return articleRepository.findByIdAndMemberId(articleId, memberId)
                .orElseThrow(() -> new NotAuthorizationException("잘못된 접근입니다."));
    }
}
