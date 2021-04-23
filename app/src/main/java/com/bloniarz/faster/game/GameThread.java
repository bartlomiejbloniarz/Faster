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
        long startTime;
        long totalTime = 0;
        long timeMillis;
        int frameCount = 0;
        long waitTime;
        long endTime = System.nanoTime();
        long targetTime = 1000 / targetFPS;

        while (running.get()){
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gameView.update((float)(System.nanoTime() - endTime)/1000000);
                    this.gameView.draw(canvas);
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
                endTime = System.nanoTime();
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            //try {
            //    sleep(waitTime);
            //} catch (Exception ignored) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS){
                //System.out.println(1000 / ((totalTime / frameCount) / 1000000));
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}
