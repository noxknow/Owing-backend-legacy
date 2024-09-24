package com.ddj.owing.domain.project.model.enums;

import lombok.Getter;

@Getter
public enum Category {

    SCENARIO_SCRIPT("시나리오/대본"),
    WEB_NOVEL("웹소설"),
    WEBTOON_STORY("웹툰스토리"),
    GAME_STORY("게임스토리"),
    PLAY("희극"),
    ESSAY("에세이"),
    LITERATURE("순문학"),
    OTHER("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

}
