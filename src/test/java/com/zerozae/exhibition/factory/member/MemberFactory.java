package com.zerozae.exhibition.factory.member;

import com.zerozae.exhibition.domain.member.entity.Member;

public class MemberFactory {

    public static Member createMember() {
        return new Member("member", "test123@gmail.com", "01012341234");
    }
}
