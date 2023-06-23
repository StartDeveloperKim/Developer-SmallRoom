package com.developer.smallRoom.domain.article.repository;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        member = memberRepository.save(Member.builder()
                .gitHubId("testId")
                .name("testName")
                .gitHubId("testImage")
                .build());
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

    private Article savedNewArticle() {
        return articleRepository.save(Article.builder()
                .title("title")
                .subTitle("subTitle")
                .content("content")
                .githubLink("gitHubLink")
                .thumbnailUrl("thumbnail")
                .member(member)
                .build());
    }
}