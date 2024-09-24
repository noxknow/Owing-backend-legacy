package com.ddj.owing.project.domain.enums;

import lombok.Getter;

@Getter
public enum Genre {

    DRAMA("드라마"),
    ROMANCE("로맨스"),
    CRIME_NOIR("범죄/느와르"),
    THRILLER("스릴러"),
    ADVENTURE("어드벤처"),
    MYSTERY("미스터리"),
    HORROR("공포"),
    DETECTIVE("추리"),
    SF("SF"),
    HISTORICAL("사극"),
    ACTION("무협"),
    ENTERTAINMENT("예능"),
    COMEDY("코미디"),
    WAR("전쟁"),
    MUSICAL("음악/뮤지컬"),
    FANFICTION("팬픽"),
    BL_GL("BL/GL"),
    OTHER("기타");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }
}
