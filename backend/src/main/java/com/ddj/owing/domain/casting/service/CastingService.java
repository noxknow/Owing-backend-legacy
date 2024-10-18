package com.ddj.owing.domain.casting.service;

import java.util.List;
import java.util.Set;

import com.ddj.owing.domain.casting.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.code.CastingFolderErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.ddj.owing.domain.casting.error.exception.CastingFolderException;
import com.ddj.owing.domain.casting.model.dto.CastingImageRequestDto;
import com.ddj.owing.domain.casting.model.dto.CastingImageResponseDto;
import com.ddj.owing.domain.casting.model.dto.CastingRelationshipInfoDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingConnectionCreateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingConnectionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingCoordUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingGraphDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingInfoUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingPositionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingRelationshipDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingRequestDto;
import com.ddj.owing.domain.casting.repository.CastingFolderRepository;
import com.ddj.owing.domain.casting.repository.CastingNodeRepository;
import com.ddj.owing.domain.casting.repository.CastingRepository;
import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.domain.project.error.exception.ProjectException;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.project.repository.ProjectNodeRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import com.ddj.owing.global.util.S3FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CastingService {

	private final S3FileUtil s3FileUtil;
	private final OpenAiUtil openAiUtil;

	private final CastingRepository castingRepository;
	private final CastingNodeRepository castingNodeRepository;
	private final CastingFolderRepository castingFolderRepository;
	private final ProjectNodeRepository projectNodeRepository;

    @Value("${cloud.aws.s3.directory.casting}")
    private String castingDirectory;

    /**
     * 캐릭터 이미지를 생성하는 메서드
     * 주어진 CastingRequestDto를 이용해서 프롬프트를 만들고, OpenAI API를 통해 이미지를 생성
     *
     * @param castingRequestDto 캐릭터 정보를 담고 있는 DTO
     * @return 생성된 이미지의 URL을 ResponseEntity로 반환
     */
    @Transactional
    public ResponseEntity<String> generateCharacterImage(CastingRequestDto castingRequestDto) {

        String prompt = openAiUtil.createPrompt(castingRequestDto);
        String result = openAiUtil.createImage(prompt);
        String imageBase64 = Parser.extractBase64(result);

        return ResponseEntity.ok(imageBase64);
    }

	/**
	 * 캐릭터 정보를 저장하고 파일 생성 요청을 위한 Presigned URL을 반환하는 메서드
	 *
	 * @param castingImageRequestDto 캐릭터 생성 정보를 담고 있는 DTO
	 * @return 생성된 파일에 대한 Presigned URL을 ResponseEntity로 반환
	 */
	@Transactional
	public ResponseEntity<CastingImageResponseDto> createCharacter(CastingImageRequestDto castingImageRequestDto) {

		CastingFolder castingFolder = castingFolderRepository.findById(castingImageRequestDto.folderId())
				.orElseThrow(() -> CastingFolderException.of(CastingFolderErrorCode.FOLDER_NOT_FOUND));

		Integer position = castingRepository.findMaxOrderByCastingFolderId(castingImageRequestDto.folderId()) + 1;

		String fileName = "casting-image.png";
		String preSignedUrl = s3FileUtil.getPreSignedUrl(castingDirectory, fileName);
		String imageUrl = Parser.extractPresignedUrl(preSignedUrl);

		Casting casting = castingImageRequestDto.toEntity(castingFolder, position, imageUrl);
		castingRepository.save(casting);

		CastingNode castingNode = castingImageRequestDto.toNodeEntity(casting);
		ProjectNode projectNode = projectNodeRepository.findById(castingFolder.getProjectId())
				.orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NODE_NOT_FOUND));
		castingNode.linkProjectNode(projectNode);

		castingNodeRepository.save(castingNode);
		CastingImageResponseDto castingImageResponseDto = CastingImageResponseDto.fromEntity(casting, preSignedUrl);

		return ResponseEntity.ok(castingImageResponseDto);
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

//	@Transactional
//	public CastingDto createCasting(CastingCreateDto castingCreateDto) {
//		CastingFolder castingFolder = castingFolderRepository.findById(castingCreateDto.folderId())
//			.orElseThrow(() -> CastingFolderException.of(CastingFolderErrorCode.FOLDER_NOT_FOUND));
//
//		Integer position = castingRepository.findMaxOrderByCastingFolderId(castingCreateDto.folderId()) + 1;
//
//		Casting casting = castingCreateDto.toEntity(castingFolder, position);
//		Casting savedCasting = castingRepository.save(casting);
//
//		CastingNode castingNode = castingCreateDto.toNodeEntity(savedCasting);
//		CastingNode savedCastingNode = castingNodeRepository.save(castingNode);
//
//		return CastingDto.from(savedCastingNode);
//	}

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
			coordUpdateDto.position().x(),
			coordUpdateDto.position().y()
		);

		CastingNode castingNode = castingNodeRepository.findById(id)
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NODE_NOT_FOUND));
		castingNode.updateCoord(
			coordUpdateDto.position().x(),
			coordUpdateDto.position().y()
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

		if (newPosition < 0 || newPosition > newFolder.getCastings().size()) {
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
				connectionCreateDto.sourceHandle(),
				connectionCreateDto.targetHandle()
			);
		} else {
			sourceCasting.addBiConnection(
				connectionCreateDto.uuid(),
				targetCasting,
				connectionCreateDto.label(),
				connectionCreateDto.sourceHandle(),
				connectionCreateDto.targetHandle()
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
			connectionCreateDto.connectionType(),
			connectionCreateDto.sourceHandle(),
			connectionCreateDto.targetHandle()
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
		CastingNode sourceCasting = castingNodeRepository.findById(connectionUpdateDto.sourceId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));
		CastingNode targetCasting = castingNodeRepository.findById(connectionUpdateDto.targetId())
			.orElseThrow(() -> CastingException.of(CastingErrorCode.CASTING_NOT_FOUND));

		boolean isNameUpdated = false;
		switch (connectionUpdateDto.connectionType()) {
			case ConnectionType.DIRECTIONAL -> isNameUpdated = castingNodeRepository.updateDirectionalConnectionName(
					uuid,
					sourceCasting.getId(),
					targetCasting.getId(),
					connectionUpdateDto.label(),
					connectionUpdateDto.sourceHandle().name(),
					connectionUpdateDto.targetHandle().name()
			).isPresent();
			case ConnectionType.BIDIRECTIONAL ->
				isNameUpdated = castingNodeRepository.updateBidirectionalConnectionName(
						uuid,
						sourceCasting.getId(),
						targetCasting.getId(),
						connectionUpdateDto.label(),
						connectionUpdateDto.sourceHandle().name(),
						connectionUpdateDto.targetHandle().name()
				).isPresent();
		}

		if (!isNameUpdated) {
			throw CastingException.of(CastingErrorCode.CONNECTION_NAME_UPDATE_FAIL);
		}

		return new CastingRelationshipDto(
			uuid,
			connectionUpdateDto.sourceId(),
			connectionUpdateDto.targetId(),
			connectionUpdateDto.connectionType(),
			connectionUpdateDto.sourceHandle(),
			connectionUpdateDto.targetHandle()
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
