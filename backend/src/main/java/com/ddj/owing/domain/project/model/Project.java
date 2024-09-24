package com.ddj.owing.domain.project.model;

import com.ddj.owing.global.entity.BaseTimeEntity;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Category category;

    @Column
    private Genre genre;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverImage;
}
