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

import com.bloniarz.faster.game.objects.JumpingBall;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.Random;

public class Level6 implements SensorEventListener, Level {
    private float x, y, xVelocity = 0, yVelocity = 0;
    private final float width = 10*unit, height = width;
    private final JumpingBall mainRect;
    private SensorManager sensorManager;
    private float holeX = 20*unit, holeY = 20*unit, holeRadius = 10*unit;
    private final float speed;
    private final ProgressBar progressBar;
    private final float maxTime = 7*1000;
    private float timeElapsed = 0;
    private Paint neutralPaint;
    private final Random random = new Random();

    public Level6(Context context, float speed, int neutralColor, int playerColor){
        x = (screenWidth - width)/2;
        y = screenHeight-height-unit;
        holeX = (random.nextFloat()*0.8f+0.1f)*screenWidth;
        this.speed = speed;
        neutralPaint = new Paint();
        neutralPaint.setColor(neutralColor);
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
        mainRect = new JumpingBall(x, y, x+width, y+height, playerColor, xVelocity, yVelocity);
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            canvas.drawCircle(holeX, holeY, holeRadius, neutralPaint);
            canvas.drawCircle(mainRect.getCenterX(), mainRect.getCenterY(), mainRect.getRadius(), mainRect.getPaint());
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
        mainRect.update(time);
        if ((mainRect.getCenterX() - holeX)*(mainRect.getCenterX() - holeX) + (mainRect.getCenterY() - holeY)*(mainRect.getCenterY() - holeY) < (holeRadius - mainRect.getRadius())*(holeRadius - mainRect.getRadius()))
            return State.PASSED;
        if (timeElapsed >= maxTime)
            return State.LOST;
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mainRect.setXAcceleration(-sensorEvent.values[0]*200*unit*speed);
        mainRect.setYAcceleration(sensorEvent.values[1]*200*unit*speed);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public String getLevelName() {
        return "Golf?";
    }

    @Override
    public String getLevelDescription() {
        return "Put the ball\nin the hole";
    }
}
