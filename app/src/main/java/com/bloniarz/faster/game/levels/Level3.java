package com.bloniarz.faster.game.levels;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;
import com.bloniarz.faster.game.objects.SensorPoint;

import java.util.Locale;

public class Level3 implements SensorEventListener, Level {

    private final float width = 20*unit, height = width, speed;
    private SensorManager sensorManager;
    private Point point, leftBorder, rightBorder;
    private float timeElapsed = 0;
    private final float maxTime = 5*1000;
    private final ProgressBar progressBar;

    public Level3(Context context, float speed, int badColor, int playerColor){
        this.speed = speed;
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
        point = new SensorPoint(screenWidth/2-width/2, screenHeight/2-height/2, screenWidth/2+width/2, screenHeight/2+height/2,  playerColor, 0, 0);
        rightBorder = new Point(screenWidth/2+width, screenHeight/2-height, screenWidth/2+2*width, screenHeight/2+height,  badColor, 0, 0);
        leftBorder = new Point(screenWidth/2-2*width, screenHeight/2-height, screenWidth/2-width, screenHeight/2+height,  badColor, 0, 0);

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            progressBar.draw(canvas);
            point.draw(canvas);
            leftBorder.draw(canvas);
            rightBorder.draw(canvas);
        }
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        if (timeElapsed >= maxTime)
            return State.PASSED;
        point.update(time);
        if (RectF.intersects(point, leftBorder) || RectF.intersects(point, rightBorder))
            return State.LOST;
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        point.setXVelocity(-sensorEvent.values[0]*30*unit*speed);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public String getLevelName() {
        return "Don't move!";
    }

    @Override
    public String getLevelDescription() {
        return "Red borders will kill you";
    }
}
