package com.ddj.owing.global.entity;

import jakarta.persistence.EntityListeners;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeGraph {

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void restore() {
		this.deletedAt = null;
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}
}
