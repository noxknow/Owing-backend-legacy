package com.ddj.owing.domain.casting.repository;

import com.ddj.owing.domain.casting.model.Casting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastingRepository extends JpaRepository<Casting, Long> {
}
