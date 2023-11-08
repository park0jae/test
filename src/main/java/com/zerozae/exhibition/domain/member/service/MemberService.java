package com.zerozae.exhibition.domain.member.service;

import com.zerozae.exhibition.domain.member.dto.MemberCondition;
import com.zerozae.exhibition.domain.member.dto.MemberCreateRequest;
import com.zerozae.exhibition.domain.member.dto.MemberResponse;
import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import com.zerozae.exhibition.domain.member.entity.Member;
import com.zerozae.exhibition.domain.member.exception.DuplicateMemberException;
import com.zerozae.exhibition.domain.member.exception.MemberNotFoundException;
import com.zerozae.exhibition.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {
        validateDuplicateEmail(memberCreateRequest.email());
        validateDuplicateNicknameAndPhoneNumber(memberCreateRequest.nickname(), memberCreateRequest.phoneNumber());
        Member member = memberRepository.save(memberCreateRequest.toEntity());
        return MemberResponse.toDto(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberResponse::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.toDto(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberByCondition(MemberCondition memberCondition) {
        Member member = memberRepository.findMemberByCondition(
                memberCondition.email(),
                memberCondition.nickname()).orElseThrow(MemberNotFoundException::new);

        return MemberResponse.toDto(member);
    }

    public void updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        validateDuplicateNicknameAndPhoneNumber(memberUpdateRequest.nickname(), memberUpdateRequest.phoneNumber());
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.updateMember(memberUpdateRequest);
    }

    public void deleteMemberById(Long id) {
        memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.deleteById(id);
    }

    public void deleteAllMembers() {
        memberRepository.deleteAll();
    }

    private void validateDuplicateNicknameAndPhoneNumber(String nickname, String phoneNumber){
        if(memberRepository.existsByNickname(nickname)) {
            throw new DuplicateMemberException();
        }
        if(memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateMemberException();
        }
    }

    private void validateDuplicateEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicateMemberException();
        }
    }

}
