package com.zerozae.exhibition.domain.member.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class MemberCreateRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_닉네임_설정으로_회원_생성_실패_테스트() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest("m", "test123@gmail.com", "01022223333");

        // When
        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.");
    }

    @Test
    void 잘못된_이메일_설정으로_회원_생성_실패_테스트() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest("member", "email", "01022223333");

        // When
        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("이메일 형식에 맞게 입력해주세요.");
    }

    @Test
    void 잘못된_폰넘버_설정으로_회원_생성_실패_테스트() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest("member", "test123@gmail.com", "12345678");

        // When
        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전화번호 형식에 맞게 입력해주세요.");
    }
}
