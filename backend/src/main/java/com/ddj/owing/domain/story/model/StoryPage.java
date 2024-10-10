package com.ddj.owing.domain.story.model;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
public class StoryPage extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "story_plot_id")
	private StoryPlot storyPlot;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private List<StoryPageBlock> blocks;

	@Builder
	public StoryPage(StoryPlot storyPlot, List<StoryPageBlock> blocks) {
		this.storyPlot = storyPlot;
		this.blocks = blocks;
	}

	@Getter
	@Builder
	public static class StoryPageBlock {
		private String id;
		private String type;
		private List<StoryPageBlock> children;

		@JdbcTypeCode(SqlTypes.JSON)
		@Column(columnDefinition = "jsonb")
		private List<Content> content;

		@JdbcTypeCode(SqlTypes.JSON)
		@Column(columnDefinition = "jsonb")
		private Map<String, Object> props;

	}

	public void updatePageBlocks(List<StoryPageBlock> blocks) {
		this.blocks = blocks;
	}

}
