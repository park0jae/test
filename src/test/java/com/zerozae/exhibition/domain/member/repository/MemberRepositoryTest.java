package com.zerozae.exhibition.domain.member.repository;

import com.zerozae.exhibition.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.zerozae.exhibition.factory.member.MemberFactory.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    private Member member = createMember();

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원_저장_테스트() {
        // Given

        // When
        Member savedMember = memberRepository.save(member);

        // Then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isEqualTo(member.getId());
        assertThat(savedMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
    }

    @Test
    void 회원_이메일로_조회_테스트() {
        // Given
        memberRepository.save(member);

        // When
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        // Then
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.get().getPhoneNumber()).isEqualTo(member.getPhoneNumber());
    }

    @Test
    void 회원_닉네임으로_조회_테스트() {
        // Given
        memberRepository.save(member);

        // When
        Optional<Member> findMember = memberRepository.findByNickname(member.getNickname());

        // Then
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.get().getPhoneNumber()).isEqualTo(member.getPhoneNumber());
    }
}
