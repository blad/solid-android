package com.btellez.solidandroid.utility;

import android.text.TextUtils;

public class Strings {
    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || TextUtils.getTrimmedLength(s) == 0;
    }
}
