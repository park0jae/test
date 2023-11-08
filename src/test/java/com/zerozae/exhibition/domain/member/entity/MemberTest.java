package com.zerozae.exhibition.domain.member.entity;

import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import com.zerozae.exhibition.domain.member.entity.Member;
import org.junit.jupiter.api.Test;

import static com.zerozae.exhibition.factory.member.MemberFactory.*;
import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    void 회원_생성_테스트() {
        // Given
        Member member = createMember();

        // When & Then
        assertThat(member).isNotNull();
    }

    @Test
    void 회원_수정_테스트() {
        // Given
        Member member = createMember();
        MemberUpdateRequest updatedMember = new MemberUpdateRequest("changeName", "01011112222");

        // When
        member.updateMember(updatedMember);

        // Then
        assertThat(member.getNickname()).isEqualTo(updatedMember.nickname());
        assertThat(member.getPhoneNumber()).isEqualTo(updatedMember.phoneNumber());
    }
}
