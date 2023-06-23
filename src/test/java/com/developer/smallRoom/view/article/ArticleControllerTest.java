package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.jwt.JwtFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
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

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenProvider tokenProvider;

    private Member member;
    private String accessToken;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new TokenAuthenticationFilter(tokenProvider)).build();
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        member = memberRepository.save(Member.builder()
                .name("testUser")
                .gitHubId("testId")
                .imageUrl("image")
                .build());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("name", member.getName());
        claims.put("role", member.getRole().getKey());

        accessToken = JwtFactory.builder()
                .subject(member.getGitHubId())
                .claims(claims)
                .build().createToken(jwtProperties);
    }

    @DisplayName("postArticle() : 로그인한 사용자는 게시글 등록이 가능하다")
    @Test
    void postArticle() throws Exception {
        //given
        List<String> tags = Arrays.asList("tag1", "tag2");

        ArticleRequest articleRequest = new ArticleRequest("title", "subTitle", "content", "githubLink", "thumbnail", tags);
        String requestBody = objectMapper.writeValueAsString(articleRequest);

        //when
        ResultActions result = mvc.perform(post("/api/article")
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(status().isOk());

        List<Article> articles = articleRepository.findAll();
        assertThat(articles.get(0).getTitle()).isEqualTo(articleRequest.getTitle());
    }

    @DisplayName("updateArticle() : 글의 작성자는 글의 수정할 수 있다.")
    @Test
    void updateArticle() throws Exception {
        //given
        Article article = getArticle("title", "content", member);

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest(article.getId(), updateTitle, "subTitle", "gitHubLink",
                updateContent, "", Arrays.asList("태그1", "태그2"));
        String requestBody = objectMapper.writeValueAsString(articleUpdateRequest);

        //when
        ResultActions result = mvc.perform(put("/api/article")
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.responseData").value(article.getId()));
        result.andExpect(jsonPath("$.message").value("게시글이 수정되었습니다."));
    }

    @DisplayName("notAuthorizationUpdate() : 다른 작성자의 글을 수정할 수 없다.")
    @Test
    void notAuthorizationUpdate() throws Exception {
        //given
        Member otherMember = getOtherMember();

        Article memberArticle = getArticle("title", "content", member);
        Article otherArticle = getArticle("anotherTitle", "anotherContent", otherMember);

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest(memberArticle.getId(), updateTitle, "subTitle", "gitHubLink",
                updateContent, "", Arrays.asList("태그1", "태그2"));
        String requestBody = objectMapper.writeValueAsString(articleUpdateRequest);

        String otherMemberAccessToken = getOtherMemberAccessToken(otherMember);
        //when
        ResultActions result = mvc.perform(put("/api/article")
                .cookie(getAccessTokenCookie(otherMemberAccessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.message").value("잘못된 접근입니다."));
    }

    @DisplayName("deleteArticle() : 게시글을 삭제할 수 있다.")
    @Test
    void deleteArticle() throws Exception {
        //given
        Article memberArticle = getArticle("title", "content", member);
        //when
        ResultActions result = mvc.perform(delete("/api/article/"+memberArticle.getId())
                .cookie(getAccessTokenCookie(accessToken)));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."));

        List<Article> articles = articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(0);
    }

    @DisplayName("notAuthorizationDelete() : 다른 작성자의 게시글을 삭제할 수 없다.")
    @Test
    void notAuthorizationDelete() throws Exception {
        //given
        Article memberArticle = getArticle("title", "content", member);

        Member otherMember = getOtherMember();
        String otherMemberAccessToken = getOtherMemberAccessToken(otherMember);
        //when
        ResultActions result = mvc.perform(delete("/api/article/"+memberArticle.getId())
                .cookie(getAccessTokenCookie(otherMemberAccessToken)));
        //then
        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.message").value("잘못된 접근입니다."));
    }

    private Member getOtherMember() {
        return memberRepository.save(Member.builder()
                .gitHubId("anotherMember")
                .name("anotherMemberName")
                .imageUrl("image")
                .build());
    }

    private String getOtherMemberAccessToken(Member otherMember) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", otherMember.getName());
        claims.put("role", otherMember.getRole().getKey());

        return JwtFactory.builder()
                .subject(otherMember.getGitHubId())
                .claims(claims)
                .build().createToken(jwtProperties);
    }

    private Article getArticle(String title, String content, Member member) {
        return articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .thumbnailUrl("thumbnail")
                .member(member).build());
    }

    private Cookie getAccessTokenCookie(String accessToken) {
        return new Cookie("access_token", accessToken);
    }
}