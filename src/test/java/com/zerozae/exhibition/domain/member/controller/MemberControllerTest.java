package com.zerozae.exhibition.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozae.exhibition.domain.member.dto.MemberCondition;
import com.zerozae.exhibition.domain.member.dto.MemberCreateRequest;
import com.zerozae.exhibition.domain.member.dto.MemberResponse;
import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import com.zerozae.exhibition.domain.member.entity.Member;
import com.zerozae.exhibition.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.zerozae.exhibition.factory.member.MemberFactory.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    private Member member = createMember();

    @Test
    void 회원_생성_테스트() throws Exception {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNumber());

        MemberResponse memberResponse = MemberResponse.toDto(member);
        given(memberService.createMember(memberCreateRequest)).willReturn(memberResponse);

        // When
        mvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequest)))
                .andExpect(status().isCreated());

        // Then
        verify(memberService).createMember(memberCreateRequest);
    }

    @Test
    void 회원_전체_조회_테스트() throws Exception {
        // Given
        List<MemberResponse> members = List.of(MemberResponse.toDto(member));
        given(memberService.findAllMembers()).willReturn(members);

        // When
        mvc.perform(get("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(member.getId()))
                .andExpect(jsonPath("$[0].nickname").value(member.getNickname()))
                .andExpect(jsonPath("$[0].email").value(member.getEmail()))
                .andExpect(jsonPath("$[0].phoneNumber").value(member.getPhoneNumber()));

        // Then
        verify(memberService).findAllMembers();
    }

    @Test
    void 회원_검색_조건_이메일_검색_테스트() throws Exception {
        // Given
        MemberCondition memberCondition = new MemberCondition(member.getEmail(), null);
        given(memberService.findMemberByCondition(memberCondition)).willReturn(MemberResponse.toDto(member));

        // When
        mvc.perform(get("/api/v1/members/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", member.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()));

        // Then
        verify(memberService).findMemberByCondition(memberCondition);
    }

    @Test
    void 회원_검색_조건_닉네임_검색_테스트() throws Exception {
        // Given
        MemberCondition memberCondition = new MemberCondition(null, member.getNickname());
        given(memberService.findMemberByCondition(memberCondition)).willReturn(MemberResponse.toDto(member));

        // When
        mvc.perform(get("/api/v1/members/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nickname", member.getNickname()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()));

        // Then
        verify(memberService).findMemberByCondition(memberCondition);
    }

    @Test
    void 회원_수정_테스트() throws Exception {
        // Given
        Long memberId = 1L;
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("updateMember", "01099998888");
        given(memberService.findMemberById(memberId)).willReturn(MemberResponse.toDto(member));

        // When
        mvc.perform(patch("/api/v1/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateRequest)))
                .andExpect(status().isNoContent());

        // Then
        verify(memberService).updateMember(memberId, memberUpdateRequest);
    }

    @Test
    void 회원_아이디로_삭제_테스트() throws Exception {
        // Given
        Long memberId = 1L;
        given(memberService.findMemberById(memberId)).willReturn(MemberResponse.toDto(member));

        // When
        mvc.perform(delete("/api/v1/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(memberService).deleteMemberById(memberId);
    }

    @Test
    void 회원_전체_삭제_테스트() throws Exception {
        // Given & When
        mvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(memberService).deleteAllMembers();
    }
}
