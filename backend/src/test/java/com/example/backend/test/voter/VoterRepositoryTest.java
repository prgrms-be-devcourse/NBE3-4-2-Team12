package com.example.backend.test.voter;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import com.example.backend.domain.voter.repository.VoterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class VoterRepositoryTest {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Vote vote;
    private Member member;
    private Voter.VoterId voterId;
    private Voter voter;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder().build());
        vote = voteRepository.save(Vote.builder().groupId(1L).build());

        voterId = new Voter.VoterId(member.getId(), vote.getId());
        voter = new Voter(voterId, member, vote);
    }

    @Test
    @DisplayName("투표 참여 저장 테스트")
    void saveVoterTest() {
        // when
        Voter savedVoter = voterRepository.save(voter);

        // then
        assertThat(savedVoter).isNotNull();
        assertThat(savedVoter.getId()).isEqualTo(voterId);
    }

    @Test
    @DisplayName("투표에 참여한 멤버 목록 조회 테스트")
    void findVotersByVoteTest() {
        // given
        voterRepository.save(voter);

        // when
        List<Voter> voters = voterRepository.findByIdVoteId(vote.getId());

        // then
        assertThat(voters).isNotEmpty();
        assertThat(voters.get(0).getId()).isEqualTo(voterId);
    }

    @Test
    @DisplayName("멤버가 특정 투표에 참여했는지 확인")
    void existsByIdTest() {
        // given
        voterRepository.save(voter);

        // when
        boolean exists = voterRepository.existsById(voterId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("투표 취소 테스트")
    void deleteVoterTest() {
        // given
        voterRepository.save(voter);

        // when
        voterRepository.deleteById(voterId);
        Optional<Voter> deletedVoter = voterRepository.findById(voterId);

        // then
        assertThat(deletedVoter).isEmpty();
    }
}
