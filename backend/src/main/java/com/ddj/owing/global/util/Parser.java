package com.ddj.owing.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

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
