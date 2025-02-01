package com.example.backend.test.vote;

import com.example.backend.domain.group.service.GroupService;
import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import com.example.backend.domain.vote.service.VoteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private GroupService groupService;

    private VoteRequestDto requestDto;
    private Vote vote;
    private Long groupId = 1L;
    private Long voteId = 1L;

    @BeforeEach
    void setUp() {
        requestDto = VoteRequestDto.builder()
                .location("강남역")
                .address("서울시 강남구")
                .latitude(37.498095)
                .longitude(127.027610)
                .build();

        vote = requestDto.toEntity(groupId);
        ReflectionTestUtils.setField(vote, "id", voteId);
    }

    @Test
    @DisplayName("투표 생성 테스트")
    void createVoteTest() {
        // given
        given(voteRepository.save(any())).willReturn(vote);

        // when
        VoteResponseDto response = voteService.createVote(groupId, requestDto);

        // then
        assertThat(response.getLocation()).isEqualTo(requestDto.getLocation());
        assertThat(response.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(response.getLatitude()).isEqualTo(requestDto.getLatitude());
        assertThat(response.getLongitude()).isEqualTo(requestDto.getLongitude());
    }

    @Test
    @DisplayName("투표 조회 테스트")
    void findByIdTest() {
        // given
        given(voteRepository.findByIdAndGroupId(voteId, groupId))
                .willReturn(Optional.of(vote));

        // when
        VoteResponseDto response = voteService.findById(groupId, voteId);

        // then
        assertThat(response.getId()).isEqualTo(voteId);
        assertThat(response.getLocation()).isEqualTo(vote.getLocation());
        assertThat(response.getAddress()).isEqualTo(vote.getAddress());
    }

    @Test
    @DisplayName("투표 수정 테스트")
    void modifyVoteTest() {
        // given
        VoteRequestDto updateRequest = VoteRequestDto.builder()
                .location("역삼역")
                .address("서울시 강남구 역삼동")
                .latitude(37.500624)
                .longitude(127.036489)
                .build();

        Vote updatedVote = updateRequest.toEntity(groupId);
        ReflectionTestUtils.setField(updatedVote, "id", voteId);

        given(voteRepository.findByIdAndGroupId(voteId, groupId))
                .willReturn(Optional.of(vote));
        given(voteRepository.save(any())).willReturn(updatedVote);

        // when
        VoteResponseDto response = voteService.modifyVote(groupId, voteId, updateRequest);

        // then
        assertThat(response.getLocation()).isEqualTo("역삼역");
        assertThat(response.getAddress()).isEqualTo("서울시 강남구 역삼동");
        assertThat(response.getLatitude()).isEqualTo(37.500624);
        assertThat(response.getLongitude()).isEqualTo(127.036489);
    }

    @Test
    @DisplayName("존재하지 않는 투표 조회시 예외 발생 테스트")
    void findByIdNotFoundTest() {
        // given
        given(voteRepository.findByIdAndGroupId(voteId, groupId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> voteService.findById(groupId, voteId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vote not found");
    }

    @Test
    @DisplayName("투표 삭제 테스트")
    void deleteVoteTest() {
        // given
        given(voteRepository.findByIdAndGroupId(voteId, groupId))
                .willReturn(Optional.of(vote));

        // when & then
        // 그냥 메서드를 실행하고, 예외가 발생하지 않으면 테스트 성공
        voteService.deleteVote(groupId, voteId);
    }
}