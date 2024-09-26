package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.model.Universe;
import com.ddj.owing.domain.universe.model.dto.UniverseRequestDto;
import com.ddj.owing.domain.universe.repository.UniverseRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UniverseService {

    private final UniverseRepository universeRepository;
    private final OpenAiUtil openAiUtil;

    @Transactional
    public ResponseEntity<String> generateUniverseImage(UniverseRequestDto universeRequestDto) {

        String prompt = openAiUtil.createPrompt(universeRequestDto);
        String jsonString = openAiUtil.createImage(prompt);
        String imageUrl = Parser.extractUrl(jsonString);
        Universe universe = universeRequestDto.toEntity(imageUrl);

        universeRepository.save(universe);

        return ResponseEntity.ok().body(imageUrl);
    }


}
