package com.developer.smallRoom.application.boardTag;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.boardTag.BoardTag;
import com.developer.smallRoom.domain.boardTag.repository.BoardTagRepository;
import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));
        for (String tag : tags) {
            Optional<Tag> findTag = tagRepository.findTagByName(tag);
            findTag.ifPresent(value -> boardTagRepository.save(new BoardTag(article, value)));
        }
    }

    @Override
    public List<String> findBoardTagByArticleId(Long articleId) {
        List<BoardTag> boardTags = boardTagRepository.findBoardTagsByArticleId(articleId);
        return boardTags.stream().map(BoardTag::getTagName).collect(Collectors.toList());
    }
}
