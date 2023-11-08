package com.zerozae.exhibition.domain.member.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberUpdateRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_닉네임_설정으로_회원_수정_실패_테스트() {
        MemberUpdateRequest request = new MemberUpdateRequest("m", "01012345678");
        Set<ConstraintViolation<MemberUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.");
    }

    @Test
    void 잘못된_폰넘버_설정으로_회원_수정_실패_테스트() {
        MemberUpdateRequest request = new MemberUpdateRequest("member", "12345678");
        Set<ConstraintViolation<MemberUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactly("전화번호 형식에 맞게 입력해주세요.");
    }
}
