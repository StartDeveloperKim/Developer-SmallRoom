package com.developer.smallRoom.view.search;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.boardTag.BoardTag;
import com.developer.smallRoom.domain.boardTag.repository.BoardTagRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import com.developer.smallRoom.view.ControllerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SearchControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BoardTagRepository boardTagRepository;

    private Article article;
    private final String TAG_NAME = "test";

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();

        Tag tag = tagRepository.save(new Tag(TAG_NAME));

        Member member = memberRepository.save(MemberFactory.getMemberDefaultValue());
        article = articleRepository.save(ArticleFactory.getDefaultValueArticle(member));
        boardTagRepository.save(new BoardTag(article, tag));
    }

    @DisplayName("searchArticle() : 태그이름을 사용해서 게시글을 검색할 수 있다.")
    @Test
    void searchArticle() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/search")
                .param("page", "0")
                .param("query", TAG_NAME)
                .param("standard", "createAt"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].articleId").value(article.getId()));

        result.andDo(print())
                .andDo(document("article-search",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("query").description("검색 태그"),
                                parameterWithName("standard").description("정렬기준")
                        ),
                        responseFields(
                                fieldWithPath("[]").description("게시글 목록"),
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].title").description("제목"),
                                fieldWithPath("[].subTitle").description("부제목"),
                                fieldWithPath("[].thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("[].memberGithubId").description("작성자 GitHub ID"),
                                fieldWithPath("[].createAt").description("작성일시"),
                                fieldWithPath("[].hit").description("조회수"),
                                fieldWithPath("[].likeCount").description("좋아요 수")
                        )
                ));
    }
}