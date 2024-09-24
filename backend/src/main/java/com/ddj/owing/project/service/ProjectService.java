package com.ddj.owing.project.service;

import com.ddj.owing.project.domain.Project;
import com.ddj.owing.project.domain.dto.ProjectRequestDto;
import com.ddj.owing.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OpenAiImageApi openAiImageApi;

    public ResponseEntity<String> generateProjectImage(ProjectRequestDto projectRequestDto) {

        String prompt = createPrompt(projectRequestDto);
        String imageUrl = createImage(prompt);
        Project project = projectRequestDto.toEntity(imageUrl);

        projectRepository.save(project);

        return ResponseEntity.ok().body(imageUrl);
    }

    private String createPrompt(ProjectRequestDto projectRequestDto) {

        return String.format(
                "다음 정보에 따라 작품의 표지 이미지를 만드세요. 총 4개의 이미지로, 각 이미지는 인생네컷 스타일로 제작되며, 작품의 분위기와 주요 내용을 시각적으로 표현해야 합니다. 다음은 작품의 정보입니다: " +
                "작품 제목: [%s] \n" +
                "작품 설명: [%s] \n" +
                "분류(Category): [%s] \n" +
                "장르(Genre): [%s] \n\n" +
                "각 이미지는 캐릭터, 배경, 주요 사건, 또는 작품의 분위기를 시각적으로 나타내야 합니다. \n" +
                "첫 번째 이미지는 주인공이나 주요 캐릭터를 중심으로 사실적으로 묘사합니다. \n" +
                "두 번째 이미지는 작품의 핵심 장면이나 스토리 전개를 시각화합니다. \n" +
                "세 번째 이미지는 작품의 전반적인 분위기나 장르적 특성을 반영합니다. \n" +
                "네 번째 이미지는 배경이나 상징적인 이미지를 활용하여 작품의 주제를 나타냅니다. \n" +
                "이미지 스타일: 각 이미지가 인생네컷처럼 서로 연속된 분위기와 색감을 유지해야 하며, 전반적으로 현실적이면서도 작품의 장르와 분류에 맞는 예술적 디테일을 적용하세요. \n" +
                "이미지들은 주로 밝고 선명한 색감을 사용하되, 장르나 분류에 따라 어두운 색상도 적절히 혼합하세요. \n\n" +
                "제작된 표지 이미지는 독자의 관심을 끌 수 있도록 세밀하고 몰입감 있게 표현해주세요.",
                projectRequestDto.title(),
                projectRequestDto.description(),
                projectRequestDto.category(),
                projectRequestDto.genre()
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
