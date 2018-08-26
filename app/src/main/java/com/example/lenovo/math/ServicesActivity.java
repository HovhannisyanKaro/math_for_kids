package com.example.lenovo.math;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.math.utils.GM;
import com.example.lenovo.math.utils.LogUtils;

import java.util.Arrays;
import java.util.Random;

public class ServicesActivity extends AppCompatActivity {

    private TextView tvAnswer1;
    private TextView tvAnswer2;
    private TextView tvAnswer3;
    private TextView tvAnswer4;
    private TextView tvExercise;
    private RelativeLayout root;
    private ImageView ivBalloon;
    private ImageView ivBalloon1;
    private ImageView ivBalloon2;
    private ImageView ivCloud;
    private TextView[] tvAnswers = new TextView[4];
    private RelativeLayout.LayoutParams[] defaultParams = new RelativeLayout.LayoutParams[4];
    private static int gameType;

    private int xDelta;
    private int yDelta;

    private float tvAnswerSize  = 65;

    private int[] cordinatesExercise;

    private int[] ballons = new int[]{R.drawable.balloons_0,
            R.drawable.balloons_1,
            R.drawable.balloons_2,
            R.drawable.balloons_3,
            R.drawable.balloons_4,
            R.drawable.balloons_5,
            R.drawable.balloons_6,
            R.drawable.balloons_7,
            R.drawable.balloons_8,
            R.drawable.balloons_9,
            R.drawable.balloons_10,
            R.drawable.balloons_11,
            R.drawable.balloons_13
    };

    private int[] leftMargins = new int[4];
    private int[] topMargins = new int[4];

    private int a;
    private int b;

