package com.ddj.owing.domain.casting.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.code.CastingFolderErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.casting.error.exception.CastingFolderException;
import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.CastingRelationship;
import com.ddj.owing.domain.casting.model.ConnectionType;
import com.ddj.owing.domain.casting.model.dto.CastingRelationshipInfoDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingConnectionCreateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingConnectionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingCoordUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingCreateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingGraphDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingInfoUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingPositionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingRelationshipDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingRequestDto;
import com.ddj.owing.domain.casting.repository.CastingFolderRepository;
import com.ddj.owing.domain.casting.repository.CastingNodeRepository;
import com.ddj.owing.domain.casting.repository.CastingRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CastingService {

	private final CastingRepository castingRepository;
	private final OpenAiUtil openAiUtil;

	private final CastingNodeRepository castingNodeRepository;
	private final CastingFolderRepository castingFolderRepository;

	@Transactional
	public ResponseEntity<String> generateCharacterImage(CastingRequestDto castingRequestDto) {

		String prompt = openAiUtil.createPrompt(castingRequestDto);
		String jsonString = openAiUtil.createImage(prompt);
		String imageUrl = Parser.extractUrl(jsonString);
		Casting casting = castingRequestDto.toEntity(imageUrl);

		castingRepository.save(casting);

		return ResponseEntity.ok(imageUrl);
	}

	public List<CastingDto> getCastingList(Long folderId) {
		return castingRepository.findByCastingFolderIdOrderByPositionAsc(folderId)
			.stream()
			.map(CastingDto::from)
			.toList();
	}

	public CastingDto getCasting(Long id) {
		Casting casting = castingRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		return CastingDto.from(casting);
	}

	@Transactional
	public CastingDto createCasting(CastingCreateDto castingCreateDto) {
		CastingFolder castingFolder = castingFolderRepository.findById(castingCreateDto.folderId())
			.orElseThrow(() -> CastingFolderException.of(CastingFolderErrorCode.FOLDER_NOT_FOUND));

		Integer position = castingRepository.findMaxOrderByCastingFolderId(castingCreateDto.folderId()) + 1;

		Casting casting = castingCreateDto.toEntity(castingFolder, position);
		Casting savedCasting = castingRepository.save(casting);

		CastingNode castingNode = castingCreateDto.toNodeEntity(savedCasting);
		CastingNode savedCastingNode = castingNodeRepository.save(castingNode);

		return CastingDto.from(savedCastingNode);
	}

	@Transactional
	public CastingDto updateCastingInfo(Long id, CastingInfoUpdateDto castingInfoUpdateDto) {
		Casting casting = castingRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		casting.updateInfo(
			castingInfoUpdateDto.name(),
			castingInfoUpdateDto.age(),
			castingInfoUpdateDto.name(),
			castingInfoUpdateDto.role(),
			castingInfoUpdateDto.detail(),
			castingInfoUpdateDto.imageUrl()
		);

		CastingNode castingNode = castingNodeRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
		castingNode.updateInfo(
			castingInfoUpdateDto.name(),
			castingInfoUpdateDto.age(),
			castingInfoUpdateDto.name(),
			castingInfoUpdateDto.role(),
			castingInfoUpdateDto.imageUrl()
		);
		CastingNode updatedCastingNode = castingNodeRepository.save(castingNode);

		return CastingDto.from(updatedCastingNode);
	}

	@Transactional
	public CastingDto updateCastingCoord(Long id, CastingCoordUpdateDto coordUpdateDto) {
		Casting casting = castingRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		casting.updateCoord(
			coordUpdateDto.coordX(),
			coordUpdateDto.coordY()
		);

		CastingNode castingNode = castingNodeRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
		castingNode.updateCoord(
			coordUpdateDto.coordX(),
			coordUpdateDto.coordY()
		);
		CastingNode updatedCastingNode = castingNodeRepository.save(castingNode);

		return CastingDto.from(updatedCastingNode);
	}

	@Transactional
	public CastingDto updateCastingPosition(Long id, CastingPositionUpdateDto dto) {
		Casting casting = castingRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));

		CastingFolder oldFolder = casting.getCastingFolder();
		CastingFolder newFolder = castingFolderRepository.findById(casting.getCastingFolder().getId())
			.orElseThrow(() -> CastingFolderException.of(CastingFolderErrorCode.FOLDER_NOT_FOUND));

		int oldPosition = casting.getPosition();
		int newPosition = dto.position();

		if (oldFolder.getId().equals(newFolder.getId()) && oldPosition == newPosition) {
			return CastingDto.from(casting);
		}

		if (newPosition < 1 || newPosition > newFolder.getCastings().size() + 1) {
			throw CastingException.of(CastingErrorCode.INVALID_POSITION);
		}

		if (oldFolder.getId().equals(newFolder.getId())) {
			if (oldPosition < newPosition) {
				castingRepository.decrementPositionBetween(oldPosition, newPosition, oldFolder.getId());
			} else {
				castingRepository.incrementPositionBetween(newPosition, oldPosition - 1, oldFolder.getId());
			}
		} else {
			castingRepository.decrementPositionAfter(oldPosition, oldFolder.getId());
			castingRepository.incrementPositionAfter(newPosition, newFolder.getId());
			casting.updateFolder(newFolder);
		}

		casting.updatePosition(newPosition);
		return CastingDto.from(castingRepository.save(casting));
	}

	@Transactional
	public void deleteCasting(Long id) {
		Casting casting = castingRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		castingRepository.decrementPositionAfter(casting.getPosition(), casting.getCastingFolder().getId());

		castingRepository.deleteById(id);
		CastingNode castingNode = castingNodeRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
		castingNode.delete();
		castingNodeRepository.save(castingNode);
	}

	/**
	 * Casting간 Relation을 생성하는 메서드
	 *
	 * @param connectionCreateDto connectionType()을 통해 단방향, 양방향 지정
	 * @return
	 */
	@Transactional
	public CastingRelationshipDto createConnection(CastingConnectionCreateDto connectionCreateDto) {
		CastingNode sourceCasting = castingNodeRepository.findById(connectionCreateDto.sourceId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		CastingNode targetCasting = castingNodeRepository.findById(connectionCreateDto.targetId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));

		boolean isDirectional = ConnectionType.DIRECTIONAL.equals(connectionCreateDto.connectionType());
		if (isDirectional) {
			sourceCasting.addConnection(
				connectionCreateDto.uuid(),
				targetCasting,
				connectionCreateDto.label(),
				connectionCreateDto.sourceHandleStr(),
				connectionCreateDto.targetHandleStr()
			);
		} else {
			sourceCasting.addBiConnection(
				connectionCreateDto.uuid(),
				targetCasting,
				connectionCreateDto.label(),
				connectionCreateDto.sourceHandleStr(),
				connectionCreateDto.targetHandleStr()
			);
		}

		CastingNode updatedCastingNode = castingNodeRepository.save(sourceCasting);
		Set<CastingRelationship> connections = isDirectional
			? updatedCastingNode.getOutConnections()
			: updatedCastingNode.getOutBiConnections();

		CastingRelationship castingRelationship = connections.stream()
			.filter(conn -> conn.getCastingNode().getId().equals(connectionCreateDto.targetId()))
			.filter(conn -> conn.getLabel().equals(connectionCreateDto.label()))
			.findFirst()
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CONNECTION_NOT_FOUND));

		return new CastingRelationshipDto(
			castingRelationship.getUuid(),
			connectionCreateDto.sourceId(),
			connectionCreateDto.targetId(),
			connectionCreateDto.connectionType()
		);
	}

	/**
	 * connectionType에 따라 단방향, 양방향 관계 이름을 변경.
	 * @param uuid
	 * relationship 지정에 사용
	 * @param connectionUpdateDto
	 * @return
	 * 관계 id, 시작객체 id, 끝객체 id, connectionType이 포함된 CastingRelationshipDto
	 */
	@Transactional
	public CastingRelationshipDto updateConnectionName(String uuid, CastingConnectionUpdateDto connectionUpdateDto) {
		CastingNode fromCasting = castingNodeRepository.findById(connectionUpdateDto.fromId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		CastingNode toCasting = castingNodeRepository.findById(connectionUpdateDto.toId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));

		boolean isNameUpdated = false;
		switch (connectionUpdateDto.connectionType()) {
			case ConnectionType.DIRECTIONAL ->
				isNameUpdated = castingNodeRepository.updateDirectionalConnectionName(uuid, fromCasting.getId(),
					toCasting.getId(), connectionUpdateDto.label()).isPresent();
			case ConnectionType.BIDIRECTIONAL ->
				isNameUpdated = castingNodeRepository.updateBidirectionalConnectionName(uuid, fromCasting.getId(),
					toCasting.getId(), connectionUpdateDto.label()).isPresent();
		}

		if (!isNameUpdated) {
			throw CastingException.of(CastingErrorCode.CONNECTION_NAME_UPDATE_FAIL);
		}

		return new CastingRelationshipDto(
			uuid,
			connectionUpdateDto.fromId(),
			connectionUpdateDto.toId(),
			connectionUpdateDto.connectionType()
		);
	}

	@Transactional
	public void deleteConnection(String uuid) {
		Integer deletedConnectionCount = castingNodeRepository.deleteConnectionByUuid(uuid);

		if (deletedConnectionCount < 1) {
			throw CastingException.of(CastingErrorCode.CONNECTION_NOT_FOUND);
		}

		if (deletedConnectionCount > 1) {
			throw CastingException.of(CastingErrorCode.INVALID_DELETE_COUNT);
		}
	}

	public CastingGraphDto getGraph(Long projectId) {
		List<CastingDto> castingNodeList =
			castingNodeRepository.findAllByProjectId(projectId).stream()
				.map(casting -> CastingDto.from(casting)).toList();
		List<CastingRelationshipInfoDto> castingConnectionList =
			castingNodeRepository.findAllConnectionByProjectId(projectId);

		return new CastingGraphDto(castingNodeList, castingConnectionList);
	}
}
