package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import com.developer.smallRoom.jwt.JwtFactory;
import com.developer.smallRoom.jwt.TestJwtProperties;
import com.developer.smallRoom.view.ControllerTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArticleControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;
    private final TestJwtProperties testJwtProperties = new TestJwtProperties();

    private Member member;
    private String accessToken;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new TokenAuthenticationFilter(tokenProvider, refreshTokenRepository)).build();
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        given(jwtProperties.getIssuer()).willReturn(testJwtProperties.getIssuer());
        given(jwtProperties.getSecretKey()).willReturn(testJwtProperties.getSecretKey());

        member = memberRepository.save(MemberFactory.getMemberDefaultValue());
        accessToken = JwtFactory.builder()
                .subject(member.getGitHubId())
                .member(member)
                .build().createToken(testJwtProperties);
    }

    @DisplayName("postArticle() : 로그인한 사용자는 게시글 등록이 가능하다")
    @Test
    void postArticle() throws Exception {
        //given
        List<String> tags = Arrays.asList("tag1", "tag2");

        ArticleRequest articleRequest = new ArticleRequest("title", "subTitle", "content", "githubLink", "thumbnail", tags);
        String requestBody = objectMapper.writeValueAsString(articleRequest);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.post("/api/article")
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        List<Article> articles = articleRepository.findAll();
        assertThat(articles.get(0).getTitle()).isEqualTo(articleRequest.getTitle());

        result.andDo(document("post-article",
                requestFields(
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("subTitle").description("부제목"),
                        fieldWithPath("content").description("본문"),
                        fieldWithPath("gitHubLink").description("깃허브 링크"),
                        fieldWithPath("thumbnailUrl").description("썸네일 링크"),
                        fieldWithPath("tags").description("태그")
                ),
                responseFields(
                        fieldWithPath("message").description("결과 메시지"),
                        fieldWithPath("responseData").description("등록 게시글 ID")
                )
        ));
    }

    @DisplayName("updateArticle() : 글의 작성자는 글의 수정할 수 있다.")
    @Test
    void updateArticle() throws Exception {
        //given
        Article article = saveArticle("title", "content", member);

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest(article.getId(), updateTitle, "subTitle", "gitHubLink",
                updateContent, "", Arrays.asList("태그1", "태그2"));
        String requestBody = objectMapper.writeValueAsString(articleUpdateRequest);

        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.put("/api/article")
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseData").value(article.getId()))
                .andExpect(jsonPath("$.message").value("게시글이 수정되었습니다."));

        result.andDo(document("update-article",
                requestFields(
                        fieldWithPath("articleId").description("게시글ID"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("subTitle").description("부제목"),
                        fieldWithPath("content").description("본문"),
                        fieldWithPath("gitHubLink").description("깃허브 링크"),
                        fieldWithPath("thumbnailUrl").description("썸네일 링크"),
                        fieldWithPath("tags").description("태그")
                ),
                responseFields(
                        fieldWithPath("message").description("결과 메시지"),
                        fieldWithPath("responseData").description("수정 게시글 ID")
                )
        ));
    }

    @DisplayName("notAuthorizationUpdate() : 다른 작성자의 글을 수정할 수 없다.")
    @Test
    void notAuthorizationUpdate() throws Exception {
        //given
        Member otherMember = getOtherMember();

        Article memberArticle = saveArticle("title", "content", member);
        Article otherArticle = saveArticle("anotherTitle", "anotherContent", otherMember);

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
        Article memberArticle = saveArticle("title", "content", member);
        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.delete("/api/article/{id}", memberArticle.getId())
                .cookie(getAccessTokenCookie(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."));

        List<Article> articles = articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(0);

        result.andDo(document("delete-article",
                pathParameters(
                        parameterWithName("id").description("게시글ID")
                ),responseFields(
                        fieldWithPath("message").description("결과 메시지"),
                        fieldWithPath("responseData").description("응답 데이터 (null이 될 수 있음)")
                )
        ));
    }

    @DisplayName("notAuthorizationDelete() : 다른 작성자의 게시글을 삭제할 수 없다.")
    @Test
    void notAuthorizationDelete() throws Exception {
        //given
        Article memberArticle = saveArticle("title", "content", member);

        Member otherMember = getOtherMember();
        String otherMemberAccessToken = getOtherMemberAccessToken(otherMember);
        //when
        ResultActions result = mvc.perform(delete("/api/article/"+memberArticle.getId())
                .cookie(getAccessTokenCookie(otherMemberAccessToken)));
        //then
        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.message").value("잘못된 접근입니다."));
    }

    @DisplayName("getaHomeArticleByCreateAt() : 사용자는 최신순으로 게시글을 조회할 수 있다.")
    @Test
    void getaHomeArticleByCreateAt() throws Exception {
        //given
        Article article1 = saveArticle("title1", "content", member);
        Article article2 = saveArticle("title2", "content", member);
        Article article3 = saveArticle("title3", "content", member);
        Article article4 = saveArticle("title4", "content", member);

        //when
        String url = "/api/article?page=0&standard=createAt";
        ResultActions result = mvc.perform(get(url));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].articleId").value(article4.getId()))
                .andExpect(jsonPath("$[1].articleId").value(article3.getId()))
                .andExpect(jsonPath("$[2].articleId").value(article2.getId()))
                .andExpect(jsonPath("$[3].articleId").value(article1.getId()));
    }

    @DisplayName("getaHomeArticleByLikeCount() : 사용자는 좋아요순으로 게시글을 조회할 수 있다.")
    @Test
    void getaHomeArticleByLikeCount() throws Exception {
        /*//given
        Article article1 = saveArticle("title1", "content", member);
        Article article2 = saveArticle("title2", "content", member);
        Article article3 = saveArticle("title3", "content", member);
        Article article4 = saveArticle("title4", "content", member);

        //when
        String url = "/api/article?page=0&standard=likeCount";
        ResultActions result = mvc.perform(get(url));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].articleId").value(article4.getId()))
                .andExpect(jsonPath("$[1].articleId").value(article3.getId()))
                .andExpect(jsonPath("$[2].articleId").value(article2.getId()))
                .andExpect(jsonPath("$[3].articleId").value(article1.getId()));*/
    }

    private Member getOtherMember() {
        return memberRepository.save(MemberFactory.builder()
                .githubId("anotherMember")
                .name("anotherMemberName")
                .imageUrl("image")
                .build().getMember());
    }

    private String getOtherMemberAccessToken(Member otherMember) {
        return JwtFactory.builder()
                .subject(otherMember.getGitHubId())
                .member(otherMember)
                .build().createToken(testJwtProperties);
    }

    private Article saveArticle(String title, String content, Member member) {
        return articleRepository.save(ArticleFactory.builder()
                .title(title)
                .content(content)
                .member(member).build().getMember());
    }

    private Cookie getAccessTokenCookie(String accessToken) {
        return new Cookie("access_token", accessToken);
    }
}