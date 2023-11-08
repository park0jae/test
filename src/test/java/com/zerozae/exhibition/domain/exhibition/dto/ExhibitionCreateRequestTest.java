package com.zerozae.exhibition.domain.exhibition.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExhibitionCreateRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_전시회명_설정으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "e",
                "전시 1입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "전시장 1번",
                10000L,
                    null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.");
    }

    @Test
    void 전시회_시작시간_미설정으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "전시 1",
                "전시 1입니다.",
                null,
                LocalDateTime.now(),
                "전시장 1번",
                10000L,
                null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회 시작 시간은 필수 입력값 입니다.");
    }

    @Test
    void 전시회_종료시간_미설정으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "전시 1",
                "전시 1입니다.",
                LocalDateTime.now(),
                null,
                "전시장 1번",
                10000L,
                null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회 종료 시간은 필수 입력값 입니다.");
    }

    @Test
    void 전시회_장소_미입력으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "전시 1",
                "전시 1입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                10000L,
                null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회장은 필수 항목입니다.");
    }

    @Test
    void 전시회_금액_미입력으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "전시 1",
                "전시 1입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "전시장 1번",
                null,
                null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("가격은 필수 항목입니다.");
    }

    @Test
    void 전시회_금액_0입력으로_검증_실패_테스트() {
        // Given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "전시 1",
                "전시 1입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "전시장 1번",
                0L,
                null
        );

        // When
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("가격은 1 이상이어야 합니다.");
    }
}
