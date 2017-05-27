package com.cukurova.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
        public static boolean isEmail(String email) {
        boolean result;

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        result = mat.matches();
        return result;
    }
}
