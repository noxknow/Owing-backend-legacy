package com.ddj.owing.domain.storyFolder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddj.owing.domain.storyFolder.model.StoryFolder;

public interface StoryFolderRepository extends JpaRepository<StoryFolder, Long> {
	List<StoryFolder> findByProjectId(Long projectId);
}
