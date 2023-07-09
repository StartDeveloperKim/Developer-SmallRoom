package com.developer.smallRoom.application.like;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class LikeCountSynchronizer {

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void syncLikeCount() {
        log.info("Start LikeCountSynchronizer");
        int size = 10;
        int remainSize = 1;

        long articleCount = articleRepository.count();
        long end = articleCount / size + remainSize;
        for (int page = 0; page < end; page++) {
            articleRepository.findArticlesBy(PageRequest.of(page, size))
                    .getContent()
                    .forEach(article ->
                            article.updateLikeCount(articleLikeRepository.countByArticleId(article.getId())));
        }
        log.info("End LikeCountSynchronizer");
    }
}
