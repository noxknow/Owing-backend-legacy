package com.ddj.owing.casting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CastingRequestDto {

    private String name;
    private Long age;
    private String gender;
    private String role;
    private String detail;
}
