package com.ddj.owing.domain.storyPlot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.storyFolder.error.code.StoryFolderErrorCode;
import com.ddj.owing.domain.storyPlot.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.storyFolder.error.exception.StoryFolderException;
import com.ddj.owing.domain.storyPlot.error.exception.StoryPlotException;
import com.ddj.owing.domain.storyFolder.model.StoryFolder;
import com.ddj.owing.domain.storyPlot.model.StoryPlot;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotCreateDto;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotDto;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotPositionUpdateDto;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotUpdateDto;
import com.ddj.owing.domain.storyFolder.repository.StoryFolderRepository;
import com.ddj.owing.domain.storyPlot.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryPlotService {
	private final StoryPlotRepository storyPlotRepository;
	private final StoryFolderRepository storyFolderRepository;

	private StoryPlot findById(Long id) {
		return storyPlotRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
	}

	public List<StoryPlotDto> getStoryPlotList(Long folderId) {
		// todo: permission
		return storyPlotRepository.findByStoryFolderId(folderId).stream().map(StoryPlotDto::from).toList();
	}

	public StoryPlotDto getStoryPlot(Long id) {
		StoryPlot plot = storyPlotRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
		return StoryPlotDto.from(plot);
	}

	@Transactional
	public StoryPlotDto createStoryPlot(StoryPlotCreateDto storyPlotCreateDto) {
		StoryFolder storyFolder = storyFolderRepository.findById(storyPlotCreateDto.folderId())
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));
		StoryPlot storyPlot = storyPlotCreateDto.toEntity(storyFolder);
		return StoryPlotDto.from(storyPlotRepository.save(storyPlot));
	}

	@Transactional
	public StoryPlotDto updateStoryPlot(Long id, StoryPlotUpdateDto storyPlotUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		// todo: order update
		StoryPlot storyPlot = storyPlotRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
		storyPlot.update(storyPlotUpdateDto.name(), storyPlotUpdateDto.description(),
			storyPlotUpdateDto.position());

		return StoryPlotDto.from(storyPlotRepository.save(storyPlot));
	}

	@Transactional
	public void deleteStoryPlot(Long id) {
		StoryPlot storyPlot = storyPlotRepository.findById(id)
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));
		storyPlotRepository.deleteById(id);
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

}
