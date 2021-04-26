package com.bloniarz.faster.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.bloniarz.faster.GameActivity;
import com.bloniarz.faster.game.levels.FirstLevel;
import com.bloniarz.faster.game.levels.Level;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Level currentLevel;
    private GameActivity gameActivity;
    private boolean paused = false;
    private float currentSpeed;

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        if (!this.isInEditMode())
            this.gameActivity = (GameActivity) context;
        getHolder().addCallback(this);
        reset();
    }

    public void pause(){
        paused = true;
        thread.setRunning(false);
    }

    public void resume(){
        paused = false;
        fixSystemUiVisibility();
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    public void reset(){
        currentSpeed = 2f;
        currentLevel = new FirstLevel(currentSpeed);
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

    public void update(float time) {
        Level.State result = currentLevel.update(time);
        if (result == Level.State.PASSED) {
            changeLevel();
        }
        else if (result == Level.State.LOST) {
            thread.setRunning(false);
            gameActivity.gameOver();
        }
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

    public void changeLevel(){
        currentSpeed += 0.5f;
        currentLevel = new FirstLevel(currentSpeed);
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
        if (paused) {
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

    public boolean isPaused() {
        return paused;
    }
}
