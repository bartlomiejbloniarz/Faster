package com.bloniarz.faster.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.bloniarz.faster.GameActivity;
import com.bloniarz.faster.game.levels.FirstLevel;
import com.bloniarz.faster.game.levels.Level;
import com.bloniarz.faster.game.levels.SecondLevel;

import java.util.Locale;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Level currentLevel;
    private GameActivity gameActivity;
    private boolean paused = false, animating = false;
    private float currentSpeed;
    private final Random random;
    private int result = 0;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private ValueAnimator valueAnimator;
    private int countdown;
    private String currentLevelName="", currentLevelDescription="";


    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        random = new Random();
        if (!this.isInEditMode())
            this.gameActivity = (GameActivity) context;
        getHolder().addCallback(this);
        reset();
    }

    Level newRandomLevel(){
        float prob = random.nextFloat();
        if (prob<0.5) {
            currentLevelName = "Falling square";
            currentLevelDescription = "Put your fingers\non the sides\nto avoid red squares\nand\ncollect the green ones!";
            return new FirstLevel(currentSpeed);
        }
        else {
            currentLevelName = "Tap tap tap!";
            currentLevelDescription = "Tap all the squares";
            return new SecondLevel(currentSpeed);
        }
    }

    public void pause(){
        paused = true;
        if (animating){
            gameActivity.runOnUiThread(valueAnimator::pause);
        }
        else {
            thread.setRunning(false);
        }
    }

    public void resume(){
        gameActivity.setScore(result);
        paused = false;
        if (animating){
            gameActivity.runOnUiThread(valueAnimator::resume);
        }
        else {
            fixSystemUiVisibility();
            thread = new GameThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    public void reset(){
        result = 0;
        animating = true;
        createCountdownAnimator();
        gameActivity.runOnUiThread(valueAnimator::start);
        currentSpeed = 2f;
        currentLevel = newRandomLevel();
        setFocusable(true);
        fixSystemUiVisibility();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return currentLevel.touch(event);
    }

    public boolean update(float time) {
        Level.State state = currentLevel.update(time);
        if (state == Level.State.PASSED) {
            changeLevel();
            return false;
        }
        else if (state == Level.State.LOST) {
            thread.setRunning(false);
            gameActivity.gameOver(result);
            return false;
        }
        return true;
    }

    private void endThread(){
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    void drawCountDown(Canvas canvas){
        canvas.drawColor(Color.BLUE);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(300);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.format(Locale.US, "%d", countdown), screenWidth/2, screenHeight/2, paint);
        paint.setTextSize(100);
        canvas.drawText(currentLevelName, screenWidth/2, screenHeight/2-500, paint);
        float x = screenWidth/2, y = screenHeight/2+500;
        for (String line: currentLevelDescription.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }
    }

    private void onAnimationUpdate(ValueAnimator valueAnimator) {
        System.out.println("animating");
        Canvas canvas = getHolder().lockCanvas();
        synchronized (getHolder()){
            if (canvas!= null){
                countdown = (int)valueAnimator.getAnimatedValue();
                drawCountDown(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void createCountdownAnimator(){
        valueAnimator = ValueAnimator.ofInt(3, 2, 1, 0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(this::onAnimationUpdate);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
                resume();
            }
        });
    }

    public void changeLevel(){
        thread.setRunning(false);
        animating = true;
        System.out.println("change");
        currentLevel = newRandomLevel();
        createCountdownAnimator();
        gameActivity.runOnUiThread(valueAnimator::start);
        result += 100;
        gameActivity.setScore(result);
        currentSpeed += 0.5f;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null){
            currentLevel.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (animating){
            Canvas canvas = surfaceHolder.lockCanvas();
            drawCountDown(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        else if (paused) {
            Canvas canvas = surfaceHolder.lockCanvas();
            draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        else
            resume();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        //System.out.println("gooo");
        //endThread();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == GONE)
            endThread();
        super.onVisibilityChanged(changedView, visibility);
    }

    public void fixSystemUiVisibility(){
        setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |SYSTEM_UI_FLAG_FULLSCREEN
                |SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |SYSTEM_UI_FLAG_LAYOUT_STABLE
                |SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public int getScore(){
        return result;
    }

    public boolean isPaused() {
        return paused;
    }
}
