package com.zerozae.exhibition.domain.exhibition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCondition;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCreateRequest;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionResponse;
import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import com.zerozae.exhibition.domain.exhibition.service.ExhibitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.zerozae.exhibition.factory.exhibition.ExhibitionFactory.createExhibition;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExhibitionController.class)
class ExhibitionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExhibitionService exhibitionService;

    private Exhibition exhibition = createExhibition();

    @Test
    void 전시회_생성_테스트() throws Exception {
        // Given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("exhibitionName", exhibition.getExhibitionName());
        formData.add("description", exhibition.getDescription());
        formData.add("startTime", exhibition.getStartTime().toString());
        formData.add("endTime", exhibition.getEndTime().toString());
        formData.add("location", exhibition.getLocation());
        formData.add("price", String.valueOf(exhibition.getPrice()));

        ExhibitionResponse response = ExhibitionResponse.toDto(exhibition);
        given(exhibitionService.createExhibition(any(ExhibitionCreateRequest.class))).willReturn(response);

        // When
        mvc.perform(post("/api/v1/exhibitions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData))
                .andExpect(status().isCreated());

        // Then
        verify(exhibitionService).createExhibition(any(ExhibitionCreateRequest.class));
    }

    @Test
    void 전시회_전체조회_테스트() throws Exception {
        // Given
        List<ExhibitionResponse> exhibitions = List.of(ExhibitionResponse.toDto(exhibition));
        given(exhibitionService.findAllExhibitions()).willReturn(exhibitions);

        // When
        mvc.perform(get("/api/v1/exhibitions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].exhibitionName").value(exhibition.getExhibitionName()))
                .andExpect(jsonPath("$[0].description").value(exhibition.getDescription()))
                .andExpect(jsonPath("$[0].location").value(exhibition.getLocation()))
                .andExpect(jsonPath("$[0].price").value(exhibition.getPrice()));

        // Then
        verify(exhibitionService).findAllExhibitions();
    }

    @Test
    void 전시회_이름검색_테스트() throws Exception {
        // Given
        ExhibitionCondition condition = new ExhibitionCondition(exhibition.getExhibitionName(), null);
        given(exhibitionService.findExhibitionByCondition(condition)).willReturn(List.of(ExhibitionResponse.toDto(exhibition)));

        // When
        mvc.perform(get("/api/v1/exhibitions/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("exhibitionName", exhibition.getExhibitionName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].exhibitionName").value(exhibition.getExhibitionName()))
                .andExpect(jsonPath("$[0].description").value(exhibition.getDescription()))
                .andExpect(jsonPath("$[0].location").value(exhibition.getLocation()))
                .andExpect(jsonPath("$[0].price").value(exhibition.getPrice()));

        // Then
        verify(exhibitionService).findExhibitionByCondition(condition);
    }

    @Test
    void 전시회_시작날짜검색_테스트() throws Exception {
        // Given
        ExhibitionCondition condition = new ExhibitionCondition(null, exhibition.getStartTime());
        given(exhibitionService.findExhibitionByCondition(condition)).willReturn(List.of(ExhibitionResponse.toDto(exhibition)));

        // When
        mvc.perform(get("/api/v1/exhibitions/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("exhibitionTime", String.valueOf(exhibition.getStartTime())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].exhibitionName").value(exhibition.getExhibitionName()))
                .andExpect(jsonPath("$[0].description").value(exhibition.getDescription()))
                .andExpect(jsonPath("$[0].location").value(exhibition.getLocation()))
                .andExpect(jsonPath("$[0].price").value(exhibition.getPrice()));

        // Then
        verify(exhibitionService).findExhibitionByCondition(condition);
    }

    @Test
    void 전시회_수정_테스트() throws Exception {
        // Given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("exhibitionName", "전시 1 수정");
        formData.add("description", "전시 1 수정입니다.");
        formData.add("startTime", exhibition.getStartTime().toString());
        formData.add("endTime", exhibition.getEndTime().toString());
        formData.add("location", "전시장 1번 수정");
        formData.add("price", String.valueOf(20000L));
        Long exhibitionId = 1L;

        // When
        mvc.perform(patch("/api/v1/exhibitions/{id}",exhibitionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData))
                .andExpect(status().isNoContent());
    }

    @Test
    void 전시회_아이디로_삭제_테스트() throws Exception {
        // Given
        Long exhibitionId = 1L;
        given(exhibitionService.findExhibitionById(exhibitionId)).willReturn(ExhibitionResponse.toDto(exhibition));

        // When
        mvc.perform(delete("/api/v1/exhibitions/{id}", exhibitionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(exhibitionService).deleteExhibitionById(exhibitionId);
    }

    @Test
    void 전시회_전체_삭제_테스트() throws Exception {
        // Given

        // When
        mvc.perform(delete("/api/v1/exhibitions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(exhibitionService).deleteAllExhibitions();
    }
}
