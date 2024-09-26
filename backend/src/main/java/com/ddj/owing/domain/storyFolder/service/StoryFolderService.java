package com.ddj.owing.domain.storyFolder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.storyFolder.error.code.StoryFolderErrorCode;
import com.ddj.owing.domain.storyFolder.error.exception.StoryFolderException;
import com.ddj.owing.domain.storyFolder.model.StoryFolder;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderCreateDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderUpdateDto;
import com.ddj.owing.domain.storyFolder.repository.StoryFolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryFolderService {
	private final StoryFolderRepository storyFolderRepository;

	public List<StoryFolderDto> getStoryFolderList(Long projectId) {
		// todo: permission
		return storyFolderRepository.findByProjectId(projectId).stream().map(StoryFolderDto::from).toList();
	}

	public StoryFolderDto getStoryFolder(Long id) {
		StoryFolder folder = storyFolderRepository.findById(id)
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));
		return StoryFolderDto.from(folder);
	}

	@Transactional
	public StoryFolderDto createStoryFolder(StoryFolderCreateDto storyFolderCreateDto) {
		StoryFolder storyFolder = storyFolderCreateDto.toEntity();
		return StoryFolderDto.from(storyFolderRepository.save(storyFolder));
	}

	@Transactional
	public StoryFolderDto updateStoryFolder(Long id, StoryFolderUpdateDto storyFolderUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		// todo: order update
		StoryFolder storyFolder = storyFolderRepository.findById(id)
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));
		storyFolder.update(storyFolderUpdateDto.name(), storyFolderUpdateDto.description(),
			storyFolderUpdateDto.position());

		return StoryFolderDto.from(storyFolderRepository.save(storyFolder));
	}

	@Transactional
	public void deleteStoryFolder(Long id) {
		StoryFolder storyFolder = storyFolderRepository.findById(id)
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));
		storyFolderRepository.deleteById(id);
	}

}
