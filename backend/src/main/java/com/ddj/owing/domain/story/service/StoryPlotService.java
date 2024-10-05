package com.ddj.owing.domain.story.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.repository.CastingNodeRepository;
import com.ddj.owing.domain.story.model.StoryPlotNode;
import com.ddj.owing.domain.story.model.dto.*;
import com.ddj.owing.domain.story.repository.StoryPlotNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.story.error.code.StoryFolderErrorCode;
import com.ddj.owing.domain.story.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.story.error.exception.StoryPlotException;
import com.ddj.owing.domain.story.model.StoryPlot;
import com.ddj.owing.domain.story.error.exception.StoryFolderException;
import com.ddj.owing.domain.story.model.StoryFolder;
import com.ddj.owing.domain.story.repository.StoryFolderRepository;
import com.ddj.owing.domain.story.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryPlotService {
	private final StoryPlotRepository storyPlotRepository;
	private final StoryFolderRepository storyFolderRepository;
	private final StoryPlotNodeRepository storyPlotNodeRepository;
	private final CastingNodeRepository castingNodeRepository;

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

		StoryPlotNode storyPlotNode = storyPlotCreateDto.toNode(savedStoryPlot.getId());
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

		if(oldFolder.getId().equals(newFolder.getId()) && oldPosition==newPosition) {
			return StoryPlotDto.from(storyPlot);
		}

		if (newPosition < 1 || newPosition > newFolder.getStoryPlots().size() + 1) {
			throw StoryPlotException.of(StoryPlotErrorCode.INVALID_POSITION);
		}

		if(oldFolder.getId().equals(newFolder.getId())){
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
	public List<StoryPlotAppearedCastDto> registerCasts(Long storyPlotId, StoryPlotAppearedCastCreateDto appearedCastCreateDto) {
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
	public void extractCasts(Long id) {
		// 기존 캐릭터 정보(이름, id), StoryPlot -> Gen Ai
		// Gen Ai -> 출연한 캐릭터 정보(이름, id)
		// 해당 값을 사용자에게 Return
	}
}
