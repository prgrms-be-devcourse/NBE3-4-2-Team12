package com.example.backend.test.voter;

import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.repository.GroupRepository;
import com.example.backend.domain.groupmember.repository.GroupMemberRepository;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.exception.VoterErrorCode;
import com.example.backend.domain.voter.exception.VoterException;
import com.example.backend.domain.voter.repository.VoterRepository;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import com.example.backend.domain.voter.service.VoterService;
import com.example.backend.global.auth.model.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoterServiceTest {

    @InjectMocks
    private VoterService voterService;

    @Mock
    private VoterRepository voterRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private CustomUserDetails customUserDetails;

    private final Long groupId = 1L;
    private final Long voteId = 1L;
    private final Long memberId = 1L;

    private Vote vote;
    private Member member;
    private Group group;
    private Voter.VoterId voterId;
    private Voter voter;

    @BeforeEach
    void setUp() {
        member = Member.builder().build();
        vote = Vote.builder().groupId(groupId).build();
        group = new Group();

        ReflectionTestUtils.setField(member, "id", memberId);
        ReflectionTestUtils.setField(vote, "id", voteId);
        ReflectionTestUtils.setField(group, "id", groupId);

        voterId = new Voter.VoterId(memberId, voteId);
        voter = new Voter(voterId, member, vote);

        lenient().when(customUserDetails.getUserId()).thenReturn(memberId);
    }

    @Test
    @DisplayName("투표 참여 테스트")
    void addVoter() {
        when(voteRepository.findByIdAndGroupId(voteId, groupId)).thenReturn(Optional.of(vote));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupAndMember(group, member)).thenReturn(true);
        when(voterRepository.existsById(voterId)).thenReturn(false);
        when(voterRepository.save(any(Voter.class))).thenReturn(voter);

        Voter result = voterService.addVoter(groupId, voteId, customUserDetails.getUserId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(voterId);
        verify(voterRepository, times(1)).save(any(Voter.class));
    }

    @Test
    @DisplayName("중복 투표시 예외 발생")
    void addVoter_AlreadyVoted() {
        when(voteRepository.findByIdAndGroupId(voteId, groupId)).thenReturn(Optional.of(vote));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupAndMember(group, member)).thenReturn(true);
        when(voterRepository.existsById(voterId)).thenReturn(true);

        assertThatThrownBy(() -> voterService.addVoter(groupId, voteId, customUserDetails.getUserId()))
                .isInstanceOf(VoterException.class)
                .hasMessageContaining(VoterErrorCode.ALREADY_VOTED.getMessage());

    }

    @Test
    @DisplayName("투표에 참여한 멤버 목록 조회 테스트")
    void getVotersByVote() {
        when(voterRepository.findByIdVoteId(voteId)).thenReturn(Collections.singletonList(voter));

        List<VoterDTO> result = voterService.getVotersByVoteId(voteId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMemberId()).isEqualTo(voter.getId().getMemberId());
        assertThat(result.get(0).getVoteId()).isEqualTo(voter.getId().getVoteId());
    }

    @Test
    @DisplayName("투표 취소 테스트")
    void removeVoter() {
        when(voteRepository.findByIdAndGroupId(voteId, groupId)).thenReturn(Optional.of(vote));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupAndMember(group, member)).thenReturn(true);
        when(voterRepository.existsById(voterId)).thenReturn(true);
        doNothing().when(voterRepository).deleteById(voterId);

        voterService.removeVoter(groupId, voteId, customUserDetails.getUserId());
        verify(voterRepository, times(1)).deleteById(voterId);
    }

    @Test
    @DisplayName("참여하지 않은 투표를 취소할 경우 예외 발생")
    void removeVoter_NotVoted() {
        when(voteRepository.findByIdAndGroupId(voteId, groupId)).thenReturn(Optional.of(vote));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupAndMember(group, member)).thenReturn(true);
        when(voterRepository.existsById(voterId)).thenReturn(false);

        assertThatThrownBy(() -> voterService.removeVoter(groupId, voteId, customUserDetails.getUserId()))
                .isInstanceOf(VoterException.class)
                .hasMessageContaining(VoterErrorCode.NOT_VOTED.getMessage());
    }
}
