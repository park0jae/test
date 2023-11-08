package com.zerozae.exhibition.domain.member.dto;

import com.zerozae.exhibition.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String nickname;
    private String email;
    private String phoneNumber;

    public static MemberResponse toDto(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber()
        );
    }
}
