package com.ddj.owing.domain.story.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
public class StoryPlot extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private Integer position;

	@Column(columnDefinition = "int default 0")
	private int textCount;

	@ManyToOne
	@JoinColumn(name = "story_folder_id")
	private StoryFolder storyFolder;

	@OneToMany(mappedBy = "storyPlot", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoryBlock> storyBlocks = new ArrayList<>();

	@Builder
	StoryPlot(String name, String description, Integer position, int textCount, StoryFolder storyFolder) {
		this.name = name;
		this.description = description;
		this.position = position;
		this.textCount = textCount;
		this.storyFolder = storyFolder;
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void updatePosition(Integer position) {
		this.position = position;
	}

	public void updateFolder(StoryFolder newFolder) {
		this.storyFolder = newFolder;
	}

	public void updateTextCount(int textCount) {
		this.textCount += textCount;
	}
}
