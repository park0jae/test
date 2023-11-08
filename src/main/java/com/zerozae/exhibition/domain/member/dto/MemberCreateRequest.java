package com.zerozae.exhibition.domain.member.dto;


import com.zerozae.exhibition.domain.member.entity.Member;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record MemberCreateRequest(
        @Length(min = 2, max = 15, message = "닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.")
        @NotBlank(message = "닉네임은 필수 항목입니다.")
        String nickname,

        @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", message = "이메일 형식에 맞게 입력해주세요.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email,

        @Pattern(regexp = "^(\\d{10}|\\d{11})$", message = "전화번호 형식에 맞게 입력해주세요.")
        @NotBlank(message = "전화번호는 필수 항목입니다.")
        String phoneNumber) {

    public Member toEntity() {
        return new Member(
                nickname,
                email,
                phoneNumber
        );
    }
}
