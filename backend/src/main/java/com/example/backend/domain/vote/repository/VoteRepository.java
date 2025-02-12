package com.example.backend.domain.vote.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.domain.vote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	List<Vote> findAllByGroupId(Long groupId);

	Optional<Vote> findByIdAndGroupId(Long id, Long groupId);

	@Query("SELECT v.id FROM Vote v WHERE v.groupId = :groupId")
	List<Long> findAllIdByGroupId(@Param("groupId") Long groupId);

	void deleteAllByGroupId(Long groupId);
}
