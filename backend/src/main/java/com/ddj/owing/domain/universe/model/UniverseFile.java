package com.ddj.owing.domain.universe.model;

import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UniverseFile extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "universe_folder_id", nullable = false)
	private UniverseFolder universeFolder;

	@Column
	private String name;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String imageUrl;

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
