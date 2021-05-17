package com.bloniarz.faster.game.levels;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;
import com.bloniarz.faster.game.objects.SensorPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Level4 implements SensorEventListener, Level{
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final float width = 100, height = 100, speed, smallWidth = 50, smallHeight = 50;
    private SensorManager sensorManager;
    private final Point point;
    private final List<Point> points = new ArrayList<>();
    private float timeElapsed = 0;
    private final float maxTime = 10*1000;
    private float addTime = 500, addTimeElapsed = 0;
    private final Random random = new Random();
    private final int badColor;
    private final ProgressBar progressBar;

    public Level4(Context context, float speed, int badColor){
        this.speed = speed;
        this.badColor = badColor;
        progressBar = new ProgressBar(screenWidth, 50, Color.GRAY, maxTime);
        point = new SensorPoint(screenWidth/2-width/2, screenHeight-height, screenWidth/2+width/2, screenHeight,  Color.BLACK, 0, 0);
        addPoints();
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            canvas.drawRect(point, point.getPaint());
            for(Point p: points){
                canvas.drawRect(p, p.getPaint());
            }
            canvas.drawRect(progressBar, progressBar.getPaint());
        }
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        addTimeElapsed += time;
        if (addTimeElapsed >= addTime){
            addPoints();
            addTimeElapsed = 0;
        }
        if (timeElapsed >= maxTime)
            return State.PASSED;
        point.update(time);
        Iterator<Point> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point p = iterator.next();
            p.update(time);
            if (RectF.intersects(point, p))
                return State.LOST;
            if (p.bottom>=screenHeight)
                iterator.remove();
        }
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    void addPoints(){
        for (int i=0; i<3; i++){
            float x = screenWidth*random.nextFloat(), y = 300*random.nextFloat();
            points.add(new Point(x, y, x+smallWidth, y+smallHeight, badColor, 0, (0.5f+random.nextFloat()/2)*800*speed));
        }
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
        return "Run!";
    }

    @Override
    public String getLevelDescription() {
        return "Avoid falling objects";
    }
}
