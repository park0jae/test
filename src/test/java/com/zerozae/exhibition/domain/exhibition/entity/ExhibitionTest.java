package com.zerozae.exhibition.domain.exhibition.entity;

import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionUpdateRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.zerozae.exhibition.factory.exhibition.ExhibitionFactory.*;
import static org.assertj.core.api.Assertions.*;

class ExhibitionTest {

    @Test
    void 전시회_생성_테스트() {
        // Given
        Exhibition exhibition = createExhibition();

        // When & Then
        assertThat(exhibition).isNotNull();
    }

    @Test
    void 전시회_업데이트_테스트() {
        // Given
        Exhibition exhibition = createExhibition();
        ExhibitionUpdateRequest exhibitionUpdateRequest = new ExhibitionUpdateRequest(
                "전시 2",
                "전시 2입니다.",
                LocalDateTime.of(2023, 11, 3, 12, 0),
                LocalDateTime.of(2023, 11, 5, 18, 0),
                "전시장 2번",
                20000L,
                null
        );

        // When
        exhibition.updateExhibition(exhibitionUpdateRequest);

        // Then
        assertThat(exhibition.getExhibitionName()).isEqualTo(exhibitionUpdateRequest.name());
        assertThat(exhibition.getDescription()).isEqualTo(exhibitionUpdateRequest.description());
        assertThat(exhibition.getLocation()).isEqualTo(exhibitionUpdateRequest.location());
    }
}
