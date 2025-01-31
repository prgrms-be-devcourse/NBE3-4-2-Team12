package com.example.backend.domain.voter.entity;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Voter {

    @EmbeddedId
    private VoterId id; // 복합 키 정의

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voteId") // 복합 키의 voteId를 FK로 매핑
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // 복합 키의 userId를 FK로 매핑
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class VoterId implements Serializable {
        private Long userId;
        private Long voteId;
    }
}
