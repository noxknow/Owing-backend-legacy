package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateDto;
import com.ddj.owing.domain.universe.repository.UniverseFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniverseFolderService {

    private final UniverseFolderRepository universeFolderRepository;

    @Transactional
    public ResponseEntity<Void> createFolder(UniverseFolderCreateDto universeFolderCreateDto) {

        UniverseFolder universeFolder = universeFolderCreateDto.toEntity();
        universeFolderRepository.save(universeFolder);

        return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<UniverseFolder>> getAllFolders() {

        return ResponseEntity.ok(universeFolderRepository.findAll());
    }
}
