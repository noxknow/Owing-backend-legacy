package com.ddj.owing.domain.project.model;

import com.ddj.owing.domain.project.model.dto.ProjectUpdateRequestDto;
import com.ddj.owing.global.entity.BaseTimeEntity;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SoftDelete
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

    @Column(name = "genres", columnDefinition = "varchar[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverImage;

    public void update(ProjectUpdateRequestDto updateRequestDto) {
        this.title = updateRequestDto.title();
        this.description = updateRequestDto.description();
        this.category = updateRequestDto.category();
        this.genres = updateRequestDto.genres();
        this.coverImage = updateRequestDto.coverImage();
    }
}
