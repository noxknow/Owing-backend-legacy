package com.ddj.owing.domain.story.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class StoryBlock extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "CHAR(36)")
	private String uuid;

	private String type;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private String props;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private List<Content> contents;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private StoryBlock parentBlock;

	@OneToMany(mappedBy = "parentBlock", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoryBlock> children = new ArrayList<>();

	private Integer position;

	@ManyToOne
	@JoinColumn(name = "story_plot_id")
	private StoryPlot storyPlot;

	@Builder
	public StoryBlock(String type, String props, List<Content> contents, StoryBlock parentBlock, Integer position,
		StoryPlot storyPlot) {
		this.type = type;
		this.props = props;
		this.contents = contents;
		this.parentBlock = parentBlock;
		this.position = position;
		this.storyPlot = storyPlot;
	}

	public void update(String type, String props, List<Content> contents) {
		this.type = type;
		this.props = props;
		this.contents = contents;
	}

	public void updatePosition(Integer newPosition) {
		this.position = newPosition;
	}

	public void updateParentBlock(StoryBlock parentBlock) {
		this.parentBlock = parentBlock;
	}
}
