package com.zerozae.exhibition.domain.exhibition.service;

import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCondition;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCreateRequest;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionResponse;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionUpdateRequest;
import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import com.zerozae.exhibition.domain.exhibition.exception.DuplicateExhibitionException;
import com.zerozae.exhibition.domain.exhibition.exception.ExhibitionNotFoundException;
import com.zerozae.exhibition.domain.exhibition.repository.ExhibitionRepository;
import com.zerozae.exhibition.domain.file.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.zerozae.exhibition.factory.exhibition.ExhibitionFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExhibitionServiceTest {

    @InjectMocks
    private ExhibitionService exhibitionService;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private FileService fileService;

    private Exhibition exhibition = createExhibition();

    @Test
    void 전시회_생성_테스트() {
        // When
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                exhibition.getExhibitionName(),
                exhibition.getDescription(),
                exhibition.getStartTime(),
                exhibition.getEndTime(),
                exhibition.getLocation(),
                exhibition.getPrice(),
                null
        );

        Exhibition savedExhibition = request.toEntity();

        given(exhibitionRepository.existsByExhibitionName(request.exhibitionName())).willReturn(false);
        given(exhibitionRepository.save(any(Exhibition.class))).willReturn(savedExhibition);

        // When
        ExhibitionResponse exhibitionResponse = exhibitionService.createExhibition(request);

        // Then
        assertThat(exhibitionResponse.getId()).isEqualTo(exhibition.getId());
        assertThat(exhibitionResponse.getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        assertThat(exhibitionResponse.getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(exhibitionResponse.getLocation()).isEqualTo(exhibition.getLocation());
        assertThat(exhibitionResponse.getPrice()).isEqualTo(exhibition.getPrice());
        verify(exhibitionRepository).existsByExhibitionName(request.exhibitionName());
        verify(exhibitionRepository).save(any(Exhibition.class));
    }

    @Test
    void 전시회_생성_중복이름_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                exhibition.getExhibitionName(),
                exhibition.getDescription(),
                exhibition.getStartTime(),
                exhibition.getEndTime(),
                exhibition.getLocation(),
                exhibition.getPrice(),
                null
        );

        given(exhibitionRepository.existsByExhibitionName(exhibition.getExhibitionName())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> exhibitionService.createExhibition(request))
                .isInstanceOf(DuplicateExhibitionException.class);
    }

    @Test
    void 전시회_전체조회_테스트() {
        // Given
        given(exhibitionRepository.findAll()).willReturn(List.of());

        // When
        exhibitionService.findAllExhibitions();

        // Then
        verify(exhibitionRepository).findAll();
    }

    @Test
    void 전시회_아이디로_조회_테스트() {
        // Given
        given(exhibitionRepository.findById(exhibition.getId())).willReturn(Optional.of(exhibition));

        // When
        ExhibitionResponse findExhibition = exhibitionService.findExhibitionById(exhibition.getId());

        // Then
        assertThat(findExhibition.getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        verify(exhibitionRepository).findById(exhibition.getId());
    }

    @Test
    void 전시회_아이디로_조회_실패_테스트() {
        // Given
        Long notExistId = 0L;
        given(exhibitionRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> exhibitionService.findExhibitionById(notExistId))
                .isInstanceOf(ExhibitionNotFoundException.class);
    }

    @Test
    void 전시회_이름으로_검색_테스트() {
        // Given
        ExhibitionCondition condition = new ExhibitionCondition(exhibition.getExhibitionName(), null);
        given(exhibitionRepository.findAllByCondition(condition.exhibitionName(), condition.exhibitionTime())).willReturn(List.of(exhibition));

        // When
        List<ExhibitionResponse> exhibitions = exhibitionService.findExhibitionByCondition(condition);

        // Then
        assertThat(exhibitions.get(0).getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        verify(exhibitionRepository).findAllByCondition(condition.exhibitionName(), condition.exhibitionTime());
    }

    @Test
    void 전시회_시작날짜로_검색_테스트() {
        // Given
        ExhibitionCondition condition = new ExhibitionCondition(null, LocalDateTime.of(2023, 11, 3, 12, 0));
        given(exhibitionRepository.findAllByCondition(condition.exhibitionName(), condition.exhibitionTime())).willReturn(List.of(exhibition));

        // When
        List<ExhibitionResponse> exhibitions = exhibitionService.findExhibitionByCondition(condition);

        // Then
        assertThat(exhibitions.get(0).getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        verify(exhibitionRepository).findAllByCondition(condition.exhibitionName(), condition.exhibitionTime());
    }

    @Test
    void 전시회_업데이트_테스트() {
        // Given
        Long exhibitionId = exhibition.getId();
        ExhibitionUpdateRequest request = new ExhibitionUpdateRequest(
                "전시 1번 수정",
                "전시 1번 수정입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "전시장 1번에서 2번입니다.",
                20000L,
                null
        );

        given(exhibitionRepository.existsByExhibitionName(request.name())).willReturn(false);
        given(exhibitionRepository.findById(exhibitionId)).willReturn(Optional.of(exhibition));

        // When
        exhibitionService.updateExhibition(exhibitionId, request);

        // Then
        assertThat(exhibition.getExhibitionName()).isEqualTo(request.name());
        assertThat(exhibition.getDescription()).isEqualTo(request.description());
        assertThat(exhibition.getLocation()).isEqualTo(request.location());
        verify(exhibitionRepository).existsByExhibitionName(request.name());
        verify(exhibitionRepository).findById(exhibitionId);
    }

    @Test
    void 전시회_아이디로_삭제_테스트() {
        // Given
        given(exhibitionRepository.findById(exhibition.getId())).willReturn(Optional.of(exhibition));

        // When
        exhibitionService.deleteExhibitionById(exhibition.getId());

        // Then
        verify(exhibitionRepository).findById(exhibition.getId());
        verify(exhibitionRepository).deleteById(exhibition.getId());
    }

    @Test
    void 전시회_아이디로_삭제_실패_테스트() {
        // Given
        Long notExistId = 0L;
        given(exhibitionRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> exhibitionService.findExhibitionById(notExistId))
                .isInstanceOf(ExhibitionNotFoundException.class);
    }

    @Test
    void 전시회_전체삭제_테스트() {
        // Given

        // When
        exhibitionService.deleteAllExhibitions();

        // Then
        verify(exhibitionRepository).deleteAll();
    }
}
