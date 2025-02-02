package com.example.backend.domain.category.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Category(String name, CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType;
    }
}
