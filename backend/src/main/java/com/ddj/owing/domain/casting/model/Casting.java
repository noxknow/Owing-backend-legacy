package com.ddj.owing.domain.casting.model;

import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SoftDelete
public class Casting extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private Long age;

	@Column
	private String gender;

	@Column
	private String role;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String detail;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String coverImage;

	@Column
	private Integer coordX;

	@Column
	private Integer coordY;

	@ManyToOne
	@JoinColumn(name = "casting_folder_id")
	private CastingFolder castingFolder;

	private Integer position;

	public void updateInfo(String name, Long age, String gender, String role, String detail, String coverImage) {
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.role = role;
		this.detail = detail;
		this.coverImage = coverImage;
	}

	public void updateCoord(Integer coordX, Integer coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public void updateFolder(CastingFolder newFolder) {
		this.castingFolder = newFolder;
	}

	public void updatePosition(int newPosition) {
		this.position = newPosition;
	}
}
