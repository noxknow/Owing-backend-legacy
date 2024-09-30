package com.ddj.owing.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    /**
     * 주어진 문자열에서 URL 을 추출하는 메서드
     *
     * @param input URL 을 포함한 문자열
     * @return 추출된 URL 문자열, URL 이 없으면 null
     */
    public static String extractUrl(String input) {

        String urlPattern = "url='([^']*)'";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
