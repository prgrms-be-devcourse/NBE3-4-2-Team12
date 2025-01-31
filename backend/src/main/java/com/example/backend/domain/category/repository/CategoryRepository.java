package com.example.backend.domain.category.repository;

import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
