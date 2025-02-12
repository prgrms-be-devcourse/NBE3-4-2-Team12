package com.example.backend.domain.voter.repository;
import com.example.backend.domain.voter.entity.Voter;

import com.example.backend.domain.voter.entity.Voter.VoterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoterRepository extends JpaRepository<Voter, VoterId> {

    // 특정 사용자가 이미 투표에 참여했는지 확인
    boolean existsById(VoterId voterId);

    // 특정 투표(voteId)에 참여한 Voter 목록 조회
    List<Voter> findByIdVoteId(Long voteId);

    // 특정 사용자의 투표 기록 삭제 (투표 취소)
    void deleteById(VoterId voterId);

    // 새로 추가할 메서드
    @Query("SELECT COUNT(v) FROM Voter v WHERE v.id.voteId = :voteId")
    long countVoters(Long voteId);

	// 투표id의 list중 현재 사용자가 투표한 투표의 id만 반환
	@Query("SELECT v.vote.id FROM Voter v WHERE v.vote.id IN :voteIds AND v.member.id = :memberId")
	List<Long> findVoteIdsByVoteIdsAndMemberId(@Param("voteIds") List<Long> voteIds, @Param("memberId") Long memberId);

	void deleteByVoteIdIn(List<Long> voteIds);
}