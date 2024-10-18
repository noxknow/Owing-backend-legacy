package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.util.StringUtils;

@RelationshipProperties
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CastingRelationship {
    @RelationshipId
    private Long id;

    private String uuid;
    private String label;
    private Long sourceId;
    private Long targetId;
    private ConnectionHandle sourceHandle;
    private ConnectionHandle targetHandle;

    @TargetNode
    private CastingNode castingNode;

    CastingRelationship(String uuid, String label, CastingNode castingNode, Long sourceId, ConnectionHandle sourceHandle, Long targetId, ConnectionHandle targetHandle) {
        this.uuid = uuid;
        this.label = label;
        this.castingNode = castingNode;

        this.sourceId = sourceId;
        this.sourceHandle = sourceHandle;

        this.targetId = targetId;
        this.targetHandle = targetHandle;
    }

    public void updateLabel(String label) {
        if (!StringUtils.hasText(label)) {
            throw CastingException.of(CastingErrorCode.INVALID_ARGS_FOR_UPDATE);
        }
        this.label = label;
    }
}
