package com.ddj.owing.domain.universe.repository;

import com.ddj.owing.domain.universe.model.Universe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniverseRepository extends JpaRepository<Universe, Long> {
}
