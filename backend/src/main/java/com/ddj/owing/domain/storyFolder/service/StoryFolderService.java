package com.ddj.owing.domain.storyFolder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.storyFolder.model.StoryFolder;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderCreateDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderPositionUpdateDto;
import com.ddj.owing.domain.storyFolder.error.code.StoryFolderErrorCode;
import com.ddj.owing.domain.storyFolder.error.exception.StoryFolderException;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderUpdateDto;
import com.ddj.owing.domain.storyFolder.repository.StoryFolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryFolderService {
	private final StoryFolderRepository storyFolderRepository;

	private StoryFolder findById(Long id) {
		return storyFolderRepository.findById(id)
			.orElseThrow(() -> StoryFolderException.of(StoryFolderErrorCode.FOLDER_NOT_FOUND));
	}

	public List<StoryFolderDto> getStoryFolderList(Long projectId) {
		// todo: permission
		return storyFolderRepository.findAllByProjectIdOrderByPositionAsc(projectId)
			.stream()
			.map(StoryFolderDto::from)
			.toList();
	}

	public StoryFolderDto getStoryFolder(Long id) {
		// todo: permission
		StoryFolder folder = findById(id);
		return StoryFolderDto.from(folder);
	}

	@Transactional
	public StoryFolderDto createStoryFolder(StoryFolderCreateDto storyFolderCreateDto) {
		// todo: permission
		Integer position = storyFolderRepository.findMaxOrderByProjectId(storyFolderCreateDto.projectId()) + 1;
		StoryFolder storyFolder = storyFolderCreateDto.toEntity(position);
		return StoryFolderDto.from(storyFolderRepository.save(storyFolder));
	}

	@Transactional
	public StoryFolderDto updateStoryFolder(Long id, StoryFolderUpdateDto storyFolderUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		StoryFolder storyFolder = findById(id);
		storyFolder.update(storyFolderUpdateDto.name(), storyFolderUpdateDto.description());

		return StoryFolderDto.from(storyFolderRepository.save(storyFolder));
	}

	@Transactional
	public void deleteStoryFolder(Long id) {
		StoryFolder storyFolder = findById(id);
		storyFolderRepository.decrementPositionAfter(storyFolder.getPosition(), storyFolder.getProjectId());

		storyFolderRepository.deleteById(id);
	}

	@Transactional
	public StoryFolderDto updateStoryFolderPosition(Long id,
		StoryFolderPositionUpdateDto storyFolderPositionUpdateDto) {
		StoryFolder storyFolder = findById(id);
		Integer newPosition = storyFolderPositionUpdateDto.position();

		if (storyFolder.getPosition().equals(newPosition)) {
			return StoryFolderDto.from(storyFolder);
		}

		Integer maxPosition = storyFolderRepository.findMaxOrderByProjectId(storyFolder.getProjectId());
		if (newPosition < 1 || newPosition > maxPosition) {
			throw StoryFolderException.of(StoryFolderErrorCode.INVALID_POSITION);
		}


		if (storyFolder.getPosition() < newPosition) {
			storyFolderRepository.decrementPositionBetween(storyFolder.getPosition() + 1, newPosition,
				storyFolder.getProjectId());
		} else {
			storyFolderRepository.incrementPositionBetween(newPosition, storyFolder.getPosition() - 1,
				storyFolder.getProjectId());
		}
		storyFolder.updatePosition(newPosition);
		storyFolder = storyFolderRepository.save(storyFolder);

		return StoryFolderDto.from(storyFolder);
	}
}
