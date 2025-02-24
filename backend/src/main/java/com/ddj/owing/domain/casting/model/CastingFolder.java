package com.ddj.owing.domain.casting.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
public class CastingFolder extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long projectId;

	private String name;
	private String description;
	private Integer position;

	@OneToMany(mappedBy = "castingFolder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Casting> castings = new ArrayList<>();

	@Builder
	CastingFolder(Long projectId, String name, String description, Integer position) {
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.position = position;
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void updatePosition(Integer position) {
		this.position = position;
	}
}
