package com.ddj.owing.domain.universe.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.domain.universe.model.dto.UniverseFolderUpdateRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String name;

    @Lob
    @Column
    private String description;

    @OneToMany(mappedBy = "universeFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniverseFile> universeFiles = new ArrayList<>();

    public void updateFolder(UniverseFolderUpdateRequestDto universeFolderUpdateRequestDto) {
        this.name = universeFolderUpdateRequestDto.name();
        this.description = universeFolderUpdateRequestDto.description();
    }
}
