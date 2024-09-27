package com.ddj.owing.domain.casting.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Node("Cast")
public class CastingNode {
    @Id
    @NotNull
    private Long id;
    private String name;
    private Long age;
    private String gender;
    private String role;
    private String detail;
    private String imageUrl;

    @Builder
    public CastingNode(Long id, String name, Long age, String gender, String role, String detail, String imageUrl) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.detail = detail;
        this.imageUrl = imageUrl;
    }

    public void update(String name, Long age, String gender, String role, String detail, String imageUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.detail = detail;
        this.imageUrl = imageUrl;
    }
}
