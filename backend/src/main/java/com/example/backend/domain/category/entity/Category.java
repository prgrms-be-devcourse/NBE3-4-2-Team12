package com.example.backend.domain.category.entity;


import com.example.backend.domain.groupcategory.GroupCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"categories\"")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private CategoryType categoryType;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupCategory> groupCategories = new ArrayList<>();

    @Builder
    public Category(String name, CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType;
    }
}
