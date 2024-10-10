package com.ddj.owing.domain.project.service;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.project.model.dto.*;
import com.ddj.owing.domain.project.repository.ProjectNodeRepository;
import com.ddj.owing.domain.project.repository.ProjectRepository;
import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.domain.project.error.exception.ProjectException;
import com.ddj.owing.domain.story.repository.StoryPlotNodeRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import com.ddj.owing.global.util.S3FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final int MAX_GENRE_COUNT = 5;

    private final ProjectRepository projectRepository;
    private final ProjectNodeRepository projectNodeRepository;
    private final S3FileUtil s3FileUtil;
    private final OpenAiUtil openAiUtil;

    @Value("${cloud.aws.s3.directory.project}")
    private String projectDirectory;

    /**
     * 프로젝트 정보를 불러오는 메서드
     *
     * @return 프로젝트 정보 리스트를 담은 ProjectInfoListResponseDto를 ResponseEntity로 반환
     * @throws ProjectException 프로젝트가 존재하지 않을 경우 발생
     */
    public ResponseEntity<ProjectInfoListResponseDto> loadProject() {

        List<Project> projects = projectRepository.findAllByOrderByUpdatedAtDesc();

        if (projects.isEmpty()) {
            throw ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND);
        }

        ProjectInfoListResponseDto projectInfoListResponseDto = ProjectInfoListResponseDto.fromEntity(projects);

        return ResponseEntity.ok(projectInfoListResponseDto);
    }

    /**
     * OpenAI API 를 이용해 프로젝트 이미지를 생성하는 메서드
     *
     * @param projectRequestDto 프로젝트 요청 정보를 담은 DTO
     * @return 생성된 이미지의 URL 을 ResponseEntity 로 반환
     */
    @Transactional
    public ResponseEntity<String> generateProjectImage(ProjectRequestDto projectRequestDto) {

        String prompt = openAiUtil.createPrompt(projectRequestDto);
        String result = openAiUtil.createImage(prompt);
        String imageBase64 = Parser.extractUrl(result);

        return ResponseEntity.ok(imageBase64);
    }

    /**
     * 프로젝트를 생성하는 메서드, S3에 업로드할 파일을 위한 Presigned URL을 반환
     *
     * @param projectCreateDto 프로젝트 생성 요청 정보를 담은 DTO
     * @return 생성된 프로젝트 정보를 담은 ProjectCreateResponseDto를 ResponseEntity로 반환
     * @throws ProjectException 잘못된 장르 개수일 경우 예외 발생
     */
    @Transactional
    public ResponseEntity<ProjectCreateResponseDto> createProject(ProjectCreateRequestDto projectCreateDto) {

        if (MAX_GENRE_COUNT < projectCreateDto.genres().size()) {
            throw ProjectException.of(ProjectErrorCode.INVALID_GENRE_COUNT);
        }

        String fileName = "project-image.png";
        String preSignedUrl = s3FileUtil.getPreSignedUrl(projectDirectory, fileName);
        String imageUrl = Parser.extractPresignedUrl(preSignedUrl);

        Project project = projectCreateDto.toEntity(imageUrl);
        projectRepository.save(project);

        ProjectNode projectNode = projectCreateDto.toNode(project.getId());
        projectNodeRepository.save(projectNode);

        return ResponseEntity.ok(ProjectCreateResponseDto.fromEntity(project, preSignedUrl));
    }

    public ProjectDetailResponseDto findProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        return ProjectDetailResponseDto.from(project);
    }

    @Transactional
    public void updateProject(Long id, ProjectUpdateRequestDto projectUpdateRequestDto) {
        if (MAX_GENRE_COUNT < projectUpdateRequestDto.genres().size()) {
            throw ProjectException.of(ProjectErrorCode.INVALID_GENRE_COUNT);
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        project.update(projectUpdateRequestDto);

        ProjectNode projectNode = projectNodeRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NODE_NOT_FOUND));
        projectNode.updateTitle(projectUpdateRequestDto.title());
        projectNodeRepository.save(projectNode);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        projectRepository.delete(project);

        projectNodeRepository.findById(id).ifPresentOrElse(
                (node) -> {
                    node.delete();
                    projectNodeRepository.save(node);
                },
                () -> log.warn("Project 데이터 불일치 발생. entity id:{}", project.getId())
        );
    }
}
