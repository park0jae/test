package com.zerozae.exhibition.domain.reservation.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationUpdateRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_전시회명_입력으로_예약_실패_테스트() {
        // Given
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDateTime.now(),
                "e"
        );

        // When
        Set<ConstraintViolation<ReservationUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.");
    }

}
