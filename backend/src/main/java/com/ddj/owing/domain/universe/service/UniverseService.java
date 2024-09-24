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

        String prompt = createPrompt(universeRequestDto);
        String imageUrl = openAiUtil.createImage(prompt);
        Universe universe = universeRequestDto.toEntity(imageUrl);

        universeRepository.save(universe);

        return ResponseEntity.ok().body(imageUrl);
    }

    private String createPrompt(UniverseRequestDto universeRequestDto) {

        return String.format(
                "다음 정보에 따라 작품의 표지 이미지를 만드세요. 작품의 분위기와 주요 내용을 시각적으로 표현해야 합니다. 다음은 작품의 정보입니다: " +
                    "작품 제목: [%s] \n" +
                    "작품 설명: [%s] \n" +
                    "각 이미지는 캐릭터, 배경, 주요 사건, 또는 작품의 분위기를 시각적으로 나타내야 합니다. \n" +
                    "이미지 스타일: 각 이미지가 인생네컷처럼 서로 연속된 분위기와 색감을 유지해야 하며, 전반적으로 현실적이면서도 작품의 장르와 분류에 맞는 예술적 디테일을 적용하세요. \n" +
                    "이미지들은 주로 밝고 선명한 색감을 사용하되, 장르나 분류에 따라 어두운 색상도 적절히 혼합하세요. \n\n" +
                    "제작된 표지 이미지는 독자의 관심을 끌 수 있도록 세밀하고 몰입감 있게 표현해주세요.",
                universeRequestDto.title(),
                universeRequestDto.description()
        );
    }
}
