package com.example.backend.domain.admin.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String admin_name;

    @Column
    private String password;
}
