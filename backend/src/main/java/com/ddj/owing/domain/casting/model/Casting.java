package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.model.dto.CastingUpdateDto;
import com.ddj.owing.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Casting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long age;

    @Column
    private String gender;

    @Column
    private String role;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String detail;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverImage;

    public void update(String name, Long age, String gender, String role, String detail, String coverImage) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.detail = detail;
        this.coverImage = coverImage;
    }

}
