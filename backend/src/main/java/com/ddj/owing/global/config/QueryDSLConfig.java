package com.ddj.owing.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDSLConfig {

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * JPAQueryFactory 를 위한 Bean 을 생성하는 메서드
     *
     * @return JPAQueryFactory 객체
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
