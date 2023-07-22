package com.developer.smallRoom.view.member;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshToken;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.factory.MemberFactory;
import com.developer.smallRoom.jwt.TestJwtProperties;
import com.developer.smallRoom.view.ControllerTestBase;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MemberControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;

    private String testAccessToken;
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

        testAccessToken = tokenProvider.generateToken(member, Duration.ofHours(2));
    }

    @DisplayName("logout() : 로그인한 사용자는 로그아웃을 할 수 있다.")
    @Test
    void logout() throws Exception {
        //given
        String ACCESS_TOKEN = "access_token";
        String REFRESH_TOKEN = "refresh_token";

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, testAccessToken);
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, testRefreshToken);
        //when, then
        mvc.perform(RestDocumentationRequestBuilders.get("/logout")
                .cookie(accessTokenCookie)
                .cookie(refreshTokenCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print())
                .andDo(document("logout",
                        requestCookies(
                                cookieWithName(ACCESS_TOKEN).description("access_token"),
                                cookieWithName(REFRESH_TOKEN).description("refresh_token")
                        )));
    }
}