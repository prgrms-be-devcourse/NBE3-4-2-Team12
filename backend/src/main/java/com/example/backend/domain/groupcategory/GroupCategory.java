package com.example.backend.domain.groupcategory;

import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GroupCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public GroupCategory(Group group, Category category) {
        this.group = group;
        this.category = category;
    }
}
