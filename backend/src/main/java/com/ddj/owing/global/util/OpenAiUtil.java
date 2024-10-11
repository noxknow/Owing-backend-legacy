package com.ddj.owing.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.stereotype.Component;

import com.ddj.owing.domain.casting.model.dto.casting.CastingRequestDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingSummaryDto;
import com.ddj.owing.domain.project.model.dto.ProjectRequestDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotConflictCheckDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFileRequestDto;
import com.ddj.owing.global.error.code.OpenAiErrorCode;
import com.ddj.owing.global.error.exception.OpenAiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAiUtil {

	private final ImageModel imageModel;
	private final ChatModel chatModel;
	private final ObjectMapper objectMapper;

	/**
	 * OpenAI API 를 이용해 이미지를 생성하는 메서드
	 *
	 * @param prompt 생성할 이미지에 대한 설명
	 * @return 생성된 이미지 URL
	 */
	public String createImage(String prompt) {

		ImageMessage imageMessage = new ImageMessage(prompt, 1.0f);
		OpenAiImageOptions imageOptions = imageOptions();

		ImagePrompt imagePrompt = new ImagePrompt(imageMessage, imageOptions);
		ImageResponse response = imageModel.call(imagePrompt);

		if (response.getResults() != null && !response.getResults().isEmpty()) {
			return response.getResult().getOutput().toString();
		} else {
			throw OpenAiException.of(OpenAiErrorCode.IMAGE_GENERATION_FAIL);
		}
	}

	/**
	 * OpenAI API를 사용해 원고에 출연한 캐릭터를 추출하는 메서드
	 *
	 * @param prompt 원고와 캐릭터 정보가 담긴 프롬프트
	 * @return 추출된 캐릭터 요약 정보
	 */
	public List<CastingSummaryDto> extractCast(Prompt prompt) {
		ChatResponse chatResponse = chatModel.call(prompt);
		String extractResult = chatResponse.getResult().getOutput().getContent();
		try {
			Map<String, ArrayList<CastingSummaryDto>> mappedResponse =
				objectMapper.readValue(extractResult, new TypeReference<>() {
				});

			return mappedResponse.values().stream().findFirst()
				.orElseThrow(() -> OpenAiException.of(OpenAiErrorCode.CASTING_EXTRACT_FAIL));

		} catch (JsonProcessingException e) {
			throw OpenAiException.of(OpenAiErrorCode.CASTING_PARSE_FAIL);
		}
	}

	public String checkStoryConflict(Prompt prompt) {
		ChatResponse chatResponse = chatModel.call(prompt);
		String content = chatResponse.getResult().getOutput().getContent();
		return content;
	}

	/**
	 * CastingRequestDto 를 기반으로 캐릭터 설명 프롬프트를 생성하는 메서드
	 *
	 * @param castingRequestDto 캐릭터 정보가 담긴 DTO
	 * @return 생성된 캐릭터 설명 프롬프트
	 */
	public String createPrompt(CastingRequestDto castingRequestDto) {

		return String.format(
			"다음 정보에 따라 자세한 인물 설명을 만드세요: 인물 이름: [%s] \n" +
				"나이: [%d] \n" +
				"성별: [%s] \n" +
				"직업/역할: [%s] \n" +
				"제공된 세부 정보: [%s] \n" +
				"특성: - 스타일: 사실적인 디테일이 있는 인물 \n" +
				"- 배경: 사실적이고 인물 직업 또는 역할과 관련이 있어야 합니다. \n" +
				"인물의 외모, 성격, 주변 환경에 초점을 맞춰 이러한 세부 사항을 통합한 설명 텍스트를 생생하고 몰입감 있는 인물 콘셉트를 만들어 주세요. \n" +
				"이때, 1개의 이미지만 나오도록 해주세요. \n" +
				"그리고 인물의 상반신이 사진을 대부분 차지하도록 출력해주세요.\n",
			castingRequestDto.name(),
			castingRequestDto.age(),
			castingRequestDto.gender(),
			castingRequestDto.role(),
			castingRequestDto.detail()
		);
	}

	/**
	 * ProjectRequestDto 를 기반으로 작품 표지 이미지를 위한 프롬프트를 생성하는 메서드
	 *
	 * @param projectRequestDto 작품 정보가 담긴 DTO
	 * @return 생성된 작품 표지 이미지 프롬프트
	 */
	public String createPrompt(ProjectRequestDto projectRequestDto) {

		return String.format(
			"다음 정보에 따라 작품의 표지 이미지를 만드세요. 작품의 분위기와 주요 내용을 시각적으로 표현해야 합니다. " +
				"다음은 작품의 정보입니다: \n" +
				"작품 제목: [%s] \n" +
				"작품 설명: [%s] \n" +
				"분류(Category): [%s] \n" +
				"장르(Genre): [%s] \n\n" +
				"이미지는 작품의 주인공이나 주요 캐릭터를 중심으로, 핵심 장면이나 스토리 전개, 전반적인 분위기, 그리고 작품의 주제를 모두 시각적으로 나타내야 합니다. \n" +
				"배경이나 상징적인 이미지 또한 포함하여, 작품의 주제와 분위기를 강하게 전달하세요. \n" +
				"이미지 스타일: 전반적으로 통일된 분위기와 색감을 유지하며, 현실적이면서도 작품의 장르와 분류에 맞는 예술적 디테일을 적용하세요. \n" +
				"밝고 선명한 색감을 주로 사용하되, 장르나 분류에 따라 어두운 색상도 적절히 혼합하세요. \n\n" +
				"제작된 표지 이미지는 독자의 관심을 끌 수 있도록 세밀하고 몰입감 있게 표현해주세요.",
			projectRequestDto.title(),
			projectRequestDto.description(),
			projectRequestDto.category(),
			projectRequestDto.genres().toString()
		);
	}

	//	/**
	//	 * ProjectRequestDto 를 기반으로 인생네컷 스타일의 4개 이미지로 작품 표지 이미지를 위한 프롬프트를 생성하는 메서드
	//	 *
	//	 * @param projectRequestDto 작품 정보가 담긴 DTO
	//	 * @return 생성된 작품 표지 이미지 프롬프트
	//	 */
	//	public String createPrompt(ProjectRequestDto projectRequestDto) {
	//
	//		return String.format(
	//			"다음 정보에 따라 작품의 표지 이미지를 만드세요. 총 4개의 이미지로, 각 이미지는 인생네컷 스타일로 제작되며, 작품의 분위기와 주요 내용을 시각적으로 표현해야 합니다. 다음은 작품의 정보입니다: "
	//				+
	//				"작품 제목: [%s] \n" +
	//				"작품 설명: [%s] \n" +
	//				"분류(Category): [%s] \n" +
	//				"장르(Genre): [%s] \n\n" +
	//				"각 이미지는 캐릭터, 배경, 주요 사건, 또는 작품의 분위기를 시각적으로 나타내야 합니다. \n" +
	//				"첫 번째 이미지는 주인공이나 주요 캐릭터를 중심으로 사실적으로 묘사합니다. \n" +
	//				"두 번째 이미지는 작품의 핵심 장면이나 스토리 전개를 시각화합니다. \n" +
	//				"세 번째 이미지는 작품의 전반적인 분위기나 장르적 특성을 반영합니다. \n" +
	//				"네 번째 이미지는 배경이나 상징적인 이미지를 활용하여 작품의 주제를 나타냅니다. \n" +
	//				"이미지 스타일: 각 이미지가 인생네컷처럼 서로 연속된 분위기와 색감을 유지해야 하며, 전반적으로 현실적이면서도 작품의 장르와 분류에 맞는 예술적 디테일을 적용하세요. \n" +
	//				"이미지들은 주로 밝고 선명한 색감을 사용하되, 장르나 분류에 따라 어두운 색상도 적절히 혼합하세요. \n\n" +
	//				"제작된 표지 이미지는 독자의 관심을 끌 수 있도록 세밀하고 몰입감 있게 표현해주세요.",
	//			projectRequestDto.title(),
	//			projectRequestDto.description(),
	//			projectRequestDto.category(),
	//			projectRequestDto.genres().toString()
	//		);
	//	}

	/**
	 * UniverseFileRequestDto 를 기반으로 작품 세계관 일러스트레이션 이미지를 위한 프롬프트를 생성하는 메서드
	 *
	 * @param universeFileRequestDto 작품 정보가 담긴 DTO
	 * @return 생성된 작품 세계관 일러스트레이션 프롬프트
	 */
	public String createPrompt(UniverseFileRequestDto universeFileRequestDto) {

		return String.format(
			"다음 정보에 따라 작품의 표지 일러스트레이션 이미지를 만드세요. 작품의 분위기와 주요 내용을 시각적으로 표현해야 합니다. 다음은 작품의 정보입니다 " +
				"작품 제목: [%s] \n" +
				"작품 설명: [%s] \n" +
				"이미지는 캐릭터, 배경, 주요 사건, 또는 작품의 분위기를 시각적으로 나타내야 합니다. \n" +
				"이미지 스타일: 전반적으로 현실적이면서도 작품의 장르와 분류에 맞는 예술적 디테일을 적용하세요. \n" +
				"이미지는 주로 밝고 선명한 색감을 사용하되, 장르나 분류에 따라 어두운 색상도 적절히 혼합하세요. \n\n" +
				"이미지는 하나이고, 제작된 표지 이미지는 독자의 관심을 끌 수 있도록 세밀하고 몰입감 있게 표현해주세요.",
			universeFileRequestDto.name(),
			universeFileRequestDto.description()
		);
	}

	/**
	 * 원고에 출연한 캐릭터 추출을 위한 프롬프트를 생성하는 메서드
	 *
	 * @param storyPlotText 원고의 내용이 담긴 list
	 * @param castingSummaryList 프로젝트에 포함된 모든 캐릭터의 요약 정보(id, name, gender)
	 * @return 원고에 출연한 캐릭터 추출 프롬프트
	 */
	public Prompt creatPrompt(String storyPlotText, List<CastingSummaryDto> castingSummaryList) {
		String systemContent =
			"아래 <원고>를 꼼꼼히 읽고, <캐릭터 정보>를 참고하여 원고에 출연한 캐릭터의 정보를 JSON 리스트로 작성해주세요. " +
				"각 캐릭터의 id, name, gender 정보를 포함해야 합니다. 출력 양식은 <JSON list 예시>를 참고하세요. " +
				"<JSON list 예시>: \n[ { \"id\": 1, \"name\": \"John Doe\", \"gender\": \"male\" }, { \"id\": 2, \"name\": \"Jane Doe\", \"gender\": \"female\" }, ... ]\n"
				+
				"필수 준수 사항:\n" +
				"1. 출력해야 할 JSON 리스트는 위와 같은 형식을 따라야 한다.\n" +
				"2. 원고에 등장한 캐릭터만 JSON 리스트에 포함되어야 한다.\n" +
				"3. 캐릭터가 여러 번 등장하더라도 JSON 리스트에는 중복되지 않게 하나만 포함한다.\n" +
				"4. 원고에 등장하는 캐릭터가 없다면, 빈 JSON 리스트 ([])를 반환한다.";

		String userContent = String.format(
			"<원고>: \n[%s]\n" +
				"<캐릭터 정보>: \n[%s]\n" +
				storyPlotText,
			castingSummaryList.toString()
		);

		SystemMessage systemMessage = new SystemMessage(systemContent);
		UserMessage userMessage = new UserMessage(userContent);
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage), chatOptions());
		return prompt;
	}

	/**
	 * 설정오류 검사를 위한 프롬프트를 생성하는 메서드
	 * @param storyPlotConflictCheckDto 기존 내용과, 검사 대상 내용이 들어있는 Dto
	 * @return
	 */
	public Prompt createPrompt(StoryPlotConflictCheckDto storyPlotConflictCheckDto) {
		String systemContent1 =
			"이야기 속 설정이나 서사적 흐름에서 발생할 수 있는 **모순**이나 **일관성 부족**을 확인하세요. 다음의 다양한 측면에서 설정 오류를 분석하고 설명해보세요:\n" +
				"1. **등장인물의 성격 및 행동**:  \n" +
				"   등장인물의 성격, 동기, 또는 과거 행동과 **일관성이 있는지** 확인해보세요. 이야기 속에서 갑작스러운 성격 변화나 행동 변화가 있다면, 그것이 충분히 설명되었는지 분석하세요.\n"
				+
				"   - 등장인물의 성격이나 동기와 충돌하는 행동은 없는가?\n" +
				"   - 갑작스러운 행동 변화가 이야기 흐름에 자연스럽게 맞아떨어지는가?\n" +
				"2. **시간의 흐름과 사건의 연속성**:  \n" +
				"   이야기의 **시간적 흐름**이나 사건들이 **논리적으로 연결**되는지 확인하세요. 시간 순서가 혼란스럽거나, 사건 간에 앞뒤가 맞지 않는 경우를 찾아보세요.\n" +
				"   - 사건 간의 시간 흐름에 모순이 있는가?\n" +
				"   - 특정 사건이 일어난 후, 그 사건에 따른 영향을 충분히 묘사하고 있는가?\n" +
				"3. **세계관과 설정의 일관성**:  \n" +
				"   이야기의 배경, 세계관, 또는 물리적 법칙이 처음에 제시된 대로 **일관성** 있게 유지되고 있는지 확인하세요. 이야기의 세계관이나 규칙이 중간에 변하거나 모순되는 부분을 찾아보세요.\n"
				+
				"   - 이야기 초반에 설정된 세계의 규칙이 중간에 바뀌지는 않았는가?\n" +
				"   - 이야기 속 세계관에서 가능한 행동과 불가능한 행동이 명확하게 구분되고 있는가?\n" +
				"4. **사건과 결과 간의 논리성**:  \n" +
				"   특정 사건이 발생했을 때, 그 사건의 결과가 **논리적으로 설명**되는지 확인하세요. 사건과 그 결과 간에 불합리하거나 지나치게 비현실적인 전개는 없는지 검토하세요.\n" +
				"   - 사건에 대한 결과가 지나치게 부자연스럽거나 비현실적인가?\n" +
				"   - 사건의 원인과 결과가 논리적으로 맞아떨어지는가?\n" +
				"5. **등장인물 간의 관계 변화**:  \n" +
				"   등장인물 간의 관계가 처음에 설정된 대로 **일관성 있게** 발전하고 있는지 확인하세요. 관계 변화가 충분한 설명 없이 급격하게 변하는 경우를 찾아보세요.\n" +
				"   - 등장인물 간의 관계 변화가 적절히 설명되었는가?\n" +
				"   - 관계가 급격히 바뀌거나 이해할 수 없는 행동이 발생했는가?\n" +
				"6. **정보의 전달과 이해**:  \n" +
				"   이야기 속에서 등장인물들이 공유하고 있는 정보가 **일관성 있게 전달**되고 있는지 확인하세요. 특정 등장인물이 갑자기 알 수 없는 정보를 알고 있거나, 설명되지 않은 설정이 드러나는 경우를 찾아보세요.\n"
				+
				"   - 등장인물이 알아야 할 정보를 알지 못하거나, 알지 말아야 할 정보를 알고 있는가?\n" +
				"   - 이야기 속에 등장하는 중요한 정보가 적절하게 설명되고 있는가?\n" +
				"7. **복선과 결말의 연결**:  \n" +
				"   이야기 초반에 제공된 **복선**이나 힌트가 결말에서 **일관성 있게 회수**되는지 확인하세요. 복선이 무시되거나 결말과 맞지 않게 처리된 경우를 찾아보세요.\n" +
				"   - 초반에 제시된 복선이 결말에 적절히 반영되었는가?\n" +
				"   - 복선이 적절히 회수되지 않고 남겨진 경우는 없는가?";

		String systemContent2 =
			"아래에 주어지는 <기존 내용>을 천천히 그리고 자세하게 읽은 후, <추가 내용>에서 기존 내용과 모순되거나 일관성이 없는 부분을 찾아주세요.\n" +
				"작업 지침:\n" +
				"1. <기존 내용>과 <추가 내용>을 주의 깊게 읽습니다.\n" +
				"2. <추가 내용>에서 <기존 내용>과 충돌하는 설정을 찾아냅니다.\n" +
				"3. 각 설정 오류에 대해 다음의 JSON 리스트 형식으로 답변합니다. 만약 설정 오류가 없다면 빈 JSON 리스트 []를 반환합니다.\n" +
				"JSON 리스트 형식:\n" +
				"[\n" +
				"  {\n" +
				"    \"base\": \"기존 내용에서 관련된 문장 또는 구절\",\n" +
				"    \"add\": \"추가되는 내용에서 문제의 문장 또는 구절\",\n" +
				"    \"reason\": \"오류가 발생한 이유를 이해하기 쉽게 설명\"\n" +
				"  },\n" +
				"  // 필요에 따라 추가\n" +
				"]\n" +
				"주의사항:\n" +
				"1. 반드시 JSON 리스트 형식을 정확히 지켜주세요.\n" +
				"2. 설정 오류가 발생한 이유를 명확하고 상세하게 설명해주세요.\n" +
				"3. 불필요한 설명이나 분석은 제외하고, 요구된 형식에 따라 답변해주세요.";

		String userContent = String.format(
			"<기존 내용>\n[%s]" +
				"\n\n" +
				"<추가 내용>\n[%s]",
			storyPlotConflictCheckDto.baseStory(),
			storyPlotConflictCheckDto.targetStory()
		);

		SystemMessage systemMessage1 = new SystemMessage(systemContent1);
		SystemMessage systemMessage2 = new SystemMessage(systemContent2);
		UserMessage userMessage = new UserMessage(userContent);
		Prompt prompt = new Prompt(List.of(systemMessage1, systemMessage2, userMessage), chatOptions());
		return prompt;
	}

	private OpenAiChatOptions chatOptions() {
		return OpenAiChatOptions.builder()
			.withModel("gpt-4o-mini")
			.withTemperature(0.8F)
			.withResponseFormat(new ResponseFormat("json_object"))
			.build();
	}

	private static OpenAiImageOptions imageOptions() {
		return OpenAiImageOptions.builder()
			.withModel(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
			.withResponseFormat("b64_json")
			.withQuality("hd")
			.build();
	}
}
