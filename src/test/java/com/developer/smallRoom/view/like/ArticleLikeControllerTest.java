package com.developer.smallRoom.view.like;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.jwt.JwtFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleLikeControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenProvider tokenProvider;

    private Member member;
    private Article article;
    private String accessToken;

    /*
     * TODO :: 테스트코드 리팩토링 필요함
     *  - TestJwtProperties 만들어서 사용하기
     *  - MemberFactory, ArticleFactory 작성해서 중복코드 삭제하기
     * */

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new TokenAuthenticationFilter(tokenProvider)).build();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        articleLikeRepository.deleteAll();

        member = memberRepository.save(Member.builder()
                .name("testUser")
                .gitHubId("testId")
                .imageUrl("image")
                .build());

        article = articleRepository.save(Article.builder()
                .title("title")
                .content("content")
                .thumbnailUrl("thumbnail")
                .member(member).build());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("role", member.getRole().getKey());

        accessToken = JwtFactory.builder()
                .subject(member.getGitHubId())
                .claims(claims)
                .build().createToken(jwtProperties);
    }

    @DisplayName("로그인한 Member는 좋아요를 누를 수 있다.")
    @Test
    void saveLikeLoginMember() throws Exception {
        //given
        Long articleId = article.getId();
        //when
        ResultActions result = mvc.perform(post("/api/like/" + articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result").value(true));
    }

    @DisplayName("로그인한 Member는 좋아요를 취소할 수 있다.")
    @Test
    void removeLikeLoginMember() throws Exception {
        //given
        articleLikeRepository.save(new ArticleLike(article, member));
        Long articleId = article.getId();
        //when
        ResultActions result = mvc.perform(delete("/api/like/" + articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result").value(true));
    }

    private Cookie getAccessTokenCookie(String accessToken) {
        return new Cookie("access_token", accessToken);
    }

}