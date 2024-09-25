package com.ddj.owing.domain.project.repository;

import com.ddj.owing.domain.project.model.Project;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ddj.owing.domain.project.model.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Project> findTop3ByOrderByUpdatedAtDesc() {

        return jpaQueryFactory
                .selectFrom(project)
                .orderBy(project.updatedAt.desc())
                .limit(3)
                .fetch();
    }
}
