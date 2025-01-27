package com.example.backend.test.vote;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @DisplayName("투표 저장 테스트")
    void 투표저장테스트(){
        //given
        Vote vote = new Vote();
        vote.setGroupId(1L);
        vote.setLocation("강남역");
        vote.setAddress("서초구 서초동");
        vote.setLatitude(33.333);
        vote.setLongitude(127.333);
        vote.setVoterCount(0);
        //when
        Vote savedVote = voteRepository.save(vote);
        //then
        assertThat(savedVote.getId()).isNotNull();
        assertThat(savedVote.getGroupId()).isEqualTo(1L);
        assertThat(savedVote.getLocation()).isEqualTo("강남역");
        assertThat(savedVote.getAddress()).isEqualTo("서초구 서초동");
    }
}
