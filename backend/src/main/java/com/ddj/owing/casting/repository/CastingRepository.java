package com.ddj.owing.casting.repository;

import com.ddj.owing.casting.entity.Casting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastingRepository extends JpaRepository<Casting, Long> {
}
