package com.zerozae.exhibition.domain.member.repository;

import com.zerozae.exhibition.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where (:email is null or m.email = :email) and (:nickname is null or m.nickname = :nickname)")
    Optional<Member> findMemberByCondition(@Param("email") String email, @Param("nickname") String nickname);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
