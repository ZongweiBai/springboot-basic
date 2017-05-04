package com.baymin.springboot.ut.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonez on 2017/3/26.
 */
public class RegexUtil {

    /**
     * case insensitive
     */
    public static void caseInsensitive() {
        Pattern pattern = Pattern.compile("(?i)ABC|BCD|CDE|DE:.*");
        Matcher matcher = pattern.matcher("dE:13213");
        System.out.println(matcher.matches());
    }

    public static void main(String[] args) {

    }

}
