package com.ddj.owing.domain.universe.repository;

import com.ddj.owing.domain.universe.model.UniverseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniverseFileRepository extends JpaRepository<UniverseFile, Long> {
}
