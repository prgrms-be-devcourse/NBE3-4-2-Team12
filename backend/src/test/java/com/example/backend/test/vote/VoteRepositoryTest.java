package com.example.backend.test.vote;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// VoteRepositoryTest.java
@DataJpaTest
class VoteRepositoryTest {
    @Autowired
    private VoteRepository voteRepository;

    private Vote vote;
    private Long groupId = 1L;

    @BeforeEach
    void setUp() {
        vote = Vote.builder()
                .groupId(groupId)
                .location("강남역")
                .address("서울시 강남구")
                .latitude(37.498095)
                .longitude(127.027610)
                .voterCount(0)
                .build();
    }

    @Test
    @DisplayName("투표 저장 테스트")
    void saveTest() {
        // given
        // setUp()에서 생성한 vote 사용

        // when
        Vote savedVote = voteRepository.save(vote);

        // then
        assertNotNull(savedVote.getId());
        assertEquals(vote.getLocation(), savedVote.getLocation());
        assertEquals(vote.getAddress(), savedVote.getAddress());
        assertEquals(vote.getLatitude(), savedVote.getLatitude());
        assertEquals(vote.getLongitude(), savedVote.getLongitude());
    }

    @Test
    @DisplayName("그룹ID로 투표 목록 조회 테스트")
    void findAllByGroupIdTest() {
        // given
        Vote savedVote = voteRepository.save(vote);
        Vote anotherVote = Vote.builder()
                .groupId(groupId)
                .location("역삼역")
                .address("서울시 강남구")
                .latitude(37.500624)
                .longitude(127.036489)
                .voterCount(0)
                .build();
        voteRepository.save(anotherVote);

        // when
        List<Vote> votes = voteRepository.findAllByGroupId(groupId);

        // then
        assertEquals(2, votes.size());
    }

    @Test
    @DisplayName("ID와 그룹ID로 투표 조회 테스트")
    void findByIdAndGroupIdTest() {
        // given
        Vote savedVote = voteRepository.save(vote);

        // when
        Optional<Vote> foundVote = voteRepository.findByIdAndGroupId(savedVote.getId(), groupId);

        // then
        assertTrue(foundVote.isPresent());
        assertEquals(vote.getLocation(), foundVote.get().getLocation());
    }

    @Test
    @DisplayName("투표 HARD DELETE 테스트")
    void deleteTest() {
        // given
        Vote savedVote = voteRepository.save(vote);

        // when
        voteRepository.delete(savedVote);

        // then
        Optional<Vote> deletedVote = voteRepository.findById(savedVote.getId());
        assertTrue(deletedVote.isEmpty());
    }
}