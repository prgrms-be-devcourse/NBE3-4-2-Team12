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
@AllArgsConstructor
@Builder
public class Voter extends BaseEntity {

    @EmbeddedId
    private VoterId id; // 키 정의

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voteId") // 키의 voteId를 FK로 매핑
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId") // 키의 userId를 FK로 매핑
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

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
