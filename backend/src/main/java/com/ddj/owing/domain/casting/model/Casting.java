package com.ddj.owing.domain.casting.model;

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
}
