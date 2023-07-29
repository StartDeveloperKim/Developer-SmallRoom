package com.developer.smallRoom.view.autocomplete;

import com.developer.smallRoom.application.autocomplete.AutoCompleteRepository;
import com.developer.smallRoom.application.autocomplete.AutoCompleteService;
import com.developer.smallRoom.view.ControllerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutoCompleteControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AutoCompleteService autoCompleteService;
    @MockBean
    private AutoCompleteRepository autoCompleteRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }


    @DisplayName("getAutoCompleteWords() : 요청된 자동완성 쿼리에 대한 응답을 한다")
    @Test
    void getAutoCompleteWords() throws Exception {
        //given
        given(autoCompleteService.findAutoCompleteWords(anyString())).willReturn(Collections.emptyList());
        given(autoCompleteRepository.findIndexByValue(anyString(), anyString())).willReturn(Optional.of(1L));
        given(autoCompleteRepository.findWordsListByIndex(anyString(), anyLong(), anyLong())).willReturn(Collections.emptySet());

        //when, then
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/autocomplete")
                        .param("query", "A"));
        result.andExpect(status().isOk());
        result.andDo(print())
                .andDo(document("auto-complete",
                        queryParameters(
                                parameterWithName("query").description("입력단어")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("자동완성 단어")
                        )
                ));

    }
}