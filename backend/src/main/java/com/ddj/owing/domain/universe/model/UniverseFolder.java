package com.ddj.owing.domain.universe.model;

import com.ddj.owing.domain.universe.model.dto.UniverseFolderUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SoftDelete
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UniverseFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId; // todo: Project Entity

    @Column
    private String title;

    @Lob
    @Column
    private String description;

    @OneToMany(mappedBy = "universeFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniverseFile> universeFiles = new ArrayList<>();

    public void updateFolder(UniverseFolderUpdateRequestDto universeFolderUpdateRequestDto) {
        this.title = universeFolderUpdateRequestDto.title();
        this.description = universeFolderUpdateRequestDto.description();
    }
}
