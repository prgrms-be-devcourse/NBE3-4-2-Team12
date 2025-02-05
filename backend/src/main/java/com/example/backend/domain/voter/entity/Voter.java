package com.example.backend.domain.voter.entity;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Voter extends BaseEntity {

    @EmbeddedId
    private VoterId id; // 키 정의


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId") // 키의 memberId를 FK로 매핑
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voteId") // 키의 voteId를 FK로 매핑
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    // 생성자 추가
    public Voter(VoterId id, Member member, Vote vote) {
        this.id = id;
        this.member = member;
        this.vote = vote;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class VoterId implements Serializable {
        private Long memberId;
        private Long voteId;
    }
}
