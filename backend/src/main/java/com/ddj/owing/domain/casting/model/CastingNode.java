package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.global.entity.BaseTimeGraph;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Node("Cast")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Relationship(type = "CONNECTION_BY", direction = Relationship.Direction.OUTGOING)
    private Set<CastingRelationship> outConnections;

    @Relationship(type = "CONNECTION_BY", direction = Relationship.Direction.INCOMING)
    private Set<CastingRelationship> inConnections;

    public void addConnection(CastingNode targetCastingNode, String relationName) {
        if (ObjectUtils.isEmpty(targetCastingNode)) {
            throw CastingException.of(CastingErrorCode.INVALID_ARGS_FOR_UPDATE);
        }

        CastingRelationship outConnection = new CastingRelationship(relationName, targetCastingNode);
        this.outConnections.add(outConnection);

        CastingRelationship inConnection = new CastingRelationship(relationName, this);
        targetCastingNode.inConnections.add(inConnection);
    }

    public void updateConnectionName(Long connectionId, CastingNode targetNode, String relationName) {
        this.getOutConnections().stream()
                .filter(connection -> connection.getId().equals(connectionId))
                .forEach(target -> target.updateName(relationName));
        targetNode.getOutConnections().stream()
                .filter(connection -> connection.getId().equals(connectionId))
                .forEach(target -> target.updateName(relationName));
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
