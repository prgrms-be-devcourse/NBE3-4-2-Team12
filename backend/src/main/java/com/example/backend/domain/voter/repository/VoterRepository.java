package com.example.backend.domain.voter.repository;
import com.example.backend.domain.voter.entity.Voter;

import com.example.backend.domain.voter.entity.Voter.VoterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoterRepository extends JpaRepository<Voter, VoterId> {
    // 특정 투표(voteId)에 참여한 Voter 목록 조회
    List<Voter> findByIdVoteId(Long voteId);

}