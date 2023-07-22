package com.developer.smallRoom.view.like;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import com.developer.smallRoom.jwt.JwtFactory;
import com.developer.smallRoom.jwt.TestJwtProperties;
import com.developer.smallRoom.view.ControllerTestBase;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArticleLikeControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;
    private final TestJwtProperties testJwtProperties = new TestJwtProperties();

    private Member member;
    private Article article;
    private String accessToken;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new TokenAuthenticationFilter(tokenProvider, refreshTokenRepository)).build();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        articleLikeRepository.deleteAll();

        given(jwtProperties.getIssuer()).willReturn(testJwtProperties.getIssuer());
        given(jwtProperties.getSecretKey()).willReturn(testJwtProperties.getSecretKey());

        member = memberRepository.save(MemberFactory.getMemberDefaultValue());
        article = articleRepository.save(ArticleFactory.getDefaultValueArticle(member));
        accessToken = JwtFactory.builder()
                .subject(member.getGitHubId())
                .member(member)
                .build().createToken(testJwtProperties);
    }

    @DisplayName("로그인한 Member는 좋아요를 누를 수 있다.")
    @Test
    void saveLikeLoginMember() throws Exception {
        //given
        Long articleId = article.getId();
        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.post("/api/like/{id}", articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));

        result.andDo(document("save-like",
                pathParameters(
                        parameterWithName("id").description("게시글 ID")
                ), responseFields(
                        fieldWithPath("result").description("좋아요저장 결과")
                )
        ));
    }

    @DisplayName("로그인한 Member는 좋아요를 취소할 수 있다.")
    @Test
    void removeLikeLoginMember() throws Exception {
        //given
        articleLikeRepository.save(new ArticleLike(article, member));
        Long articleId = article.getId();
        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.delete("/api/like/{id}", articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$.result").value(true));

        result.andDo(document("cancel-like",
                pathParameters(
                        parameterWithName("id").description("게시글 ID")
                ), responseFields(
                        fieldWithPath("result").description("좋아요취소 결과")
                )
        ));
    }

    private Cookie getAccessTokenCookie(String accessToken) {
        return new Cookie("access_token", accessToken);
    }

}