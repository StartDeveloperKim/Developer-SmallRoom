package com.developer.smallRoom.domain.boardTag.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.boardTag.BoardTag;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BoardTagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardTagRepository boardTagRepository;

    Member member;
    Tag tag1;
    Tag tag2;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        boardTagRepository.deleteAll();

        tag1 = saveTag("태그1");
        tag2 = saveTag("태그2");

        member = memberRepository.save(MemberFactory.getMemberDefaultValue());
    }

    private Tag saveTag(String name) {
        return tagRepository.save(new Tag(name));
    }


    @DisplayName("findBoardTagByTagName() : 태그를 기준으로 검색한다. 이 때 페이징을 사용할 수 있다.")
    @Test
    void findBoardTagByTagName() {
        //given
        Article article1 = saveArticle();
        Article article2 = saveArticle(); // 제일 최신

        saveBoardTag(article1, tag1);
        saveBoardTag(article1, tag2);

        saveBoardTag(article2, tag1);
        //when
        List<BoardTag> boardTags = boardTagRepository.findBoardTagByTagName(PageRequest.of(0, 4), tag1.getName());
        //then
        Article findArticle2 = boardTags.get(0).getArticle();
        Article findArticle1 = boardTags.get(1).getArticle();

        assertThat(findArticle2.getId()).isEqualTo(article2.getId());
        assertThat(findArticle1.getId()).isEqualTo(article1.getId());
    }

    private Article saveArticle() {
        return articleRepository.save(ArticleFactory.getDefaultValueArticle(member));
    }

    private BoardTag saveBoardTag(Article article, Tag tag) {
        return boardTagRepository.save(new BoardTag(article, tag));
    }
}