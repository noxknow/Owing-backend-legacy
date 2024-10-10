package com.ddj.owing.domain.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.story.error.code.StoryPageErrorCode;
import com.ddj.owing.domain.story.error.code.StoryPlotErrorCode;
import com.ddj.owing.domain.story.error.exception.StoryPageException;
import com.ddj.owing.domain.story.error.exception.StoryPlotException;
import com.ddj.owing.domain.story.model.StoryPage;
import com.ddj.owing.domain.story.model.StoryPlot;
import com.ddj.owing.domain.story.model.dto.storyPage.StoryPageBlockDto;
import com.ddj.owing.domain.story.model.dto.storyPage.StoryPageDto;
import com.ddj.owing.domain.story.repository.StoryPageRepository;
import com.ddj.owing.domain.story.repository.StoryPlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryPageService {
	private final StoryPageRepository storyPageRepository;
	private final StoryPlotRepository storyPlotRepository;

	private StoryPage findById(Long storyPlotId) {
		return storyPageRepository.findByStoryPlotId(storyPlotId)
			.orElseThrow(() -> StoryPageException.of(StoryPageErrorCode.PAGE_NOT_FOUND));
	}

	public StoryPageDto getStoryPage(Long storyPlotId) {
		return StoryPageDto.from(findById(storyPlotId));
	}

	@Transactional
	public StoryPageDto createStoryPage(StoryPageDto dto) {
		StoryPlot storyPlot = storyPlotRepository.findById(dto.storyPlotId())
			.orElseThrow(() -> StoryPlotException.of(StoryPlotErrorCode.PLOT_NOT_FOUND));

		StoryPage storyPage = dto.toEntity(storyPlot);
		return StoryPageDto.from(storyPageRepository.save(storyPage));
	}

	@Transactional
	public StoryPageDto updateStoryPage(StoryPageDto dto) {
		StoryPage storyPage = findById(dto.storyPlotId());
		storyPage.updatePageBlocks(dto.blocks().stream().map(StoryPageBlockDto::toEntity).toList());
		return StoryPageDto.from(storyPageRepository.save(storyPage));
	}

}