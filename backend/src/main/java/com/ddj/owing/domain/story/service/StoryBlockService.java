package com.ddj.owing.domain.story.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.story.model.StoryBlock;
import com.ddj.owing.domain.story.error.code.StoryBlockErrorCode;
import com.ddj.owing.domain.story.error.exception.StoryBlockException;
import com.ddj.owing.domain.story.model.dto.StoryBlockCreateDto;
import com.ddj.owing.domain.story.model.dto.StoryBlockDto;
import com.ddj.owing.domain.story.model.dto.StoryBlockPositionUpdateDto;
import com.ddj.owing.domain.story.model.dto.StoryBlockUpdateDto;
import com.ddj.owing.domain.story.repository.StoryBlockRepository;
import com.ddj.owing.domain.story.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.story.error.exception.StoryPlotException;
import com.ddj.owing.domain.story.model.StoryPlot;
import com.ddj.owing.domain.story.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryBlockService {
	private final StoryBlockRepository storyBlockRepository;
	private final StoryPlotRepository storyPlotRepository;

	private StoryBlock findById(Long id) {
		return storyBlockRepository.findById(id)
			.orElseThrow(() -> StoryBlockException.of(StoryBlockErrorCode.BLOCK_NOT_FOUND));
	}

	public List<StoryBlockDto> getStoryBlockList(Long plotId) {
		List<StoryBlock> storyBlockList = storyBlockRepository.findTopLevelBlocksByPlotId(plotId);
		return storyBlockList.stream().map(StoryBlockDto::from).toList();
	}

	public StoryBlockDto getStoryBlock(Long id) {
		StoryBlock block = findById(id);
		return StoryBlockDto.from(block);
	}

	@Transactional
	public StoryBlockDto createStoryBlock(StoryBlockCreateDto storyBlockCreateDto) {
		StoryBlock parentBlock =
			storyBlockCreateDto.parentBlockId() != null ? findById(storyBlockCreateDto.parentBlockId()) : null;

		StoryPlot storyPlot = storyPlotRepository.findById(storyBlockCreateDto.storyPlotId())
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));

		Integer position = storyBlockRepository.findMaxOrderByStoryPlotId(storyBlockCreateDto.storyPlotId()) + 1;

		StoryBlock newBlock = storyBlockCreateDto.toEntity(storyPlot, parentBlock, position);
		return StoryBlockDto.from(storyBlockRepository.save(newBlock));
	}

	@Transactional
	public StoryBlockDto updateStoryBlock(Long id, StoryBlockUpdateDto storyBlockUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		StoryBlock storyBlock = findById(id);
		storyBlock.update(storyBlockUpdateDto.type(), storyBlockUpdateDto.props(), storyBlockUpdateDto.content());

		return StoryBlockDto.from(storyBlockRepository.save(storyBlock));
	}

	@Transactional
	public void deleteStoryBlock(Long id) {
		StoryBlock storyBlock = findById(id);
		storyBlockRepository.decrementPositionAfter(storyBlock.getPosition(), storyBlock.getStoryPlot().getId());
		storyBlockRepository.deleteById(id);
	}

	@Transactional
	public StoryBlockDto updateStoryBlockPosition(Long id, StoryBlockPositionUpdateDto dto) {
		StoryBlock storyBlock = findById(id);

		StoryBlock oldParentBlock = storyBlock.getParentBlock();
		StoryBlock newParentBlock = findById(dto.parentBlockId());

		Integer oldPosition = storyBlock.getPosition();
		Integer newPosition = dto.position();

		if(oldParentBlock.getId().equals(dto.parentBlockId()) && oldPosition.equals(newPosition)){
			return StoryBlockDto.from(storyBlock);
		}

		if (newPosition < 1 || newPosition > newParentBlock.getChildren().size() + 1) {
			throw StoryBlockException.of(StoryBlockErrorCode.INVALID_POSITION);
		}

		if(oldParentBlock.getId().equals(dto.parentBlockId())){
			if (newPosition < oldPosition) {
				storyBlockRepository.decrementPositionBetween(oldPosition, newPosition, oldParentBlock.getId());
			} else {
				storyBlockRepository.incrementPositionBetween(newPosition, oldPosition - 1, oldParentBlock.getId());
			}
		} else {
			storyBlockRepository.decrementPositionAfter(oldPosition, oldParentBlock.getId());
			storyBlockRepository.incrementPositionAfter(newPosition, newParentBlock.getId());
			storyBlock.updateParentBlock(newParentBlock);
		}

		storyBlock.updatePosition(newPosition);
		return StoryBlockDto.from(storyBlockRepository.save(storyBlock));

	}
}