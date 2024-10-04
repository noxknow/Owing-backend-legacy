package com.ddj.owing.domain.story.model;


import com.ddj.owing.global.entity.BaseTimeGraph;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("StoryPlot")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryPlotNode extends BaseTimeGraph {
	@Id
	private Long id;

	private String name;

	public void updateName(String name) {
		this.name = name;
	}
}
