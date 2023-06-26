package com.developer.smallRoom.application.boardTag;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.boardTag.BoardTag;
import com.developer.smallRoom.domain.boardTag.repository.BoardTagRepository;
import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import com.developer.smallRoom.dto.article.response.HomeArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardTagServiceImpl implements BoardTagService {

    private final ArticleRepository articleRepository;
    private final BoardTagRepository boardTagRepository;
    private final TagRepository tagRepository;

    @Transactional
    @Override
    public void saveBoardTag(Long articleId, List<String> tags) {
        Article article = getArticle(articleId);
        saveBoardTags(tags, article);
    }

    @Transactional
    @Override
    public void updateBoardTag(Long articleId, List<String> tags) {
        Article article = getArticle(articleId);
        boardTagRepository.deleteByArticle(article);
        saveBoardTags(tags, article);
    }

    private void saveBoardTags(List<String> tags, Article article) {
        for (String tag : tags) {
            Optional<Tag> findTag = tagRepository.findTagByName(tag);
            findTag.ifPresent(value -> boardTagRepository.save(new BoardTag(article, value)));
        }
    }

    private Article getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findBoardTagByArticleId(Long articleId) {
        List<BoardTag> boardTags = boardTagRepository.findBoardTagsByArticleId(articleId);
        return boardTags.stream().map(BoardTag::getTagName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<HomeArticleResponse> searchBoardByTag(List<String> tags, int page) {
        List<BoardTag> boardTags = new ArrayList<>();
        for (String tag : tags) {
            boardTags = boardTagRepository.findBoardTagByTagName(PageRequest.of(page, 4), tag);
        }
        if (boardTags.isEmpty()) {
            return null; // TODO :: null 반환 리팩토링 필요
        }
        return boardTags.stream().map(boardTag -> new HomeArticleResponse(boardTag.getArticle())).collect(Collectors.toList());
    }
}
