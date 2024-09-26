package com.ddj.owing.domain.storyBlock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.storyBlock.error.code.StoryBlockErrorCode;
import com.ddj.owing.domain.storyBlock.error.exception.StoryBlockException;
import com.ddj.owing.domain.storyBlock.model.StoryBlock;
import com.ddj.owing.domain.storyBlock.model.dto.StoryBlockCreateDto;
import com.ddj.owing.domain.storyBlock.model.dto.StoryBlockDto;
import com.ddj.owing.domain.storyBlock.model.dto.StoryBlockUpdateDto;
import com.ddj.owing.domain.storyBlock.repository.StoryBlockRepository;
import com.ddj.owing.domain.storyPlot.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.storyPlot.error.exception.StoryPlotException;
import com.ddj.owing.domain.storyPlot.model.StoryPlot;
import com.ddj.owing.domain.storyPlot.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryBlockService {
	private final StoryBlockRepository storyBlockRepository;
	private final StoryPlotRepository storyPlotRepository;

	public List<StoryBlockDto> getStoryBlockList(Long plotId) {
		List<StoryBlock> storyBlockList = storyBlockRepository.findTopLevelBlocksByPlotId(plotId);
		return storyBlockList.stream().map(StoryBlockDto::from).collect(Collectors.toList());
	}

	public StoryBlockDto getStoryBlock(Long id) {
		StoryBlock block = storyBlockRepository.findById(id)
			.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND));
		return StoryBlockDto.from(block);
	}

	@Transactional
	public StoryBlockDto createStoryBlock(StoryBlockCreateDto storyBlockCreateDto) {
		StoryBlock parentBlock = storyBlockCreateDto.parentBlockId() != null ?
			storyBlockRepository.findById(storyBlockCreateDto.parentBlockId())
				.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND)) : null;
		StoryPlot storyPlot = storyPlotRepository.findById(storyBlockCreateDto.storyPlotId())
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));

		StoryBlock newBlock = storyBlockCreateDto.toEntity(storyPlot, parentBlock);
		return StoryBlockDto.from(storyBlockRepository.save(newBlock));
	}

	@Transactional
	public StoryBlockDto updateStoryBlock(Long id, StoryBlockUpdateDto storyBlockUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		// todo: order update
		StoryBlock storyBlock = storyBlockRepository.findById(id)
			.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND));

		StoryBlock parentBlock = storyBlockUpdateDto.parentBlockId() != null ?
			storyBlockRepository.findById(storyBlockUpdateDto.parentBlockId())
				.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND)) : null;
		storyBlock.update(storyBlockUpdateDto.type(), storyBlockUpdateDto.props(), storyBlockUpdateDto.content(),
			storyBlockUpdateDto.position(),
			parentBlock);

		return StoryBlockDto.from(storyBlockRepository.save(storyBlock));
	}

	@Transactional
	public void deleteStoryBlock(Long id) {
		StoryBlock storyBlock = storyBlockRepository.findById(id)
			.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND));
		storyBlockRepository.deleteById(id);
	}

}
