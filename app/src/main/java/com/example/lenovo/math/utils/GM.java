package com.example.lenovo.math.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Hovhannisyan.Karo on 20.02.2017.
 */

public class GM {



    public static void rippleEfect(View view) {
        MaterialRippleLayout.on(view)
                .rippleDuration(600)
                .rippleAlpha((float) 0.1)
                .rippleOverlay(true)
                .rippleColor(Color.GREEN)
                .create();
    }

    public static void changeTextFond(Context context, TextView tv) {
        Typeface myFont = Typeface.createFromAsset(context.getAssets(), "fonts/agent_orange.ttf");
        tv.setTypeface(myFont);
    }
}
