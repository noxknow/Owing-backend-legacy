package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.story.model.StoryPlotNode;
import com.ddj.owing.global.entity.BaseTimeGraph;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Node("Cast")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CastingNode extends BaseTimeGraph {
    @Id
    @NotNull
    private Long id;
    private String name;
    private Long age;
    private String gender;
    private String role;
    private String imageUrl;
    private Integer coordX;
    private Integer coordY;

    @Relationship(type = "CONNECTION", direction = Relationship.Direction.OUTGOING)
    private Set<CastingRelationship> outConnections;

    @Relationship(type = "BI_CONNECTION", direction = Relationship.Direction.OUTGOING)
    private Set<CastingRelationship> outBiConnections;

    @Relationship(type = "BI_CONNECTION", direction = Relationship.Direction.INCOMING)
    private Set<CastingRelationship> inBiConnections;

    @Relationship(type = "APPEARED",  direction = Relationship.Direction.OUTGOING)
    private Set<StoryPlotNode> episodes;

    @Relationship(type = "INCLUDED", direction = Relationship.Direction.INCOMING)
    private ProjectNode projectNode;

    public void linkProjectNode(ProjectNode projectNode) {
        this.projectNode = projectNode;
    }

    public void addConnection(String uuid, CastingNode targetCastingNode, String label, String sourceHandleStr, String targetHandleStr) {
        if (ObjectUtils.isEmpty(targetCastingNode)) {
            throw CastingException.of(CastingErrorCode.INVALID_ARGS_FOR_UPDATE);
        }

        CastingRelationship outConnection = new CastingRelationship(
                uuid, label, targetCastingNode,
                this.id, sourceHandleStr,
                targetCastingNode.getId(), targetHandleStr
        );
        this.outConnections.add(outConnection);
    }

    public void addBiConnection(String uuid, CastingNode targetCastingNode, String label, String sourceHandleStr, String targetHandleStr) {
        if (ObjectUtils.isEmpty(targetCastingNode)) {
            throw CastingException.of(CastingErrorCode.INVALID_ARGS_FOR_UPDATE);
        }

        CastingRelationship outBiConnection = new CastingRelationship(
                uuid, label, targetCastingNode,
                this.id, sourceHandleStr,
                targetCastingNode.getId(), targetHandleStr
        );
        CastingRelationship inBiConnection = new CastingRelationship(
                uuid, label, this,
                this.id, sourceHandleStr,
                targetCastingNode.getId(), targetHandleStr
        );

        this.outBiConnections.add(outBiConnection);
        targetCastingNode.inBiConnections.add(inBiConnection);
    }

    @Deprecated
    public void updateConnectionName(String connectionId, CastingNode targetNode, String label) {
        this.getOutConnections().stream()
                .filter(connection -> connection.getUuid().equals(connectionId))
                .forEach(target -> target.updateLabel(label));
        targetNode.getOutConnections().stream()
                .filter(connection -> connection.getUuid().equals(connectionId))
                .forEach(target -> target.updateLabel(label));
    }

    public void updateInfo(String name, Long age, String gender, String role, String imageUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    public void updateCoord(Integer coordX, Integer coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }
}
