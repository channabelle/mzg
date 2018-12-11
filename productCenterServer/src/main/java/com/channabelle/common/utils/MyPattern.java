package com.channabelle.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPattern {
    public static Matcher grep(String s, String pattern) {
        Pattern p = Pattern.compile(pattern);
        return p.matcher(s);
    }
}
