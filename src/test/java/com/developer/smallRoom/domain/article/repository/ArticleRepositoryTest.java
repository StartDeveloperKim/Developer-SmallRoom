package com.developer.smallRoom.domain.article.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        member = memberRepository.save(MemberFactory.getMemberDefaultValue());
    }

    @DisplayName("findByIdAndMember() : 게시글ID와 게시자기준으로 게시글을 조회한다.")
    @Test
    void findByIdAndMember() {
        //given
        Article savedArticle = savedNewArticle();
        //when
        Article findArticle = articleRepository.findByIdAndMemberId(savedArticle.getId(), member.getId()).get();
        //then
        assertThat(savedArticle.getId()).isEqualTo(findArticle.getId());
    }

    @DisplayName("existsByIdAndMember() : 게시글ID와 게시자 기준으로 게시글을 존재하는지 확인한다.")
    @Test
    void existsByIdAndMember() {
        //given
        Article savedArticle = savedNewArticle();
        //when
        boolean result = articleRepository.existsByIdAndMember(savedArticle.getId(), member);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("findArticlesByWithPaging() : 게시글목록을 페이징을 활용해서 조회한다.")
    @Test
    void findArticlesByWithPaging() {
        //given
        Article article1 = savedNewArticle();
        Article article2 = savedNewArticle();
        Article article3 = savedNewArticle();
        Article article4 = savedNewArticle();
        Pageable pageable = PageRequest.of(0, 4, Sort.by("createAt").descending());
        //when
        Page<Article> result = articleRepository.findArticlesBy(pageable);
        //then
        List<Article> content = result.getContent();
        assertThat(content.get(0).getId()).isEqualTo(article4.getId());
        assertThat(content.get(1).getId()).isEqualTo(article3.getId());
        assertThat(content.get(2).getId()).isEqualTo(article2.getId());
        assertThat(content.get(3).getId()).isEqualTo(article1.getId());
    }

    private Article savedNewArticle() {
        return articleRepository.save(ArticleFactory.getDefaultValueArticle(member));
    }
}