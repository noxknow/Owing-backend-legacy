package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import jakarta.persistence.UniqueConstraint;
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
    private String name;
    private ConnectionHandle sourceHandle;
    private ConnectionHandle targetHandle;

    @TargetNode
    private CastingNode castingNode;

    CastingRelationship(String uuid, String name, CastingNode castingNode, String sourceHandleStr, String targetHandleStr) {
        this.uuid = uuid;
        this.name = name;
        this.castingNode = castingNode;
        this.sourceHandle = ConnectionHandle.of(sourceHandleStr);
        this.targetHandle = ConnectionHandle.of(targetHandleStr);
    }

    public void updateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw CastingException.of(CastingErrorCode.INVALID_ARGS_FOR_UPDATE);
        }
        this.name = name;
    }
}
