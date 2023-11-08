package com.zerozae.exhibition.domain.member.entity;

import com.zerozae.exhibition.domain.member.dto.MemberUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    public Member(String nickname, String email, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        if(memberUpdateRequest.nickname() != null) {
            this.nickname = memberUpdateRequest.nickname();
        }
        if(memberUpdateRequest.phoneNumber() != null) {
            this.phoneNumber = memberUpdateRequest.phoneNumber();
        }
    }
}
