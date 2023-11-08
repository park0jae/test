package com.zerozae.exhibition.domain.member.service;

import com.zerozae.exhibition.domain.member.dto.MemberCondition;
import com.zerozae.exhibition.domain.member.dto.MemberCreateRequest;
import com.zerozae.exhibition.domain.member.dto.MemberResponse;
import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import com.zerozae.exhibition.domain.member.entity.Member;
import com.zerozae.exhibition.domain.member.exception.DuplicateMemberException;
import com.zerozae.exhibition.domain.member.exception.MemberNotFoundException;
import com.zerozae.exhibition.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.zerozae.exhibition.factory.member.MemberFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member = createMember();

    @Test
    void 회원_생성_테스트() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber());

        given(memberRepository.existsByNickname(memberCreateRequest.nickname())).willReturn(false);
        given(memberRepository.existsByEmail(memberCreateRequest.email())).willReturn(false);
        given(memberRepository.existsByPhoneNumber(memberCreateRequest.phoneNumber())).willReturn(false);

        Member savedMember = memberCreateRequest.toEntity();
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // When
        MemberResponse memberResponse = memberService.createMember(memberCreateRequest);

        // Then
        assertThat(memberResponse.getId()).isEqualTo(member.getId());
        verify(memberRepository).existsByNickname(memberCreateRequest.nickname());
        verify(memberRepository).existsByEmail(memberCreateRequest.email());
        verify(memberRepository).existsByPhoneNumber(memberCreateRequest.phoneNumber());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void 회원_생성_중복닉네임_실패_테스트() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber());

        given(memberRepository.existsByNickname(memberCreateRequest.nickname())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberService.createMember(memberCreateRequest))
                .isInstanceOf(DuplicateMemberException.class);
    }

    @Test
    void 회원_생성_중복이메일_실패_테스트() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber());

        given(memberRepository.existsByEmail(memberCreateRequest.email())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberService.createMember(memberCreateRequest))
                .isInstanceOf(DuplicateMemberException.class);
    }

    @Test
    void 회원_생성_중복폰넘버_실패_테스트() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber());

        given(memberRepository.existsByPhoneNumber(memberCreateRequest.phoneNumber())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberService.createMember(memberCreateRequest))
                .isInstanceOf(DuplicateMemberException.class);
    }

    @Test
    void 전체_회원_조회_테스트() {
        // Given
        given(memberRepository.findAll()).willReturn(List.of());

        // When
        memberService.findAllMembers();

        // Then
        verify(memberRepository).findAll();
    }

    @Test
    void 회원_아이디로_조회_테스트() {
        // Given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // When
        MemberResponse memberResponse = memberService.findMemberById(member.getId());

        // Then
        assertThat(memberResponse.getId()).isEqualTo(member.getId());
        verify(memberRepository).findById(member.getId());
    }

    @Test
    void 회원_아이디로_조회_실패_테스트() {
        // Given
        Long notExistId = 0L;
        given(memberRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.findMemberById(notExistId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 회원_이메일로_조회_테스트() {
        // Given
        MemberCondition memberCondition = new MemberCondition(member.getEmail(), null);
        given(memberRepository.findMemberByCondition(memberCondition.email(), memberCondition.nickname())).willReturn(Optional.of(member));

        // When
        MemberResponse memberResponse = memberService.findMemberByCondition(memberCondition);

        // Then
        assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
        verify(memberRepository).findMemberByCondition(memberCondition.email(), memberCondition.nickname());
    }

    @Test
    void 회원_닉네임으로_조회_테스트() {
        // Given
        MemberCondition condition = new MemberCondition(null, member.getNickname());
        given(memberRepository.findMemberByCondition(condition.email(), condition.nickname())).willReturn(Optional.of(member));

        // When
        MemberResponse memberResponse = memberService.findMemberByCondition(condition);

        // Then
        assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
        verify(memberRepository).findMemberByCondition(condition.email(), condition.nickname());
    }


    @Test
    void 회원_수정_테스트() {
        // Given
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("update-Member", "01011112345");
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // When
        memberService.updateMember(member.getId(), memberUpdateRequest);

        // Then
        assertThat(member.getNickname()).isEqualTo(memberUpdateRequest.nickname());
        assertThat(member.getPhoneNumber()).isEqualTo(memberUpdateRequest.phoneNumber());
        verify(memberRepository).findById(member.getId());
    }

    @Test
    void 회원_수정_실패_테스트() {
        // Given
        Long notExistId = 0L;
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("update-Member", "01011112345");
        given(memberRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.updateMember(notExistId, memberUpdateRequest))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 회원_아이디로_삭제_테스트() {
        // Given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // When
        memberService.deleteMemberById(member.getId());

        // Then
        verify(memberRepository).findById(member.getId());
        verify(memberRepository).deleteById(member.getId());
    }

    @Test
    void 회원_아이디로_삭제_실패_테스트() {
        // Given
        Long notExistId = 0L;
        given(memberRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.deleteMemberById(notExistId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 회원_전체_삭제_테스트() {
        // Given & When
        memberService.deleteAllMembers();

        // Then
        verify(memberRepository).deleteAll();
    }
}
