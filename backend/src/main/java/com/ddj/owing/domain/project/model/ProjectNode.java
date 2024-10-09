package com.ddj.owing.domain.project.model;

import com.ddj.owing.domain.story.model.StoryPlotNode;
import com.ddj.owing.global.entity.BaseTimeGraph;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectNode extends BaseTimeGraph {

    @Id
    private Long id;
    private String title;

    @Relationship(type = "INCLUDED", direction = Relationship.Direction.OUTGOING)
    private Set<StoryPlotNode> plots = new HashSet<>();

    public ProjectNode(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
