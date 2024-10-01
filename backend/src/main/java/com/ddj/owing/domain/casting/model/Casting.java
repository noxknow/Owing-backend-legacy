package com.ddj.owing.domain.casting.model;

import com.ddj.owing.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SoftDelete
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

    @Column
    private Integer coordX;

    @Column
    private Integer coordY;

    public void updateInfo(String name, Long age, String gender, String role, String detail, String coverImage) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.detail = detail;
        this.coverImage = coverImage;
    }

    public void updateCoord(Integer coordX, Integer coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

}
