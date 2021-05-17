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

    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final float width = 200, height = 200, speed;
    private SensorManager sensorManager;
    private Point point, leftBorder, rightBorder;
    private float timeElapsed = 0;
    private final float maxTime = 5*1000;
    private final Paint textPaint;
    private final ProgressBar progressBar;

    public Level3(Context context, float speed, int badColor){
        this.speed = speed;
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(200);
        textPaint.setTextAlign(Paint.Align.CENTER);
        progressBar = new ProgressBar(screenWidth, 50, Color.GRAY, maxTime);
        point = new SensorPoint(screenWidth/2-width/2, screenHeight/2-height/2, screenWidth/2+width/2, screenHeight/2+height/2,  Color.BLACK, 0, 0);
        rightBorder = new Point(screenWidth/2+width, screenHeight/2-height, screenWidth/2+2*width, screenHeight/2+height,  badColor, 0, 0);
        leftBorder = new Point(screenWidth/2-2*width, screenHeight/2-height, screenWidth/2-width, screenHeight/2+height,  badColor, 0, 0);

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            canvas.drawRect(progressBar, progressBar.getPaint());
            canvas.drawRect(point, point.getPaint());
            canvas.drawRect(leftBorder, leftBorder.getPaint());
            canvas.drawRect(rightBorder, rightBorder.getPaint());
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
        //System.out.println(sensorEvent.values[0]);
        point.setXVelocity(-sensorEvent.values[0]*300*speed);
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
