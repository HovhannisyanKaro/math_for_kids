package com.example.lenovo.math;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.math.utils.GM;
import com.example.lenovo.math.utils.LogUtils;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView tvSum;
    private TextView tvDif;
    private TextView tvMult;
    private TextView tvDiv;
    private ImageView animation;
    private CheckBox musicSettings;
    private RelativeLayout root;
    private LinearLayout[] categories = new LinearLayout[4];
    private static MediaPlayer splashMusic;
    private Handler handler;
    private static Thread aThread;

    int[] location = new int[2];
    private static int testTime = 9999;
    private int xDelta;
    private int yDelta;

    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        tvSum = (TextView) findViewById(R.id.tv_sum);
        tvDif = (TextView) findViewById(R.id.tv_dif);
        tvMult = (TextView) findViewById(R.id.tv_mult);
        tvDiv = (TextView) findViewById(R.id.tv_div);
        animation = (ImageView) findViewById(R.id.animation);
        musicSettings = (CheckBox) findViewById(R.id.music_settings);
        root = (RelativeLayout) findViewById(R.id.activity_main);
        musicSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    splashMusic.start();
                } else {
                    splashMusic.stop();
                }
            }
        });

        Glide
                .with(this)
                .load(R.drawable.crawl)
                .asGif()
                .placeholder(R.drawable.crawl)
                .crossFade()
                .into(animation);

        final RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) animation.getLayoutParams();

        animation.setOnTouchListener(onTouchListener());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what < testTime) {
                    if (location[0] < categories[0].getWidth() - 140) {
                        par.leftMargin += 10;
                        animation.setLayoutParams(par);
                        animation.getLocationOnScreen(location);
                        LogUtils.d(this.getClass().getSimpleName(), "x = " + location[0] + " y= " + location[1]);
                    } else {
                        par.leftMargin -= 10;
                        animation.setLayoutParams(par);
                        animation.getLocationOnScreen(location);
                        animation.getLocationOnScreen(location);
                    }
                }
            }
        };
        GM.changeTextFond(this, tvSum);
        GM.changeTextFond(this, tvDif);
        GM.changeTextFond(this, tvMult);
        GM.changeTextFond(this, tvDiv);

        categories[0] = (LinearLayout) findViewById(R.id.iv_1);
        categories[1] = (LinearLayout) findViewById(R.id.iv_2);
        categories[2] = (LinearLayout) findViewById(R.id.iv_3);
        categories[3] = (LinearLayout) findViewById(R.id.iv_4);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click__);

        splashMusic = MediaPlayer.create(this, R.raw.splash_music);
        splashMusic.setLooping(true);
        splashMusic.start();

        for (int i = 0; i < categories.length; i++) {
            final int finalI = i;
            GM.rippleEfect(categories[finalI]);
            categories[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mp.start();
                    Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
                    intent.putExtra("color", finalI + "");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    for (int j = 0; j < categories.length; j++) {
                        categories[finalI].setClickable(false);
                    }
                }
            });
        }
        aThread = new Thread(new AnimationThred());
        aThread.start();

    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        stopAnimation(animation);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                root.invalidate();
                return true;
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        splashMusic.pause();
        synchronized (mPauseLock){
            mPaused = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (mPauseLock){
            mPaused = false;
            mPauseLock.notifyAll();
        }
        if (splashMusic != null) {
            splashMusic.start();
        } else {
            splashMusic = MediaPlayer.create(this, R.raw.splash_music);
            splashMusic.setLooping(true);
            splashMusic.start();
        }
        for (int i = 0; i < categories.length; i++) {
            categories[i].setClickable(true);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        splashMusic = null;
    }


    public class AnimationThred implements Runnable {

        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        public AnimationThred(){
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;
        }
        @Override
        public void run() {
            while (!mFinished) {
                for (int i = 0; i < testTime; i++) {
                        customSleep();
                        handler.sendEmptyMessage(i);

                }

                synchronized (mPauseLock){
                    while (mPaused){
                        try {
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void customSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopAnimation(ImageView imageView) {
        if (root.getWidth() - 50 > imageView.getWidth()) {
            LogUtils.d(this.getClass().getSimpleName(), " false rootWidth = " + (root.getWidth() - 50) + " animationWidth = " + imageView.getWidth());
        } else {
            LogUtils.d(this.getClass().getSimpleName(), " true rootWidth = " + (root.getWidth() - 50) + " animationWidth = " + imageView.getWidth());
        }
    }
}


