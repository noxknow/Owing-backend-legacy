package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.model.Universe;
import com.ddj.owing.domain.universe.model.dto.UniverseRequestDto;
import com.ddj.owing.domain.universe.repository.UniverseRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniverseService {

    private final UniverseRepository universeRepository;
    private final OpenAiUtil openAiUtil;

    public ResponseEntity<String> generateUniverseImage(UniverseRequestDto universeRequestDto) {

        String prompt = openAiUtil.createPrompt(universeRequestDto);
        String imageUrl = openAiUtil.createImage(prompt);
        Universe universe = universeRequestDto.toEntity(imageUrl);

        universeRepository.save(universe);

        return ResponseEntity.ok().body(imageUrl);
    }
}
