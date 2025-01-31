package com.example.backend.domain.group.repository;

import com.example.backend.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<List<Group>> findByOwnerId(Long ownerId);
}
