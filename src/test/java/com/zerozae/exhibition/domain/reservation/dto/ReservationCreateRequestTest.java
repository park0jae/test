package com.zerozae.exhibition.domain.reservation.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ReservationCreateRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_이메일_입력으로_예약_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDateTime.now(),
                "이메일이 아닙니다.",
                "전시 1"
        );

        // When
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("이메일 형식에 맞게 입력해주세요.");
    }

    @Test
    void 이메일_미입력으로_예약_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDateTime.now(),
                null,
                "전시 1"
        );

        // When
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("이메일은 필수 항목입니다.");
    }

    @Test
    void 예약_날짜_미입력으로_예약_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                null,
                "test123@gmail.com",
                "전시 1"
        );

        // When
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("예약 날짜 및 시간은 필수 입력 항목입니다.");
    }

    @Test
    void 전시회명_미입력으로_예약_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDateTime.now(),
                "test123@gmail.com",
                null
        );

        // When
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회명은 필수 항목입니다.");
    }

    @Test
    void 잘못된_전시회명_입력으로_예약_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDateTime.now(),
                "test123@gmail.com",
                "e"
        );

        // When
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.");
    }
}
