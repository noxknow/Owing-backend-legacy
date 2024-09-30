package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateDto;
import com.ddj.owing.domain.universe.repository.UniverseFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UniverseFolderService {

    private final UniverseFolderRepository universeFolderRepository;

    @Transactional
    public ResponseEntity<String> createFolder(UniverseFolderCreateDto universeFolderCreateDto) {

        UniverseFolder universeFolder = universeFolderCreateDto.toEntity();
        universeFolderRepository.save(universeFolder);

        return ResponseEntity.ok("success");
    }
}
