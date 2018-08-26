package com.example.lenovo.math.utils;

import android.util.Log;

/**
 * Created by Hovhannisyan.Karo on 20.02.2017.
 */

public class LogUtils {

    private static final boolean isDebug = true;
    private static final String LOG = "HK_LOG";

    public static void d(String TAG, String message){
        if (isDebug)
            Log.d(LOG + " " + TAG, message);
    }
}
