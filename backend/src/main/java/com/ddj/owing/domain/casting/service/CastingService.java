package com.ddj.owing.domain.casting.service;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.dto.CastingCreateDto;
import com.ddj.owing.domain.casting.model.dto.CastingDto;
import com.ddj.owing.domain.casting.model.dto.CastingRequestDto;
import com.ddj.owing.domain.casting.model.dto.CastingUpdateDto;
import com.ddj.owing.domain.casting.repository.CastingNodeRepository;
import com.ddj.owing.domain.casting.repository.CastingRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CastingService {

    private final CastingRepository castingRepository;
    private final OpenAiUtil openAiUtil;

    private final CastingNodeRepository castingNodeRepository;

    @Transactional
    public ResponseEntity<String> generateCharacterImage(CastingRequestDto castingRequestDto) {

        String prompt = openAiUtil.createPrompt(castingRequestDto);
        String jsonString = openAiUtil.createImage(prompt);
        String imageUrl = Parser.extractUrl(jsonString);
        Casting casting = castingRequestDto.toEntity(imageUrl);

        castingRepository.save(casting);

        return ResponseEntity.ok().body(imageUrl);
    }

    public CastingDto getCasting(Long id) {
        CastingNode castingNode = castingNodeRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        return CastingDto.from(castingNode);
    }

    public CastingDto getCasting(String name) {
        CastingNode castingNode = castingNodeRepository.findOneByName(name)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        return CastingDto.from(castingNode);
    }

    @Transactional
    public CastingDto createCasting(CastingCreateDto castingCreateDto) {
        Casting casting = castingCreateDto.toEntity();
        Casting savedCasting = castingRepository.save(casting);

        CastingNode castingNode = castingCreateDto.toNodeEntity(savedCasting);
        CastingNode savedCastingNode = castingNodeRepository.save(castingNode);

        return CastingDto.from(savedCastingNode);
    }

    @Transactional
    public CastingDto updateCasting(Long id, CastingUpdateDto castingUpdateDto) {
        Casting casting = castingRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
        casting.update(
                castingUpdateDto.name(),
                castingUpdateDto.age(),
                castingUpdateDto.name(),
                castingUpdateDto.role(),
                castingUpdateDto.detail(),
                castingUpdateDto.imageUrl()
        );

        CastingNode castingNode = castingNodeRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        castingNode.update(
                castingUpdateDto.name(),
                castingUpdateDto.age(),
                castingUpdateDto.name(),
                castingUpdateDto.role(),
                castingUpdateDto.detail(),
                castingUpdateDto.imageUrl()
        );
        CastingNode updatedCastingNode = castingNodeRepository.save(castingNode);

        return CastingDto.from(updatedCastingNode);
    }

    @Transactional
    public void deleteCasting(Long id) {
        // TODO: soft delete
        castingRepository.deleteById(id);
        castingNodeRepository.deleteById(id);
    }
}
