package com.ddj.owing.domain.story.service;

import java.util.*;

import com.ddj.owing.domain.casting.model.dto.casting.CastingSummaryDto;
import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.domain.project.error.exception.ProjectException;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.project.repository.ProjectNodeRepository;
import com.ddj.owing.domain.story.repository.StoryPageRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.repository.CastingNodeRepository;
import com.ddj.owing.domain.story.error.code.StoryFolderErrorCode;
import com.ddj.owing.domain.story.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.story.error.exception.StoryFolderException;
import com.ddj.owing.domain.story.error.exception.StoryPlotException;
import com.ddj.owing.domain.story.model.StoryFolder;
import com.ddj.owing.domain.story.model.StoryPlot;
import com.ddj.owing.domain.story.model.StoryPlotNode;
import com.ddj.owing.domain.story.model.dto.StoryPlotAppearedCastDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotAppearedCastCreateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotCreateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotPositionUpdateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotUpdateDto;
import com.ddj.owing.domain.story.repository.StoryFolderRepository;
import com.ddj.owing.domain.story.repository.StoryPlotNodeRepository;
import com.ddj.owing.domain.story.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryPlotService {
	private final StoryPageRepository storyPageRepository;
	private final StoryPlotRepository storyPlotRepository;
	private final StoryFolderRepository storyFolderRepository;
	private final StoryPlotNodeRepository storyPlotNodeRepository;
	private final CastingNodeRepository castingNodeRepository;
	private final ProjectNodeRepository projectNodeRepository;
	private final OpenAiUtil openAiUtil;

	private StoryPlot findById(Long id) {
		return storyPlotRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
	}

	public List<StoryPlotDto> getStoryPlotList(Long folderId) {
		// todo: permission
		return storyPlotRepository.findByStoryFolderIdOrderByPositionAsc(folderId)
			.stream()
			.map(StoryPlotDto::from)
			.toList();
	}

	public StoryPlotDto getStoryPlot(Long id) {
		StoryPlot plot = findById(id);
		return StoryPlotDto.from(plot);
	}

	@Transactional
	public StoryPlotDto createStoryPlot(StoryPlotCreateDto storyPlotCreateDto) {
		StoryFolder storyFolder = storyFolderRepository.findById(storyPlotCreateDto.folderId())
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));

		Integer position = storyPlotRepository.findMaxOrderByStoryFolderId(storyPlotCreateDto.folderId()) + 1;

		StoryPlot storyPlot = storyPlotCreateDto.toEntity(storyFolder, position);
		StoryPlot savedStoryPlot = storyPlotRepository.save(storyPlot);

		ProjectNode projectNode = projectNodeRepository.findById(storyFolder.getProjectId())
				.orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NODE_NOT_FOUND));

		StoryPlotNode storyPlotNode = storyPlotCreateDto.toNode(savedStoryPlot.getId());
		storyPlotNode.linkProject(projectNode);

		storyPlotNodeRepository.save(storyPlotNode);

		return StoryPlotDto.from(savedStoryPlot);
	}

	@Transactional
	public StoryPlotDto updateStoryPlot(Long id, StoryPlotUpdateDto storyPlotUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		StoryPlot storyPlot = findById(id);
		storyPlot.update(storyPlotUpdateDto.name(), storyPlotUpdateDto.description());

		StoryPlotNode storyPlotNode = storyPlotNodeRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NODE_NOT_FOUND));
		storyPlotNode.updateName(storyPlotUpdateDto.name());
		storyPlotNodeRepository.save(storyPlotNode);

		return StoryPlotDto.from(storyPlotRepository.save(storyPlot));
	}

	@Transactional
	public void deleteStoryPlot(Long id) {
		StoryPlot storyPlot = findById(id);
		storyPlotRepository.decrementPositionAfter(storyPlot.getPosition(), storyPlot.getStoryFolder().getId());
		storyPlotRepository.deleteById(id);

		storyPlotNodeRepository.findById(id).ifPresentOrElse(
			(node) -> {
				node.delete();
				storyPlotNodeRepository.save(node);
			},
			() -> log.warn("StoryPlot 데이터 불일치 발생. entity id:{}", storyPlot.getId())
		);
	}

	@Transactional
	public StoryPlotDto updateStoryPlotPosition(Long id, StoryPlotPositionUpdateDto storyPlotPositionUpdateDto) {
		StoryPlot storyPlot = findById(id);

		StoryFolder oldFolder = storyPlot.getStoryFolder();
		StoryFolder newFolder = storyFolderRepository.findById(storyPlotPositionUpdateDto.folderId())
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));

		int oldPosition = storyPlot.getPosition();
		int newPosition = storyPlotPositionUpdateDto.position();

		if (oldFolder.getId().equals(newFolder.getId()) && oldPosition == newPosition) {
			return StoryPlotDto.from(storyPlot);
		}

		if (newPosition < 1 || newPosition > newFolder.getStoryPlots().size() + 1) {
			throw StoryPlotException.of(StoryPlotErrorCode.INVALID_POSITION);
		}

		if (oldFolder.getId().equals(newFolder.getId())) {
			if (oldPosition < newPosition) {
				storyPlotRepository.decrementPositionBetween(oldPosition, newPosition, oldFolder.getId());
			} else {
				storyPlotRepository.incrementPositionBetween(newPosition, oldPosition - 1, oldFolder.getId());
			}
		} else {
			storyPlotRepository.decrementPositionAfter(oldPosition, oldFolder.getId());
			storyPlotRepository.incrementPositionAfter(newPosition, newFolder.getId());
			storyPlot.updateFolder(newFolder);
		}

		storyPlot.updatePosition(newPosition);
		return StoryPlotDto.from(storyPlotRepository.save(storyPlot));
	}

	@Transactional
	public List<StoryPlotAppearedCastDto> registerCasts(Long storyPlotId,
		StoryPlotAppearedCastCreateDto appearedCastCreateDto) {
		StoryPlotNode storyPlotNode = storyPlotNodeRepository.findById(storyPlotId)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NODE_NOT_FOUND));

		Set<CastingNode> existsCasts = new HashSet<>();
		for (Long castId : appearedCastCreateDto.castIdList()) {
			Optional<CastingNode> optionalCast = castingNodeRepository.findById(castId);
			optionalCast.ifPresent(cast -> {
				if (!cast.getEpisodes().contains(storyPlotNode)) {
					existsCasts.add(cast);
				}
			});
		}

		storyPlotNode.addCasts(existsCasts);
		storyPlotNodeRepository.save(storyPlotNode);

		return existsCasts.stream()
			.map(cast -> new StoryPlotAppearedCastDto(cast.getId(), cast.getName()))
			.toList();
	}

	// TODO 등장인물 추출
	public List<CastingSummaryDto> extractCasts(Long storyPlotId) {
		StoryPlot storyPlot = storyPlotRepository.findById(storyPlotId)
				.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
		Long projectId = storyPlot.getStoryFolder().getProjectId();
		List<CastingSummaryDto> castingSummaryList = castingNodeRepository.findAllSummaryByProjectId(projectId);


		// TODO storyPlot에서 본문만 추출하여 storyPlotTextList에 저장
//		String storyPlotTextList = storyPageRepository.findAllTextListByStoryPlotId(storyPlotId);
		String storyPlotTextList = temp();
		Prompt castExtractPrompt = openAiUtil.creatPrompt(storyPlotTextList, castingSummaryList);
		List<CastingSummaryDto> extractedCastSummaryList = openAiUtil.extractCast(castExtractPrompt);

		return extractedCastSummaryList;
	}

	public String temp() {
		return "제1장: 바람의 전조\n" +
				"\n" +
				"해 질 무렵의 마을은 평화로웠다. 들판을 가로질러 불어오는 바람이 길가의 나무를 흔들었고, 집집마다 굴뚝에서 나오는 연기가 저 멀리 하늘로 퍼져나갔다. 루카스 하퍼는 마을의 외곽에 자리 잡은 오래된 언덕에서 마을을 내려다보고 있었다. 그가 이 마을에 묶인 지는 몇 년이 지났지만, 그의 마음속에는 늘 불안감이 자리 잡고 있었다.\n" +
				"\n" +
				"어릴 적 부모를 잃고 혼자가 된 그는 마을 사람들이 베풀어준 따뜻함 덕에 살아남았다. 하지만 그것이 자신을 이곳에 가둔 족쇄처럼 느껴졌다. 마을을 떠나고 싶다는 생각은 종종 그의 머릿속을 맴돌았지만, 그는 자신의 마음속에서 끊임없이 싸우고 있었다. 책임감과 의무감이 그를 이곳에 붙잡고 있었기 때문이다.\n" +
				"\n" +
				"\"생각에 잠긴 거야?\"\n" +
				"\n" +
				"익숙한 목소리가 그의 귀에 들려왔다. 루카스는 고개를 돌려 엘레나 윈터스가 그의 뒤에 서 있는 것을 발견했다. 엘레나는 언제나 그렇듯 밝은 표정이었다. 마을에서 그녀는 누구나 사랑하는 존재였다. 늘 친절하고, 다른 사람들의 기분을 잘 살피는 엘레나는 루카스가 의지하는 몇 안 되는 사람이었다.\n" +
				"\n" +
				"\"언제나 그렇지.\" 루카스는 어깨를 으쓱하며 말했다. \"여기 있으면 가끔 내가 이 마을에 갇혀 있다는 기분이 들어.\"\n" +
				"\n" +
				"엘레나는 잠시 그를 바라보다가 조용히 옆으로 다가와 서서, 그의 시선을 따라 마을을 내려다보았다. 그녀의 얼굴에 살짝 그늘이 드리워졌지만 곧 평소처럼 미소를 되찾았다.\n" +
				"\n" +
				"\"그럴 때도 있지.\" 그녀는 나지막이 말했다. \"하지만 네가 어디에 있든 결국 네가 선택한 길이잖아. 무슨 일이 있어도 네가 원한다면 언제든 떠날 수 있어. 그걸 잊지 마.\"\n" +
				"\n" +
				"루카스는 고개를 끄덕였지만, 마음속 깊은 곳에서는 여전히 확신이 들지 않았다. 그가 떠난다면 이 마을은 어떻게 될까? 그가 떠난 자리를 누가 메울 수 있을까?\n" +
				"\n" +
				"\"사람들이 많이 모였네.\" 엘레나가 광장을 바라보며 말했다.\n" +
				"\n" +
				"루카스는 엘레나의 시선을 따라가 보았다. 마을 중앙 광장에서는 큰 무도회가 열리고 있었다. 이번 행사는 핀리 그레이가 주최한 것이었다. 핀리는 마을에서 가장 부유한 집안의 후계자였고, 그의 행사들은 항상 성대하게 열렸다. 사람들이 모두 모여 그의 파티를 즐기는 동안, 루카스는 그와 다르게 느꼈다. 그에게는 그저 지나가는 사치일 뿐이었다.\n" +
				"\n" +
				"\"가볼까?\" 엘레나가 조심스럽게 물었다.\n" +
				"\n" +
				"\"별로 내키지는 않는데.\" 루카스가 중얼거렸다.\n" +
				"\n" +
				"하지만 엘레나는 그의 팔을 살짝 잡아끌며 웃었다. \"가끔은 사람들 사이에 있는 것도 나쁘지 않아. 네가 무슨 생각을 하든, 같이 가주면 좋겠어.\"\n" +
				"\n" +
				"루카스는 잠시 망설이다가 고개를 끄덕였다. 그녀의 말이 틀리지 않았다. 무거운 마음을 잠시 내려놓고 사람들 속에 섞이는 것도 도움이 될 것이다.\n" +
				"\n" +
				"광장에 도착하자마자 루카스는 그곳의 생동감을 느낄 수 있었다. 마을 사람들은 흥겨운 음악에 맞춰 춤을 추고 웃으며 떠들고 있었다. 광장 한가운데에는 핀리 그레이가 사람들의 환호를 받으며 서 있었다. 그의 날카로운 눈매와 자신감 넘치는 표정은 마치 이 모든 것이 자신의 뜻대로 이루어진 것처럼 보였다.\n" +
				"\n" +
				"\"루카스!\" 핀리가 그를 발견하고 손을 흔들며 다가왔다. \"여기까지 와주다니! 널 보게 되어 정말 반갑다.\"\n" +
				"\n" +
				"\"응, 뭐. 초대해줘서 고마워.\" 루카스는 건성으로 대답했다. 그의 시선은 여전히 주변을 맴돌았다. 모든 사람들이 핀리에게 고마움을 표하고 있었지만, 그 이면에 있는 불편한 진실을 느끼는 것은 루카스뿐만이 아니었다.\n" +
				"\n" +
				"\"핀리는 정말 모두를 좋아하게 만드는 것 같아.\" 엘레나가 작게 웃으며 말했다. \"하지만 그 웃음 뒤에 뭐가 있는지 나는 알 수 없겠어.\"\n" +
				"\n" +
				"\"나도 그래.\" 루카스는 조용히 대답했다. \"뭔가 꺼림칙한 게 있어.\"\n" +
				"\n" +
				"그들의 대화가 이어지는 동안, 광장은 점점 더 뜨거워졌다. 갑작스러운 음악이 바뀌자, 사람들 사이에서 한 여성이 등장했다. 이사벨라 리드였다. 그녀는 마을에서 가장 뛰어난 무용가로, 그녀가 나타나자마자 사람들은 자연스럽게 그녀에게 시선을 집중했다.\n" +
				"\n" +
				"이사벨라는 핀리에게 다가가 그의 손을 잡았다. 두 사람은 자연스럽게 춤을 추기 시작했고, 사람들은 환호했다. 그러나 그 광경을 바라보는 루카스의 마음속에는 이상한 느낌이 자리 잡았다. 그들의 춤사위는 완벽했지만, 그 안에는 감정이 느껴지지 않았다.\n" +
				"\n" +
				"\"저건 이상해.\" 엘레나가 다시 속삭였다. \"마치 연극을 보는 것 같아.\"\n" +
				"\n" +
				"루카스는 대답하지 않았다. 대신 그의 시선은 점점 광장 구석으로 향했다. 그곳에는 마을 바깥에서 들어온 낯선 인물이 있었다. 오웬 비숍. 그는 마을에서 오랫동안 모습을 보이지 않다가, 어느 날 갑자기 돌아왔다. 아무도 그가 왜 돌아왔는지 알지 못했다.\n" +
				"\n" +
				"오웬은 사람들이 모인 무도회와는 다소 떨어진 곳에서 조용히 그들을 바라보고 있었다. 마치 모든 것을 지켜보는 듯한 눈빛으로.\n" +
				"\n" +
				"\"그가 왜 여기에 있지?\" 루카스는 속으로 물었다. 그와 오웬은 과거에 친구였지만, 그가 떠난 뒤로 소식이 끊겼다. 그가 다시 돌아온 이유는 무엇일까?\n" +
				"\n" +
				"오웬 비숍의 눈빛은 차가웠다. 사람들 사이에서 웃음소리가 가득한 광장 한가운데, 그는 그 어떤 감정도 드러내지 않고 서 있었다. 루카스는 오웬의 시선을 피하며 조용히 속으로 생각했다. 그가 다시 마을에 나타났다는 것은 무엇인가 잘못되어가고 있다는 신호일 수도 있었다.\n" +
				"\n" +
				"오웬이 떠났던 것은 거의 5년 전이었다. 그는 떠나면서 누구에게도 인사하지 않았고, 그가 떠난 이유를 아는 사람은 거의 없었다. 루카스는 오웬이 사라진 후, 그와의 우정이 끊어진 듯 느꼈지만 마음속 한편에는 아직도 그에 대한 의문이 남아있었다. 오랜 시간이 흘렀음에도 불구하고, 그가 돌아온다는 것은 과거에 묻어둔 무언가가 다시 떠오를 수밖에 없다는 뜻이었다.\n" +
				"\n" +
				"“무슨 일이 있는 거야?” 엘레나가 루카스의 시선을 따라가며 물었다. 그녀 역시 오웬을 알아본 모양이었다. \"저 사람, 오웬 맞지? 그가 다시 돌아왔다니 이상한데...\"\n" +
				"\n" +
				"\"맞아. 하지만 왜 돌아왔는지 모르겠어.\" 루카스는 시선을 고정한 채 작게 대답했다. \"그가 마을을 떠났을 때 아무 말도 없이 사라졌었잖아.\"\n" +
				"\n" +
				"\"맞아, 정말 갑작스러웠지.\" 엘레나는 마치 과거의 기억을 더듬는 듯 잠시 침묵했다. \"그가 돌아온 게 아무 이유 없진 않을 거야.\"\n" +
				"\n" +
				"광장에서는 이사벨라 리드와 핀리 그레이의 춤이 끝나가고 있었다. 사람들은 여전히 그들에게 환호하고 있었지만, 루카스는 더 이상 그 장면에 집중할 수 없었다. 그의 모든 신경은 오웬에게로 쏠려 있었다. 오웬은 여전히 사람들과 거리를 두고 있었지만, 그가 무엇을 생각하고 있는지는 알 수 없었다.\n" +
				"\n" +
				"그러나 그때, 광장 저편에서 또 다른 소란이 일어나기 시작했다. 한 남자가 거칠게 소리를 지르며 사람들 사이를 헤치고 나왔다. 마을에서 낯익은 얼굴이었다. 그의 이름은 마일로 파커, 오웬과도 약간의 악연이 있는 인물이었다. 마일로는 몇 년 전 오웬이 떠날 즈음부터 이상하게도 그에 대한 적대감을 드러냈었다. 그리고 지금, 그가 오웬을 본 순간 감정을 억제할 수 없다는 것이 분명했다.\n" +
				"\n" +
				"\"오웬!\" 마일로가 쏘아붙이듯 외쳤다. \"네가 다시 여기에 나타날 줄은 몰랐군.\"\n" +
				"\n" +
				"광장에 있던 사람들은 순간적으로 정적에 휩싸였다. 모두가 마일로와 오웬을 번갈아가며 쳐다보았다. 핀리조차 무도회의 중심에서 그 둘을 지켜보며 흥미로운 표정을 지었다. 그는 자신이 계획한 행사가 갑작스레 벌어진 이 대치 상황 덕분에 예상치 못한 방향으로 흘러가고 있음을 느끼고 있었다.\n" +
				"\n" +
				"오웬은 마일로를 잠시 바라보다가 고개를 숙였다. \"나는 싸우러 돌아온 게 아니야.\"\n" +
				"\n" +
				"그러나 마일로는 그 말에 전혀 귀를 기울이지 않았다. \"그래? 하지만 그때도 네가 무슨 짓을 했는지 다들 잊은 줄 아나 보지?\"\n" +
				"\n" +
				"루카스는 숨을 삼켰다. 오웬이 떠나기 직전 무슨 일이 있었는지, 그는 명확하게 알지 못했다. 하지만 마일로와의 갈등이 그 사건의 중심에 있었던 것만큼은 확실했다. 그리고 그로 인해 오웬이 떠나야 했던 것이었는지도 모른다.\n" +
				"\n" +
				"\"그만둬, 마일로.\" 엘레나가 차분하게 말을 꺼냈다. \"여기서 싸움이 벌어질 필요는 없어.\"\n" +
				"\n" +
				"마일로는 고개를 홱 돌려 엘레나를 노려보았다. \"너도 알잖아, 엘레나. 그가 저지른 일들, 모두가 기억해야 할 일들이야.\"\n" +
				"\n" +
				"\"지금은 그런 이야기를 할 때가 아니야.\" 엘레나가 강단 있게 말을 이어갔다. \"우리는 무도회에 왔어. 모두가 즐기고 있어. 그리고 오웬이 무슨 이유로 돌아왔든, 그 이유를 들어볼 필요가 있잖아. 그를 재판하는 자리가 아니야.\"\n" +
				"\n" +
				"엘레나의 말에 사람들은 잠시 숨을 죽였다. 마일로조차 그녀의 말을 곱씹는 듯 보였지만, 그의 눈에는 여전히 분노가 가득했다. 반면 오웬은 미동도 없이 서 있었다. 그는 자신을 방어하려 하지 않았고, 그저 고개를 숙인 채 서 있을 뿐이었다.\n" +
				"\n" +
				"그 순간, 핀리가 입을 열었다. \"좋아. 모두 잠시 진정하자고.\" 그는 우아하게 웃으며 손을 들었다. \"오늘은 모두가 즐거운 시간을 보내기 위해 모인 날이니까. 과거는 과거고, 지금은 모두가 편안하게 지낼 시간이지.\"\n" +
				"\n" +
				"핀리는 늘 사람들의 분위기를 쉽게 돌릴 수 있는 사람이었다. 그의 말 한 마디에 긴장감이 조금씩 사라졌고, 사람들은 다시 음악에 맞춰 몸을 움직이기 시작했다. 하지만 루카스는 여전히 광장 한가운데 서 있는 오웬을 주시했다. 그가 돌아온 이유는 무엇일까? 그리고 그가 떠날 때 숨겨졌던 진실은 무엇이었을까?\n" +
				"\n" +
				"루카스는 뭔가 커다란 일이 벌어지기 직전이라는 느낌을 떨쳐낼 수 없었다.\n" +
				"\n" +
				"제2장: 잃어버린 시간\n" +
				"\n" +
				"오웬 비숍이 돌아온 이후, 루카스는 그의 존재가 마을에 가져온 변화들을 하나씩 체감하고 있었다. 마을 사람들은 오웬을 대놓고 거부하지 않았지만, 여전히 그를 경계의 눈으로 바라보았다. 무도회에서의 사건 이후로 마일로는 조용히 있었지만, 그가 언제 다시 폭발할지 모르는 불안감이 마을을 떠돌았다.\n" +
				"\n" +
				"루카스는 자신도 모르게 오웬의 행적을 주시하게 되었다. 오웬은 마을 외곽의 오래된 집에 머물고 있었다. 그 집은 오웬이 떠나기 전 살던 곳으로, 시간이 지나며 황폐해졌지만 그럼에도 불구하고 그곳에는 여전히 과거의 흔적이 남아 있었다. 루카스는 오웬이 무엇을 위해 돌아왔는지 알고 싶었지만, 직접 그에게 물어볼 용기가 나지 않았다.\n" +
				"\n" +
				"어느 날 저녁, 루카스는 결국 결심을 굳히고 오웬의 집을 찾았다. 문을 두드리기 전, 그는 잠시 망설였다. 과거의 친구였던 오웬과의 대면이 두려웠다. 그러나 대답 없는 의문들에 지친 루카스는 그 두려움을 이겨내고자 했다.\n" +
				"\n" +
				"문이 열리자, 오웬은 예상치 못한 듯 루카스를 바라보았다. 그 순간, 두 사람 사이에는 묵직한 침묵이 흘렀다. 오웬의 얼굴에는 피로가 서려 있었지만, 그 눈빛 속에는 어딘가 모를 결의가 있었다.\n" +
				"\n" +
				"\"루카스... 여기까지 올 줄은 몰랐어.\" 오웬이 조용히 말했다.\n" +
				"\n" +
				"루카스는 머뭇거리다 입을 열었다. \"너와 얘기하고 싶었어. 왜 돌아온 거야, 오웬? 우리가 모르고 있는 무언가가 있는 거지?\"\n" +
				"\n" +
				"오웬은 잠시 말없이 루카스를 바라보다가 고개를 돌렸다. \"들어와. 밖에서 이야기할 내용은 아닌 것 같아.\"\n" +
				"\n" +
				"루카스는 오웬의 초대를 받아들여 집 안으로 들어갔다. 집 안은 단촐했지만, 과거의 흔적이 여전히 남아 있었다. 벽에는 그들이 어렸을 적 함께 찍은 사진들이 걸려 있었고, 먼지가 쌓인 책장에는 예전에 함께 읽었던 책들이 가지런히 놓여 있었다.\n" +
				"\n" +
				"오웬은 작은 탁자에 루카스를 앉히고, 그 앞에 찻잔을 내놓았다. 뜨거운 차에서 올라오는 김이 두 사람 사이의 긴장을 잠시나마 녹여주었다.\n" +
				"\n" +
				"\"내가 왜 돌아왔는지 궁금할 거야.\" 오웬이 먼저 입을 열었다. 그의 목소리는 낮았지만 확고했다. \"솔직히 말하면, 나도 이렇게 돌아오게 될 줄 몰랐어. 하지만 내가 떠나기 전날 밤에 벌어진 일 때문이야. 그때 나는 잘못된 선택을 했고, 그 선택이 마을에 큰 영향을 미쳤어. 그리고 이제, 그 잘못을 바로잡아야 할 때가 온 것 같아.\"\n" +
				"\n" +
				"루카스는 오웬의 말에 귀를 기울이며 조심스럽게 물었다. \"그 잘못이 뭔지 말해줄 수 있어?\"\n" +
				"\n" +
				"오웬은 잠시 침묵했다. 그의 시선은 탁자 위에 놓인 찻잔에 머물렀다. \"그때 나는... 마일로와 큰 다툼이 있었어. 그리고 그 다툼은 단순한 개인적인 문제가 아니었어. 마을에 영향을 줄 수 있는 중요한 일이었지. 내가 떠난 것도, 그 다툼으로 인해 더 이상 마을에 머물 수 없었기 때문이야.\"\n" +
				"\n" +
				"루카스는 오웬의 말을 들으며 과거의 기억들을 되짚었다. 오웬이 떠난 이후, 마을 사람들은 그에 대해 이야기하는 것을 꺼렸고, 모든 것이 마치 금기처럼 여겨졌다. 그저 오웬이 떠나고 나서 모든 것이 조용해졌을 뿐이었다.\n" +
				"\n" +
				"\"그렇다면 이제는 뭘 하려는 거야?\" 루카스가 물었다. \"그 다툼을 해결하려고 돌아온 거야?\"\n" +
				"\n" +
				"오웬은 고개를 끄덕였다. \"맞아. 마일로와의 문제를 해결하고 싶어. 그리고 그 문제는 나 혼자 해결할 수 있는 게 아니야. 너와... 그리고 마을 사람들이 필요해.\"\n" +
				"\n" +
				"루카스는 오웬의 눈을 바라보았다. 그의 눈에는 진심이 담겨 있었다. 과거의 잘못을 바로잡기 위해 돌아온 그의 결의가 느껴졌다. 루카스는 마음속에서 오랜 친구를 돕고자 하는 마음과, 다시는 그 문제에 휘말리고 싶지 않은 두려움 사이에서 갈등하고 있었다.\n" +
				"\n" +
				"그러나 그는 결국 결심했다. \"알겠어, 오웬. 네가 뭘 하든, 내가 도울 수 있는 한 도울게. 과거를 바로잡는 데에 내가 필요하다면, 함께 하겠어.\"\n" +
				"\n" +
				"오웬의 얼굴에 약간의 안도가 비쳤다. 그는 루카스에게 미소 지으며 말했다. \"고마워, 루카스. 너라면 나를 이해해 줄 거라 생각했어. 우리 함께 이 문제를 해결하자.\"\n" +
				"\n" +
				"두 사람은 조용히 차를 마시며 앞으로의 계획을 이야기하기 시작했다. 오랜 시간이 흘렀지만, 그들 사이에는 여전히 우정의 끈이 이어져 있었다. 과거의 상처와 잘못을 바로잡기 위한 여정이 이제 막 시작된 것이다. 그리고 그 여정이 어디로 향할지, 두 사람은 알 수 없었다.\n" +
				"\n" +
				"하지만 하나는 분명했다. 오웬이 돌아온 이유는 단순한 과거의 회상이 아니었다. 그가 돌아온 것은 마을에 다가오는 위기를 예고하는 것이었다. 그리고 그 위기는 마을 전체를 뒤흔들 수 있는 커다란 것이 될 수도 있었다.\n" +
				"\n" +
				"제3장: 다가오는 그림자\n" +
				"\n" +
				"오웬과 루카스의 대화 이후, 마을에는 서서히 변화의 기운이 감돌기 시작했다. 마일로는 여전히 오웬에 대해 경계심을 풀지 않았고, 마을 사람들 또한 오웬의 존재를 불편해하는 기색이 역력했다. 마을 광장에서의 무도회 사건 이후, 사람들은 그를 볼 때마다 불편한 눈길을 보내곤 했다.\n" +
				"\n" +
				"루카스는 오웬을 돕기로 결심했지만, 그 결정이 가져올 파장을 두려워하고 있었다. 그러나 그는 친구로서 오웬이 홀로 이 문제를 해결하도록 내버려 둘 수 없었다. 루카스는 오웬이 이야기했던 '잘못된 선택'이 무엇인지, 그리고 그것이 마일로와 어떤 관련이 있는지 더 깊이 알아봐야겠다고 생각했다.\n" +
				"\n" +
				"며칠 후, 루카스는 엘레나를 찾아갔다. 그녀는 언제나 마을의 일들을 잘 알고 있었고, 그의 고민을 털어놓기에 적합한 사람이었다. 엘레나는 루카스의 이야기를 들으며 조용히 고개를 끄덕였다.\n" +
				"\n" +
				"\"오웬이 돌아온 이유가 그 다툼 때문이라니...\" 엘레나는 잠시 생각에 잠겼다. \"마일로와의 다툼이 단순한 개인적인 문제가 아니었다면, 그 뒤에 뭔가 더 큰 일이 있었을 거야. 마을 사람들이 오웬에 대해 이야기하기를 꺼리는 것도 그 때문일지도 몰라.\"\n" +
				"\n" +
				"루카스는 엘레나의 말을 들으며 고개를 끄덕였다. \"맞아. 그래서 나도 그 일이 뭔지 알아내고 싶어. 오웬이 떠날 수밖에 없었던 이유를... 그리고 그가 돌아온 이유를.\"\n" +
				"\n" +
				"엘레나는 잠시 침묵을 지키다 루카스를 바라보며 말했다. \"그렇다면 우리 둘이 같이 알아보자. 마일로와의 갈등이 무엇 때문이었는지, 그리고 그로 인해 마을에 어떤 영향이 있었는지. 오웬을 돕기 위해서라도 우리가 알아야 할 것 같아.\"\n" +
				"\n" +
				"루카스는 엘레나의 결심에 안도감을 느꼈다. 혼자가 아니라는 사실이 그에게 큰 힘이 되었다. \"고마워, 엘레나. 네가 함께 해준다면 분명히 해결할 수 있을 거야.\"\n" +
				"\n" +
				"엘레나는 미소를 지으며 말했다. \"우린 팀이잖아, 루카스. 과거에 무슨 일이 있었든, 이제는 우리가 그 일을 바로잡을 차례야.\"\n" +
				"\n" +
				"두 사람은 그날부터 마을 곳곳을 돌아다니며 단서를 찾기 시작했다. 마일로와 오웬 사이에 무슨 일이 있었는지, 그리고 그로 인해 마을에 어떤 변화가 생겼는지. 마을 사람들과 대화를 나누며 조금씩 과거의 진실을 밝혀내려 했다. 그 과정에서 루카스와 엘레나는 점점 더 많은 의문과 마주하게 되었다. 과거의 사건은 단순한 갈등이 아니었고, 그 뒤에는 마을 전체를 뒤흔들 수 있는 비밀이 숨겨져 있었다.\n" +
				"\n" +
				"어느 날, 루카스와 엘레나는 마을의 오래된 기록 보관소를 찾았다. 그곳에는 마을의 역사가 고스란히 보존되어 있었다. 낡은 서류와 먼지 쌓인 책들 사이에서 두 사람은 마일로와 오웬의 과거와 관련된 단서를 찾으려 애썼다. 몇 시간 동안의 검색 끝에, 그들은 중요한 문서를 발견했다. 그것은 몇 년 전 마일로와 오웬이 연루된 사건에 대한 기록이었다.\n" +
				"\n" +
				"문서에 따르면, 마일로와 오웬은 과거 마을의 중요한 자원을 관리하는 일에 관여하고 있었다. 그들은 마을의 공동 자원을 지키기 위해 함께 일했지만, 그 과정에서 의견 충돌이 생겼고, 그 갈등은 점점 심각해졌다. 결국, 마일로는 오웬을 배신했다고 기록되어 있었다. 마일로는 오웬이 자원을 독점하려 한다고 주장했고, 그로 인해 오웬은 마을 사람들의 신뢰를 잃고 떠나야만 했던 것이다.\n" +
				"\n" +
				"루카스는 그 문서를 읽으며 오웬이 왜 그렇게 힘들어했는지 이해할 수 있었다. 그는 마을을 위해 일했지만, 오해와 배신으로 인해 모든 것을 잃었다. 그리고 마일로는 그 이후로 마을의 자원을 장악하고, 자신의 이익을 위해 이용해 온 것 같았다. 루카스는 엘레나와 함께 이 진실을 마을 사람들에게 알리고, 오웬의 결백을 증명해야겠다고 결심했다.\n" +
				"\n" +
				"엘레나는 루카스를 바라보며 말했다. \"이제 모든 것이 조금씩 명확해지고 있어. 우리가 해야 할 일은 이 진실을 마을 사람들에게 알리는 거야. 그들이 오웬을 다시 받아들일 수 있도록, 그리고 마일로가 저지른 잘못을 바로잡을 수 있도록.\"\n" +
				"\n" +
				"루카스는 고개를 끄덕였다. \"맞아. 오웬이 돌아온 이유도 바로 이것일 거야. 과거의 잘못을 바로잡고, 마을에 진실을 알리는 것. 우리가 함께라면 분명 해낼 수 있을 거야.\"\n" +
				"\n" +
				"그날 밤, 루카스와 엘레나는 오웬의 집을 다시 찾았다. 그들은 오웬에게 자신들이 발견한 진실을 전했고, 오웬은 그 이야기를 듣고 잠시 눈을 감았다. 그의 얼굴에는 고통과 안도가 교차하고 있었다.\n" +
				"\n" +
				"\"너희가 이렇게까지 해줄 줄은 몰랐어... 정말 고마워.\" 오웬이 조용히 말했다. \"이제는 나도 더 이상 도망치지 않을 거야. 마일로와 마주하고, 이 모든 것을 바로잡을 거야.\"\n" +
				"\n" +
				"루카스와 엘레나는 그의 결심을 지지했다. 그들은 함께 이 일을 해결하기 위해 나아가기로 했다. 오랜 시간 동안 감춰졌던 진실이 드디어 드러나고 있었다. 그리고 그 진실을 통해 마을은 새로운 변화를 맞이하게 될 것이었다.\n" +
				"\n" +
				"제4장: 폭풍 전야\n" +
				"\n" +
				"오웬이 돌아온 이후, 마을에는 긴장감이 감돌았다. 마을 사람들은 여전히 오웬을 경계했지만, 루카스와 엘레나의 노력 덕분에 일부는 그의 이야기에 귀를 기울이기 시작했다. 특히 마을의 어르신들은 오웬이 떠날 당시의 상황에 대해 더 많은 것을 알고 있었고, 그들의 증언은 오웬의 결백을 증명하는 데 중요한 역할을 했다.\n" +
				"\n" +
				"하지만 마일로는 그들이 진실을 밝히려는 움직임을 눈치채고 있었다. 그는 자신의 지위를 지키기 위해 필사적이었고, 그로 인해 점점 더 과격한 행동을 보이기 시작했다. 마일로의 측근들은 마을 사람들을 위협하며, 오웬과 그를 지지하는 사람들을 방해했다. 루카스와 엘레나는 마일로의 방해에도 불구하고 진실을 알리기 위해 노력했지만, 상황은 점점 더 위험해지고 있었다.\n" +
				"\n" +
				"어느 날 밤, 루카스는 마을 광장에서 마일로의 측근들에게 위협받고 있는 엘레나를 발견했다. 그는 망설임 없이 그들에게 달려가 엘레나를 보호하려 했지만, 그 과정에서 심한 부상을 입고 말았다. 엘레나는 눈물을 흘리며 루카스를 부축했고, 그들은 가까스로 그 자리를 벗어날 수 있었다.\n" +
				"\n" +
				"루카스는 부상을 입었지만, 그의 결의는 더욱 굳건해졌다. 그는 오웬과 엘레나와 함께 마일로의 진실을 밝히고, 마을을 지키기 위해 싸우기로 결심했다. 그들은 더 이상 물러설 곳이 없었다. 마을의 미래를 위해, 그리고 오랜 친구의 결백을 위해 그들은 끝까지 싸우기로 했다.\n" +
				"\n" +
				"그리고 그날 밤, 오웬은 마을 중앙 광장에서 마일로와의 대면을 결심했다. 그는 더 이상 도망치지 않기로, 과거의 잘못을 바로잡고 마을에 진실을 알리기로 마음먹었다. 그의 곁에는 루카스와 엘레나가 있었다. 그들은 함께, 어둠 속에서 진실의 빛을 찾아 나아가고 있었다.\n" +
				"\n" +
				"마을은 폭풍 전야의 고요함에 휩싸여 있었다. 모든 것이 끝나기 전의 긴장감이 공기 중에 떠돌았다. 그리고 그 폭풍 속에서, 오웬과 그의 친구들은 마을을 지키기 위한 마지막 싸움을 준비하고 있었다.";
	}

	@Transactional
	public void deleteAppearedCast(Long storyPlotId, Long castId) {
		StoryPlotNode storyPlotNode = storyPlotNodeRepository.findById(storyPlotId)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NODE_NOT_FOUND));
		CastingNode castingNode = castingNodeRepository.findById(castId)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));

		if (!castingNode.getEpisodes().contains(storyPlotNode)) {
			throw StoryPlotException.of(StoryPlotErrorCode.NOT_APPEARED_RELATIONSHIP);
		}

		int deletedAppearedCount = storyPlotNodeRepository.deleteAppearedCasting(storyPlotId, castId);
		if (1 < deletedAppearedCount) {
			log.warn("예상치 못한 출연 관계가 다수 삭제되었습니다. 예상 삭제 수: 1, 실제 삭제된 수: {}. storyPlotId: {}, castId: {}",
				deletedAppearedCount, storyPlotId, castId);
		}
	}
}
