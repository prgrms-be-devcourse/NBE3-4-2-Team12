package com.example.backend.domain.voter.service;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoterService {
    private final VoterRepository voterRepository;
    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    // 특정 투표에 참여한 Voter 목록 조회
    public List<VoterDTO> getVotersByVoteId(Long voteId) {
        List<Voter> voters = voterRepository.findByIdVoteId(voteId);
        return voters.stream()
                .map(VoterDTO::from) // Voter 엔티티를 VoterDTO로 변환
                .collect(Collectors.toList());
    }

    // 그룹내 투표 참여 기능
    @Transactional
    public Voter addVoter(Long groupId, Long voteId, Long memberId) {
        // 투표 및 사용자 조회
        Vote vote = voteRepository.findByIdAndGroupId(voteId, groupId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_VOTE));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_MEMBER));

        // 사용자가 해당 모임에 속해 있는지 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_GROUP));

        boolean isMemberInGroup = groupMemberRepository.existsByGroupAndMember(group, member);
        if (!isMemberInGroup) {
            throw new VoterException(VoterErrorCode.NOT_GROUP_MEMBER);
        }

        // 중복 참여 방지
        Voter.VoterId voterId = new Voter.VoterId(memberId, voteId);
        if (voterRepository.existsById(voterId)) {
            throw new VoterException(VoterErrorCode.ALREADY_VOTED);
        }

        // Voter 엔티티 생성 및 저장
        Voter voter = new Voter(voterId, member, vote);
        return voterRepository.save(voter);
    }

    @Transactional
    public void removeVoter(Long groupId, Long voteId, Long memberId) {
        // 투표 및 사용자 조회
        Vote vote = voteRepository.findByIdAndGroupId(voteId, groupId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_VOTE));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_MEMBER));

        // 사용자가 해당 모임에 속해 있는지 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new VoterException(VoterErrorCode.NOT_FOUND_GROUP));

        boolean isMemberInGroup = groupMemberRepository.existsByGroupAndMember(group, member);
        if (!isMemberInGroup) {
            throw new VoterException(VoterErrorCode.NOT_GROUP_MEMBER);
        }

        // 투표 참여 여부 확인
        Voter.VoterId voterId = new Voter.VoterId(memberId, voteId);
        if (!voterRepository.existsById(voterId)) {
            throw new VoterException(VoterErrorCode.NOT_VOTED);
        }

        // 투표 취소 처리
        voterRepository.deleteById(voterId);
    }

}

