package com.ddj.owing.domain.universe.service;

import com.ddj.owing.domain.universe.error.code.UniverseFolderErrorCode;
import com.ddj.owing.domain.universe.error.exception.UniverseFolderException;
import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderResponseDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderUpdateRequestDto;
import com.ddj.owing.domain.universe.repository.UniverseFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniverseFolderService {

    private final UniverseFolderRepository universeFolderRepository;

    /**
     * 새로운 폴더를 생성하는 메서드
     *
     * @param universeFolderCreateDto 폴더 생성 요청에 필요한 정보를 담은 DTO
     * @return 생성 성공 시 빈 ResponseEntity 반환
     */
    @Transactional
    public ResponseEntity<Void> createFolder(UniverseFolderCreateDto universeFolderCreateDto) {

        UniverseFolder universeFolder = universeFolderCreateDto.toEntity();
        universeFolderRepository.save(universeFolder);

        return ResponseEntity.ok().build();
    }

    /**
     * 모든 폴더를 조회하는 메서드 (읽기 전용)
     *
     * @return UniverseFolder 리스트를 ResponseEntity 로 감싸서 반환
     */
    @Transactional(readOnly = true)
    public ResponseEntity<List<UniverseFolder>> getAllFolders() {

        return ResponseEntity.ok(universeFolderRepository.findAll());
    }

    /**
     * ID로 특정 폴더를 조회하는 메서드 (읽기 전용)
     *
     * @param id 조회할 폴더의 고유 ID
     * @return 조회된 폴더를 ResponseEntity 로 감싸서 반환
     */
    @Transactional(readOnly = true)
    public ResponseEntity<UniverseFolderResponseDto> getFolderById(Long id) {

        UniverseFolder universeFolder = universeFolderRepository.findById(id)
                .orElseThrow(() -> UniverseFolderException.of(UniverseFolderErrorCode.UNIVERSE_FOLDER_NOT_FOUND));
        UniverseFolderResponseDto universeFolderResponseDto = UniverseFolderResponseDto.fromEntity(universeFolder);

        return ResponseEntity.ok(universeFolderResponseDto);
    }

    /**
     * 폴더를 삭제하는 메서드
     *
     * @param id 삭제할 폴더의 고유 ID
     * @return 삭제 성공 시 빈 ResponseEntity 반환
     */
    @Transactional
    public ResponseEntity<Void> deleteFolder(Long id) {

        UniverseFolder universeFolder = universeFolderRepository.findById(id)
                .orElseThrow(() -> UniverseFolderException.of(UniverseFolderErrorCode.UNIVERSE_FOLDER_NOT_FOUND));
        universeFolderRepository.delete(universeFolder);

        return ResponseEntity.ok().build();
    }

    /**
     * 폴더 정보를 업데이트하는 메서드
     *
     * @param id                         업데이트할 폴더의 고유 ID
     * @param universeFolderUpdateRequestDto    업데이트 요청에 필요한 정보를 담은 DTO
     * @return 업데이트 성공 시 빈 ResponseEntity 반환
     */
    @Transactional
    public ResponseEntity<Void> updateFolder(Long id, UniverseFolderUpdateRequestDto universeFolderUpdateRequestDto) {

        UniverseFolder universeFolder = universeFolderRepository.findById(id)
                .orElseThrow(() -> UniverseFolderException.of(UniverseFolderErrorCode.UNIVERSE_FOLDER_NOT_FOUND));

        universeFolder.updateFolder(universeFolderUpdateRequestDto);

        return ResponseEntity.ok().build();
    }
}
