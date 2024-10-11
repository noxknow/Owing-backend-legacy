package com.ddj.owing.domain.casting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.casting.error.code.CastingFolderErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingFolderException;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderCreateDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderPositionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderUpdateDto;
import com.ddj.owing.domain.casting.repository.CastingFolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CastingFolderService {
	private final CastingFolderRepository castingFolderRepository;

	private CastingFolder findById(Long id) {
		return castingFolderRepository.findById(id)
			.orElseThrow(() -> CastingFolderException.of(CastingFolderErrorCode.FOLDER_NOT_FOUND));
	}

	public List<CastingFolderDto> getCastingFolderList(Long projectId) {
		// todo: permission
		return castingFolderRepository.findAllByProjectIdOrderByPositionAsc(projectId)
			.stream()
			.map(CastingFolderDto::from)
			.toList();
	}

	public CastingFolderDto getCastingFolder(Long id) {
		// todo: permission
		CastingFolder folder = findById(id);
		return CastingFolderDto.from(folder);
	}

	@Transactional
	public CastingFolderDto createCastingFolder(CastingFolderCreateDto castingFolderCreateDto) {
		// todo: permission
		Integer position = castingFolderRepository.findMaxOrderByProjectId(castingFolderCreateDto.projectId());
		CastingFolder castingFolder = castingFolderCreateDto.toEntity(position);
		return CastingFolderDto.from(castingFolderRepository.save(castingFolder));
	}

	@Transactional
	public CastingFolderDto updateCastingFolder(Long id, CastingFolderUpdateDto castingFolderUpdateDto) {
		// todo: projectId & permission check
		// todo: validation
		CastingFolder castingFolder = findById(id);
		castingFolder.update(castingFolderUpdateDto.name(), castingFolderUpdateDto.description());

		return CastingFolderDto.from(castingFolderRepository.save(castingFolder));
	}

	@Transactional
	public void deleteCastingFolder(Long id) {
		CastingFolder castingFolder = findById(id);
		castingFolderRepository.decrementPositionAfter(castingFolder.getPosition(), castingFolder.getProjectId());

		castingFolderRepository.deleteById(id);
	}

	@Transactional
	public CastingFolderDto updateCastingFolderPosition(Long id,
		CastingFolderPositionUpdateDto castingFolderPositionUpdateDto) {
		CastingFolder castingFolder = findById(id);
		Integer newPosition = castingFolderPositionUpdateDto.position();

		if (castingFolder.getPosition().equals(newPosition)) {
			return CastingFolderDto.from(castingFolder);
		}

		Integer maxPosition = castingFolderRepository.findMaxOrderByProjectId(castingFolder.getProjectId());
		if (newPosition < 0 || newPosition > maxPosition) {
			throw CastingFolderException.of(CastingFolderErrorCode.INVALID_POSITION);
		}

		if (castingFolder.getPosition() < newPosition) {
			castingFolderRepository.decrementPositionBetween(castingFolder.getPosition() + 1, newPosition,
				castingFolder.getProjectId());
		} else {
			castingFolderRepository.incrementPositionBetween(newPosition, castingFolder.getPosition() - 1,
				castingFolder.getProjectId());
		}
		castingFolder.updatePosition(newPosition);
		castingFolder = castingFolderRepository.save(castingFolder);

		return CastingFolderDto.from(castingFolder);
	}
}
