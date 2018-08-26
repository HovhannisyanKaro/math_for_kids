package com.example.lenovo.math.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lenovo on 21.02.2017.
 */

public class ToastUtils {
    private static final boolean isDebug = true;

    public static void t(Context context, String message){
        if (isDebug)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
