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

    /**
     * 주어진 PreSigned URL에서 쿼리 파라미터를 제외한 URL을 추출하는 메서드
     *
     * @param preSignedUrl 쿼리 파라미터가 포함된 PreSigned URL
     * @return 쿼리 파라미터를 제외한 URL
     */
    public static String extractPresignedUrl(String preSignedUrl) {

        String urlPattern = "([^?]+)";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(preSignedUrl);

        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;
        }
    }
}
