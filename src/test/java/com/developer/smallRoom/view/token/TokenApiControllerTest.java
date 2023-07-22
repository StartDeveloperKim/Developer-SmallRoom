package com.developer.smallRoom.view.token;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshToken;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.factory.MemberFactory;
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
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TokenApiControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;

    private String testRefreshToken;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();

        refreshTokenRepository.deleteAll();
        TestJwtProperties testJwtProperties = new TestJwtProperties();
        given(jwtProperties.getIssuer()).willReturn(testJwtProperties.getIssuer());
        given(jwtProperties.getSecretKey()).willReturn(testJwtProperties.getSecretKey());

        Member member = memberRepository.save(MemberFactory.getMemberDefaultValue());

        testRefreshToken = tokenProvider.generateToken(member, Duration.ofDays(14));
        refreshTokenRepository.save(new RefreshToken(member.getId(), testRefreshToken));
    }

    @DisplayName("generateNewAccessToken() : 해당 url로 접근하면 RefreshToken 확인한 후 새 AccessToken을 발급한다.")
    @Test
    void generateNewAccessToken() throws Exception {
        //given
        Cookie refreshTokenCookie = new Cookie("refresh_token", testRefreshToken);

        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/token")
                .cookie(refreshTokenCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(cookie().exists("access_token"));
        result.andDo(document("get-new-accessToken",
                requestCookies(
                        cookieWithName("refresh_token").description("refresh_token")
                ),
                responseCookies(
                        cookieWithName("access_token").description("access_token")
                )
        ));
    }
}