    private MediaPlayer mp;
    private MediaPlayer mpRightAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        root = (RelativeLayout) findViewById(R.id.activity_services);
        init();
    }

    private void init() {
        tvAnswer1 = (TextView) findViewById(R.id.answer_1);
        tvAnswer2 = (TextView) findViewById(R.id.answer_2);
        tvAnswer3 = (TextView) findViewById(R.id.answer_3);
        tvAnswer4 = (TextView) findViewById(R.id.answero_4);
        tvExercise = (TextView) findViewById(R.id.tv_exercise);
        ivBalloon = (ImageView) findViewById(R.id.iv_ballon);
        ivBalloon1 = (ImageView) findViewById(R.id.iv_ballon_1);
        ivBalloon2 = (ImageView) findViewById(R.id.iv_ballon_2);

        ivCloud = (ImageView) findViewById(R.id.iv_cloud);

        tvAnswers[0] = tvAnswer1;
        tvAnswers[1] = tvAnswer2;
        tvAnswers[2] = tvAnswer3;
        tvAnswers[3] = tvAnswer4;

        // TODO set margins from layoutSize
        leftMargins[0] = 15 * 2;
        leftMargins[1] = 90 * 2;
        leftMargins[2] = 180 * 2;
        leftMargins[3] = 260 * 2;

        topMargins[0] = 200 * 2;
        topMargins[1] = 370 * 2;
        topMargins[2] = 280 * 2;
        topMargins[3] = 430 * 2;

        tvAnswer1.setOnTouchListener(onTouchListener());
        tvAnswer2.setOnTouchListener(onTouchListener());
        tvAnswer3.setOnTouchListener(onTouchListener());
        tvAnswer4.setOnTouchListener(onTouchListener());

        GM.changeTextFond(this, tvExercise);
        GM.changeTextFond(this, tvAnswer1);
        GM.changeTextFond(this, tvAnswer2);
        GM.changeTextFond(this, tvAnswer3);
        GM.changeTextFond(this, tvAnswer4);
        mpRightAnswer = MediaPlayer.create(this, R.raw.right_answer);
        Intent intent = getIntent();
        String color = intent.getStringExtra("color");
        gameType = Integer.parseInt(color);
        if (color.equals("0")) {
            gameType = 0;
//            root.setBackgroundColor(ContextCompat.getColor(this, R.color.one));
            //root.setBackgroundColor(ContextCompat.getColor(this, R.color.one));
            root.setBackgroundResource(R.drawable.gaming_1);
            mp = MediaPlayer.create(this, R.raw.first_step);
            mp.setLooping(true);
            mp.start();
        } else if (color.equals("1")) {
            gameType = 1;
//            root.setBackgroundColor(ContextCompat.getColor(this, R.color.two));
            root.setBackgroundResource(R.drawable.gaming_2);
            mp = MediaPlayer.create(this, R.raw.schooldays);
            mp.setLooping(true);
            mp.start();
        } else if (color.equals("2")) {
            gameType = 2;
//            root.setBackgroundColor(ContextCompat.getColor(this, R.color.three));
            root.setBackgroundResource(R.drawable.gaming_3);
            mp = MediaPlayer.create(this, R.raw.mr_jelly_rolls);
            mp.setLooping(true);
            mp.start();
        } else {
            gameType = 3;
//            root.setBackgroundColor(ContextCompat.getColor(this, R.color.four));
            root.setBackgroundResource(R.drawable.gaming_4);
            mp = MediaPlayer.create(this, R.raw.simon_says);
            mp.setLooping(true);
            mp.start();
        }
        game(gameType);

        cloudAnimation();
    }


    private void cloudAnimation(){
        final Animation animSlideToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_to_top);

        animSlideToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivCloud.startAnimation(animSlideToTop);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivCloud.startAnimation(animSlideToTop);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        cordinatesExercise = new int[2];
        tvExercise.getLocationInWindow(cordinatesExercise);

        LogUtils.d(this.getClass().getName(), "coordinates " + cordinatesExercise[0] + " " + cordinatesExercise[1]);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < tvAnswers.length; i++) {
            defaultParams[i] = (RelativeLayout.LayoutParams) tvAnswers[i].getLayoutParams();
        }
    }

    private void game(int gameType) {
        if (gameType == 0) {
            // game type summ
            a = randomNumber(5);
            b = randomNumber(5);
            tvExercise.setText(a + " + " + b + " =" + " ?");

            int rightAnswer = randomNumber(4);
            Integer[] answer = new Integer[4];

            for (int i = 0; i < tvAnswers.length; i++) {
                if (i != rightAnswer) {
                    int randomNumber = randomNumber(5);

                    while (Arrays.asList(answer).contains(randomNumber) || (randomNumber == a + b)) {
                        randomNumber = randomNumber(5);
                    }
                    answer[i] = randomNumber;

                } else {
                    answer[i] = a + b;
                }
                tvAnswers[i].setText(answer[i] + "");
            }
        } else if (gameType == 1) {
            // TODO -
            a = randomNumber(20);
            b = randomNumber(20);

            while (b > a) {
                b = randomNumber(20);
            }

            tvExercise.setText(a + " - " + b + " =" + " ?");
            int rightAnswer = randomNumber(4);
            Integer[] answer = new Integer[4];
            for (int i = 0; i < tvAnswers.length; i++) {
                if (i != rightAnswer) {
                    int randomNumber = randomNumber(20);

                    while (Arrays.asList(answer).contains(randomNumber) || (randomNumber == a - b)) {
                        randomNumber = randomNumber(20);
                    }
                    answer[i] = randomNumber;

                } else {
                    answer[i] = a - b;
                }
                tvAnswers[i].setText(answer[i] + "");
            }

        } else if (gameType == 2) {
            // TODO *
            a = randomNumber(10);
            b = randomNumber(10);
            tvExercise.setText(a + " * " + b + " =" + " ?");
            int rightAnswer = randomNumber(4);
            Integer[] answer = new Integer[4];
            for (int i = 0; i < tvAnswers.length; i++) {
                if (i != rightAnswer) {
                    int randomNumber;
                    if (a * b >= 0 && a * b <= 20) {
                        randomNumber = randomNumber(20);
                    } else if (a * b >= 20 && a * b < 40) {
                        randomNumber = randomNumber(40);
                    } else if (a * b >= 40 && a * b < 60) {
                        randomNumber = randomNumber(60);
                    } else {
                        randomNumber = randomNumber(80);
                    }

                    while (Arrays.asList(answer).contains(randomNumber) || (randomNumber == a * b)) {
                        randomNumber = randomNumber(10);
                    }
                    answer[i] = randomNumber;

                } else {
                    answer[i] = a * b;
                }
                tvAnswers[i].setText(answer[i] + "");
            }
        } else {
            // TODO /
            a = randomNumber(10);
            while (a == 0) {
                a = randomNumber(10);
            }
            b = randomNumber(10);

            int c = a * b;

            tvExercise.setText(c + " / " + a + " =" + " ?");

            int rightAnswer = randomNumber(4);
            Integer[] answer = new Integer[4];

            for (int i = 0; i < tvAnswers.length; i++) {
                if (i != rightAnswer) {
                    int randomNumber = randomNumber(10);

                    while (Arrays.asList(answer).contains(randomNumber) || (randomNumber == b)) {
                        randomNumber = randomNumber(10);
                    }
                    answer[i] = randomNumber;

                } else {
                    answer[i] = b;
                }
                tvAnswers[i].setText(answer[i] + "");
            }
        }
    }

    private int randomNumber(int length) {
        Random random = new Random();
        return random.nextInt(length);
    }

    public void getRandomClick(View view) {
        game(0);
    }

    private void setMargins() {
        for (int i = 0; i < tvAnswers.length; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(leftMargins[i], topMargins[i], 0, 0);
            tvAnswers[i].setLayoutParams(params);
            tvAnswers[i].setPadding(15, 15, 15, 15);
            tvAnswers[i].setTextColor(Color.WHITE);
            tvAnswers[i].setVisibility(View.VISIBLE);
        }
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                TextView tvUserAnswer = (TextView) view;
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        textZoomIn(tvUserAnswer);
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;

                        scaleAnimation(tvUserAnswer, false);
                        break;
                    case MotionEvent.ACTION_UP:
                        textZoomOut(tvUserAnswer);
                        LogUtils.d(this.getClass().getName(), "getWidth " + (cordinatesExercise[0] + tvAnswer4.getWidth()));
                        LogUtils.d(this.getClass().getName(), "getHeight " + (cordinatesExercise[1] + tvAnswer4.getHeight()));
                        if (x >= cordinatesExercise[0] &&
                                y >= cordinatesExercise[1]-40 &&
                                x <= (cordinatesExercise[0] + tvExercise.getWidth()) &&
                                y <= (cordinatesExercise[1] + tvExercise.getHeight())) {
                            int userAnswer = Integer.parseInt(tvUserAnswer.getText().toString());
                            if (gameType == 0) {
                                if (userAnswer == (a + b)) {
                                    String replacedSt = tvExercise.getText().toString().replace("?", userAnswer + "");
                                    tvExercise.setText(replacedSt);
                                    tvUserAnswer.setVisibility(View.INVISIBLE);
                                    mpRightAnswer.start();
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon1, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon2, true);
                                } else {
                                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(400);
                                    colorAnimation(root);
                                    setMargins();
                                }
                            } else if (gameType == 1) {
                                // TODO -
                                if (userAnswer == (a - b)) {
                                    String replacedSt = tvExercise.getText().toString().replace("?", userAnswer + "");
                                    tvExercise.setText(replacedSt);
                                    tvUserAnswer.setVisibility(View.INVISIBLE);
                                    mpRightAnswer.start();
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon1, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon2, true);
                                } else {
                                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(400);
                                    colorAnimation(root);
                                    setMargins();
                                }
                            } else if (gameType == 2) {
                                // TODO *
                                if (userAnswer == (a * b)) {
                                    String replacedSt = tvExercise.getText().toString().replace("?", userAnswer + "");
                                    tvExercise.setText(replacedSt);
                                    tvUserAnswer.setVisibility(View.INVISIBLE);
                                    mpRightAnswer.start();
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon1, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon2, true);
                                } else {
                                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(400);
                                    colorAnimation(root);
                                    setMargins();
                                }
                            } else {
                                // TODO /
                                if (userAnswer == (b)) {
                                    String replacedSt = tvExercise.getText().toString().replace("?", userAnswer + "");
                                    tvExercise.setText(replacedSt);
                                    tvUserAnswer.setVisibility(View.INVISIBLE);
                                    mpRightAnswer.start();
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon1, true);
                                    SlideToAbove(ballons[randomNumber(12)], ivBalloon2, true);
                                } else {
                                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(400);
                                    colorAnimation(root);
                                    setMargins();
                                }
                            }
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        LogUtils.d(this.getClass().getName(), "x= " + x + " y= " + y);
                        view.setLayoutParams(layoutParams);


                        break;
                }
                root.invalidate();
                return true;
            }
        };
    }

    private void colorAnimation(final RelativeLayout myView) {
        AlphaAnimation animation1 = new AlphaAnimation(0.6f, 1.0f);
        animation1.setDuration(300);
        animation1.setFillAfter(true);
        myView.startAnimation(animation1);
    }


    public void SlideToAbove(int resId, final ImageView tvBallon, final boolean isExercise) {
        Animation slide = null;
        if (isExercise) {
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, -6.0f);
        } else {
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, -20.0f);
        }

        slide.setDuration(2000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        if (isExercise) {
            tvBallon.setImageResource(resId);
        }
        tvBallon.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ivBalloon.clearAnimation();
                ivBalloon.setVisibility(View.GONE);
                if (isExercise) {
                    game(gameType);
                    setMargins();
                } else {
                    root.removeView(tvBallon);
                }


            }

        });
    }

    private void scaleAnimation(final TextView tv, boolean clearAnimation) {
//        float finalSize = tv.getTextSize();
//        float startSize; // Size in pixels
//        float endSize;
//        final int animationDuration = 5000;
//
//        if (!clearAnimation) {
//            startSize = 16;
//            endSize = 100;
//        }else{
//            startSize =  16;
//            endSize = 100;
//        }
//
//
//
//        animator.start();


    }

    private void textZoomIn(final TextView tv){
        float startSize = tvAnswerSize;
        float endSize = 100;
        final int animationDuration = 300;

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                tv.setTextSize(animatedValue);
            }
        });
        animator.start();
    }

    private void textZoomOut(final TextView tv){
        float startSize = 100;
        float endSize = tvAnswerSize;
        final int animationDuration = 300;

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                tv.setTextSize(animatedValue);
            }
        });
        animator.start();
    }

}
