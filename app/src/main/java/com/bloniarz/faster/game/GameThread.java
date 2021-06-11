package com.bloniarz.faster.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.bloniarz.faster.game.GameView;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private final AtomicBoolean running;
    public static Canvas canvas;
    int targetFPS = 60;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.running = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        long endTime = System.currentTimeMillis();
        long nextTime = endTime;
        while (running.get()){
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    nextTime = System.currentTimeMillis();
                    if (this.gameView.update((float)(nextTime - endTime))) {
                        this.gameView.draw(canvas);
                    }
                }
            } catch (Exception ignored) {}
            finally{
                if (canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                endTime = nextTime;
            }
        }
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}
