package com.bloniarz.faster.game.levels;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Ball;
import com.bloniarz.faster.game.objects.JumpingBall;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.Random;

public class Level6 implements SensorEventListener, Level {
    private float x, y, xVelocity = 0, yVelocity = 0;
    private final float radius = 5*unit;
    private final JumpingBall mainBall;
    private SensorManager sensorManager;
    private float holeX = 20*unit, holeY = 20*unit, holeRadius = 10*unit;
    private final float speed;
    private final ProgressBar progressBar;
    private final float maxTime = 7*1000;
    private float timeElapsed = 0;
    private final Ball hole;
    private final Random random = new Random();

    public Level6(Context context, float speed, int neutralColor, int playerColor){
        x = screenWidth/2;
        y = screenHeight-radius-30*unit;
        holeX = (random.nextFloat()*0.8f+0.1f)*screenWidth;
        this.speed = speed;
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
        hole = new Ball(holeX, holeY, holeRadius, neutralColor, 0, 0);
        mainBall = new JumpingBall(x, y, radius, playerColor, xVelocity, yVelocity);
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            hole.draw(canvas);
            mainBall.draw(canvas);
            progressBar.draw(canvas);
        }
    }

    @Override
    public synchronized boolean touch(MotionEvent event) {
        return false;
    }

    @Override
    public synchronized State update(float time) {
        timeElapsed += time;
        mainBall.update(time);
        if (hole.contains(mainBall))
            return State.PASSED;
        if (timeElapsed >= maxTime)
            return State.LOST;
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mainBall.setXAcceleration(-sensorEvent.values[0]*150*unit*speed);
        mainBall.setYAcceleration(sensorEvent.values[1]*150*unit*speed);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public String getLevelName() {
        return "Golf";
    }

    @Override
    public String getLevelDescription() {
        return "Put the ball\nin the hole";
    }
}
