package com.example.backend.domain.vote.repository;

import com.example.backend.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByGroupId(Long groupId);
    Optional<Vote> findByIdAndGroupId(Long id, Long groupId);
}
