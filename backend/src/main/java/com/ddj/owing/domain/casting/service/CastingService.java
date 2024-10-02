package com.ddj.owing.domain.casting.service;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.dto.*;
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

        return ResponseEntity.ok(imageUrl);
    }

    public CastingDto getCasting(Long id) {
        Casting casting = castingRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
        return CastingDto.from(casting);
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
    public CastingDto updateCastingInfo(Long id, CastingInfoUpdateDto castingInfoUpdateDto) {
        Casting casting = castingRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
        casting.updateInfo(
                castingInfoUpdateDto.name(),
                castingInfoUpdateDto.age(),
                castingInfoUpdateDto.name(),
                castingInfoUpdateDto.role(),
                castingInfoUpdateDto.detail(),
                castingInfoUpdateDto.imageUrl()
        );

        CastingNode castingNode = castingNodeRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        castingNode.updateInfo(
                castingInfoUpdateDto.name(),
                castingInfoUpdateDto.age(),
                castingInfoUpdateDto.name(),
                castingInfoUpdateDto.role(),
                castingInfoUpdateDto.imageUrl()
        );
        CastingNode updatedCastingNode = castingNodeRepository.save(castingNode);

        return CastingDto.from(updatedCastingNode);
    }

    @Transactional
    public CastingDto updateCastingCoord(Long id, CastingCoordUpdateDto coordUpdateDto) {
        Casting casting = castingRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
        casting.updateCoord(
                coordUpdateDto.coordX(),
                coordUpdateDto.coordY()
        );

        CastingNode castingNode = castingNodeRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        castingNode.updateCoord(
                coordUpdateDto.coordX(),
                coordUpdateDto.coordY()
        );
        CastingNode updatedCastingNode = castingNodeRepository.save(castingNode);

        return CastingDto.from(updatedCastingNode);
    }

    @Transactional
    public void deleteCasting(Long id) {
        castingRepository.deleteById(id);
        CastingNode castingNode = castingNodeRepository.findById(id)
                .orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
        castingNode.delete();
        castingNodeRepository.save(castingNode);
    }
}
