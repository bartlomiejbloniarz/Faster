package com.bloniarz.faster;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Level currentLevel;
    private GameActivity gameActivity;

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        if (!this.isInEditMode())
            this.gameActivity = (GameActivity) context;
        getHolder().addCallback(this);
        currentLevel = new FirstLevel(3f);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE|SYSTEM_UI_FLAG_FULLSCREEN|SYSTEM_UI_FLAG_HIDE_NAVIGATION|SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void pause(){
        thread.setRunning(false);
    }

    public void resume(){
        setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE|SYSTEM_UI_FLAG_FULLSCREEN|SYSTEM_UI_FLAG_HIDE_NAVIGATION|SYSTEM_UI_FLAG_LAYOUT_STABLE);
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
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
            gameActivity.goBackToMenu();
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
        currentLevel = new FirstLevel(3f);
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
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) { //???????
        if (visibility == GONE) {
            endThread();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
