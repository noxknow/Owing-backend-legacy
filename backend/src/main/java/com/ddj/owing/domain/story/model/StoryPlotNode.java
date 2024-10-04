package com.ddj.owing.domain.story.model;

import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.global.entity.BaseTimeGraph;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Node("StoryPlot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryPlotNode extends BaseTimeGraph {
	@Id
	private Long id;

	private String name;

	@Relationship(type = "APPEARED", direction = Relationship.Direction.INCOMING)
	private Set<CastingNode> casts = new HashSet<>();

	public StoryPlotNode(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public void addCasts(List<CastingNode> casts) {
		this.casts.addAll(casts);
	}

	public void updateName(String name) {
		this.name = name;
	}
}
