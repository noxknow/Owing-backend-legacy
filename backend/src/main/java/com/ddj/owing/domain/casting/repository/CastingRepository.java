package com.ddj.owing.domain.casting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ddj.owing.domain.casting.model.Casting;

@Repository
public interface CastingRepository extends JpaRepository<Casting, Long> {
	@Query("SELECT COALESCE(MAX(position), 0) FROM Casting WHERE castingFolder.id = :folderId")
	Integer findMaxOrderByCastingFolderId(Long folderId);

	@Modifying
	@Query("update Casting set position = position - 1 where position between :start and :end and castingFolder.id = :folderId")
	void decrementPositionBetween(Integer start, Integer end, Long folderId);

	@Modifying
	@Query("update Casting set position = position + 1 where position between :start and :end and castingFolder.id = :folderId")
	void incrementPositionBetween(Integer start, Integer end, Long folderId);

	@Modifying
	@Query("update Casting set position = position - 1 where position > :position and castingFolder.id = :folderId")
	void decrementPositionAfter(Integer position, Long folderId);

	@Modifying
	@Query("update Casting set position = position + 1 where position >= :position and castingFolder.id = :folderId")
	void incrementPositionAfter(Integer position, Long folderId);

	List<Casting> findByCastingFolderIdOrderByPositionAsc(Long folderId);
}
