package com.ddj.owing.domain.universe.model;

import com.ddj.owing.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

@Entity
@Builder
@Getter
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UniverseFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universe_folder_id", nullable = false)
    private UniverseFolder universeFolder;

    @Column
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverImage;
}
