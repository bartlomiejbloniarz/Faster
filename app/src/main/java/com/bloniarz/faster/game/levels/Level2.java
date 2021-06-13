package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Level2 implements Level {

    private final float width = 20*unit, height = width, speed;
    private final List<Point> stillPoints = new LinkedList<>(), movingPoints = new LinkedList<>();
    private final int maxCount;
    private final Random random;
    private float timeElapsed = 0;
    private final int tileColor;
    private final Paint textPaint;


    public Level2(float speed, int neutralColor, int textColor){
        maxCount = 10;
        random = new Random();
        this.speed = speed;
        this.tileColor = neutralColor;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(20*unit);
        textPaint.setTextAlign(Paint.Align.CENTER);
        for(int i=0; i<8; i++)
            addRandomPoint();
    }

    void addRandomPoint(){
        float x1 = random.nextFloat()*screenWidth, y1 = (random.nextFloat()*0.9f+0.1f)*screenHeight;
        if (x1+width>=screenWidth)
            x1-=width;
        if (y1+height>=screenHeight)
            y1-=height;
        if (random.nextBoolean()){
            stillPoints.add(new Point(x1, y1, x1+width, y1+height, tileColor, 0, 0));
        }
        else{
            movingPoints.add(new Point(x1, y1, x1+width, y1+height, tileColor, (random.nextInt(50*(int)unit)-25*unit)*speed, (random.nextInt(50*(int)unit)-25*unit)*speed));
        }
    }


    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            for (Point point: stillPoints)
                point.draw(canvas);
            for (Point point: movingPoints)
                point.draw(canvas);
            canvas.drawText(String.format(Locale.US, "%d", stillPoints.size()+movingPoints.size()), screenWidth/2, screenHeight/5, textPaint);
        }
    }

    @Override
    public synchronized State update(float time) {
        for (Point point: movingPoints) {
            point.update(time);
        }
        timeElapsed += time;
        if (timeElapsed>500){
            addRandomPoint();
            timeElapsed = 0;
        }
        if (stillPoints.size() + movingPoints.size() >= maxCount)
            return State.LOST;
        if (stillPoints.size() + movingPoints.size() == 0)
            return State.PASSED;
        return State.RUNNING;

    }

    @Override
    public synchronized boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                for (Iterator<Point> i = stillPoints.iterator(); i.hasNext();){
                    Point element = i.next();
                    if ((element.left <= event.getX(actionIndex)) && (event.getX(actionIndex) <= element.right)
                    && (element.top <= event.getY(actionIndex)) && (event.getY(actionIndex) <= element.bottom)) {
                        i.remove();
                    }
                }
                for (Iterator<Point> i = movingPoints.iterator(); i.hasNext();){
                    Point element = i.next();
                    if ((element.left <= event.getX(actionIndex)) && (event.getX(actionIndex) <= element.right)
                            && (element.top <= event.getY(actionIndex)) && (event.getY(actionIndex) <= element.bottom)) {
                        i.remove();
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Tap tap tap!";
    }

    @Override
    public String getLevelDescription() {
        return String.format(Locale.US, "Tap all the squares.\nDon't let the counter\nget to %d!", maxCount);
    }
}
