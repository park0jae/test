package com.zerozae.exhibition.domain.exhibition.repository;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.zerozae.exhibition.factory.exhibition.ExhibitionFactory.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ExhibitionRepositoryTest {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    private Exhibition exhibition = createExhibition();

    @BeforeEach
    void cleanUp() {
        exhibitionRepository.deleteAll();
    }

    @Test
    void 전시회_저장_테스트() {
        // Given

        // When
        Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        // Then
        assertThat(savedExhibition.getId()).isEqualTo(exhibition.getId());
        assertThat(savedExhibition.getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        assertThat(savedExhibition.getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(savedExhibition.getLocation()).isEqualTo(exhibition.getLocation());
        assertThat(savedExhibition.getPrice()).isEqualTo(exhibition.getPrice());
    }

    @Test
    void 전시회명으로_조회_테스트() {
        // Given
        exhibitionRepository.save(exhibition);

        // When
        Optional<Exhibition> findExhibition = exhibitionRepository.findByExhibitionName(exhibition.getExhibitionName());

        // Then
        assertThat(findExhibition).isNotNull();
        assertThat(findExhibition.get().getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        assertThat(findExhibition.get().getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(findExhibition.get().getLocation()).isEqualTo(exhibition.getLocation());
        assertThat(findExhibition.get().getPrice()).isEqualTo(exhibition.getPrice());
    }
}
