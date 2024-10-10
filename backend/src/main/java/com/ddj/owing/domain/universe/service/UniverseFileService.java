package com.ddj.owing.domain.universe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddj.owing.domain.universe.error.code.UniverseFileErrorCode;
import com.ddj.owing.domain.universe.error.code.UniverseFolderErrorCode;
import com.ddj.owing.domain.universe.error.exception.UniverseFileException;
import com.ddj.owing.domain.universe.error.exception.UniverseFolderException;
import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFileImageRequestDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFileRequestDto;
import com.ddj.owing.domain.universe.repository.UniverseFileRepository;
import com.ddj.owing.domain.universe.repository.UniverseFolderRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import com.ddj.owing.global.util.S3FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UniverseFileService {

    private final UniverseFileRepository universeFileRepository;
    private final UniverseFolderRepository universeFolderRepository;
    private final OpenAiUtil openAiUtil;
    private final S3FileUtil s3FileUtil;

    @Value("${cloud.aws.s3.directory.universe}")
    private String universeDirectory;

    /**
     * 주어진 ID로 UniverseFile 정보를 로드하는 메서드 (읽기 전용)
     *
     * @param id 조회할 파일의 고유 ID
     * @return 조회된 UniverseFile 객체를 ResponseEntity 로 감싸서 반환
     */
    @Transactional(readOnly = true)
    public ResponseEntity<UniverseFile> loadUniverseInfo(Long id) {

        UniverseFile universeFile = universeFileRepository.findById(id)
                .orElseThrow(() -> UniverseFileException.of(UniverseFileErrorCode.UNIVERSE_FILE_NOT_FOUND));

        return ResponseEntity.ok(universeFile);
    }

    /**
     * OpenAI API 를 이용해 Universe 파일 이미지를 생성하는 메서드
     *
     * @param universeFileRequestDto 파일 생성 요청을 담은 DTO
     * @return 생성된 이미지의 URL 을 ResponseEntity 로 반환
     */
    @Transactional
    public ResponseEntity<String> generateUniverseImage(UniverseFileRequestDto universeFileRequestDto) {

        UniverseFolder universeFolder = universeFolderRepository.findById(universeFileRequestDto.folderId())
                .orElseThrow(() -> UniverseFolderException.of(UniverseFolderErrorCode.UNIVERSE_FOLDER_NOT_FOUND));

        String prompt = openAiUtil.createPrompt(universeFileRequestDto);
        String result = openAiUtil.createImage(prompt);
        String imageBase64 = Parser.extractUrl(result);

        return ResponseEntity.ok(imageBase64);
    }

    /**
     * Universe 파일을 생성하는 메서드, S3에 업로드할 파일을 위한 Presigned URL을 반환
     *
     * @param universeFileImageRequestDto 파일 생성 요청을 담은 DTO
     * @return 생성된 파일에 대한 Presigned URL을 ResponseEntity로 반환
     */
    @Transactional
    public ResponseEntity<String> createUniverseFile(UniverseFileImageRequestDto universeFileImageRequestDto) {

        UniverseFolder universeFolder = universeFolderRepository.findById(universeFileImageRequestDto.universeFolderId())
                .orElseThrow(() -> UniverseFolderException.of(UniverseFolderErrorCode.UNIVERSE_FOLDER_NOT_FOUND));

        String fileName = "universe-image.png";
        String preSignedUrl = s3FileUtil.getPreSignedUrl(universeDirectory, fileName);
        String imageUrl = Parser.extractPresignedUrl(preSignedUrl);

        UniverseFile universeFile = universeFileImageRequestDto.toEntity(universeFolder, imageUrl);
        universeFileRepository.save(universeFile);

        return ResponseEntity.ok(preSignedUrl);
    }
}
