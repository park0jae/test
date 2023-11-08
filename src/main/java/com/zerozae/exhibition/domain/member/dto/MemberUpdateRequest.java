package com.zerozae.exhibition.domain.member.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;

public record MemberUpdateRequest(
        @Length(min = 2, max = 15, message = "닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.")
        @Nullable
        String nickname,

        @Pattern(regexp = "^(\\d{10}|\\d{11})$", message = "전화번호 형식에 맞게 입력해주세요.")
        @Nullable
        String phoneNumber) {

}
