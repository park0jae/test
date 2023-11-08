package com.zerozae.exhibition.domain.member.controller;

import com.zerozae.exhibition.domain.member.dto.MemberCondition;
import com.zerozae.exhibition.domain.member.dto.MemberCreateRequest;
import com.zerozae.exhibition.domain.member.dto.MemberResponse;
import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import com.zerozae.exhibition.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        MemberResponse member = memberService.createMember(memberCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/members/" + member.getId())).body(member);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMembers() {
        return ResponseEntity.status(OK).body(memberService.findAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(memberService.findMemberById(id));
    }

    @GetMapping("/condition")
    public ResponseEntity<MemberResponse> findMemberByCondition(@Valid @ModelAttribute MemberCondition memberCondition) {
        return ResponseEntity.status(OK).body(memberService.findMemberByCondition(memberCondition));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateMember(@PathVariable Long id,
                                             @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {

        memberService.updateMember(id, memberUpdateRequest);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMemberById() {
        memberService.deleteAllMembers();
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }
}
