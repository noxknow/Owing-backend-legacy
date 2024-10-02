package com.ddj.owing.domain.universe.repository;

import com.ddj.owing.domain.universe.model.UniverseFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniverseFolderRepository extends JpaRepository<UniverseFolder, Long> {
}
