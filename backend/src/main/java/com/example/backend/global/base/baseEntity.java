package com.example.backend.global.base;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * baseEntity
 * 엔티티별 created_at, modified_at을 관리하는 BaseEntity
 * @author 100minha
 */
@Getter
@MappedSuperclass
public class baseEntity {

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

}
