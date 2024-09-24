package com.ddj.owing.casting.service;

import com.ddj.owing.casting.dto.CastingRequestDto;
import com.ddj.owing.casting.entity.Casting;
import com.ddj.owing.casting.repository.CastingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastingService {

    private final CastingRepository castingRepository;
    private final OpenAiImageApi openAiImageApi;

    public ResponseEntity<String> generateCharacterImage(CastingRequestDto castingRequestDto) {

        String prompt = createPrompt(castingRequestDto);
        String imageUrl = createImage(prompt);
        Casting casting = castingRequestDto.toEntity(imageUrl);

        castingRepository.save(casting);

        return ResponseEntity.ok().body(imageUrl);
    }

    private String createPrompt(CastingRequestDto castingRequestDto) {

        return String.format(
                "다음 사양에 따라 자세한 캐릭터 설명을 만드세요: 캐릭터 이름: [%s] 나이: [%d] 세 성별: [%s] 직업/역할: [%s] 제공된 세부 정보입니다: [%s] 특성: - 스타일: 사실적인 디테일이 있는 캐릭터형 - 배경: 사실적이고 캐릭터의 직업 또는 역할과 관련이 있어야 합니다. 캐릭터의 외모, 성격, 주변 환경에 초점을 맞춰 이러한 세부 사항을 통합한 설명 텍스트를 생성하여 생생하고 몰입감 있는 캐릭터 콘셉트를 만들어 주세요.",
                castingRequestDto.name(),
                castingRequestDto.age(),
                castingRequestDto.gender(),
                castingRequestDto.role(),
                castingRequestDto.detail()
        );
    }

    private String createImage(String prompt) {

        OpenAiImageApi.OpenAiImageRequest request = new OpenAiImageApi.OpenAiImageRequest(
                prompt,
                OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
                1,
                "standard",
                "url",
                "1024x1024",
                "natural",
                null
        );

        ResponseEntity<OpenAiImageApi.OpenAiImageResponse> response = openAiImageApi.createImage(request);

        if (response.getBody() != null && !response.getBody().data().isEmpty()) {
            return response.getBody().data().getFirst().url();
        } else {
            throw new RuntimeException("Failed to generate image");
        }
    }
}