package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.error.code.UniverseFileErrorCode;
import com.ddj.owing.domain.universe.error.exception.UniverseFileException;
import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.dto.UniverseFileRequestDto;
import com.ddj.owing.domain.universe.repository.UniverseFileRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UniverseFileService {

    private final UniverseFileRepository universeFileRepository;
    private final OpenAiUtil openAiUtil;

    @Transactional(readOnly = true)
    public ResponseEntity<UniverseFile> loadUniverseInfo(Long id) {

        UniverseFile universeFile = universeFileRepository.findById(id)
                .orElseThrow(() -> UniverseFileException.of(UniverseFileErrorCode.UNIVERSE_NOT_FOUND));

        return ResponseEntity.ok(universeFile);
    }

    @Transactional
    public ResponseEntity<String> generateUniverseImage(UniverseFileRequestDto universeFileRequestDto) {

        String prompt = openAiUtil.createPrompt(universeFileRequestDto);
        String jsonString = openAiUtil.createImage(prompt);
        String imageUrl = Parser.extractUrl(jsonString);
        UniverseFile universeFile = universeFileRequestDto.toEntity(imageUrl);

        universeFileRepository.save(universeFile);

        return ResponseEntity.ok(imageUrl);
    }
}
