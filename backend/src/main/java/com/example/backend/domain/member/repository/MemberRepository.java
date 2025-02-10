package com.example.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backend.domain.member.dto.MemberInfoDto;
import com.example.backend.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByKakaoId(Long kakaoId);

	Optional<Member> findByKakaoId(Long kakaoId);

	@Query("SELECT new com.example.backend.domain.member.dto.MemberInfoDto(m.id, m.nickname, m.email) " +
		"FROM Member m WHERE m.id = :id")
	Optional<MemberInfoDto> findMemberInfoDtoById(Long id);

	Optional<Member> findByKakaoRefreshToken(String kakaoRefreshToken);
}
