package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Level7 implements Level {

    private final List<Point> tiles = new ArrayList<>();
    private final int tilesCount = 5;
    private final float width = 20*unit, height = width;

    private final Random random = new Random();
    private final ProgressBar progressBar;
    private float showTime = 3*1000;
    private boolean show = true;
    private float timeElapsed = 0;
    private final Paint textPaint;
    private boolean lost = false;

    public Level7(float speed, int goodColor, int neutralColor, int textColor){
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(5*unit);
        textPaint.setTextAlign(Paint.Align.CENTER);
        showTime /= speed;
        progressBar = new ProgressBar(screenWidth, 5*unit, goodColor, showTime);
        for (int i=0; i<tilesCount; i++){
            Point tile;
            do {
                float x = random.nextFloat() * screenWidth, y = random.nextFloat() * screenHeight;
                if (x+width>screenWidth)
                    x-=width;
                if (y+height>screenHeight)
                    y-=height;
                tile = new Point(x, y, x+width, y+height, neutralColor, 0, 0);
            } while(anyIntersects(tile));
            tiles.add(tile);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (show){
            for (int i=0; i<tilesCount; i++){
                tiles.get(i).draw(canvas);
                canvas.drawText(String.format(Locale.US, "%d", i+1), tiles.get(i).centerX(), tiles.get(i).centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
                progressBar.draw(canvas);
            }
        }
        else{
            for (Point tile: tiles){
                tile.draw(canvas);
            }
        }
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        if (lost)
            return State.LOST;
        if (tiles.size() == 0)
            return State.PASSED;
        if (timeElapsed >= showTime)
            show = false;
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (!show) {
                    if (tiles.get(0).contains(event.getX(actionIndex), event.getY(actionIndex))) {
                        tiles.remove(0);
                    } else {
                        for (Point tile : tiles) {
                            if (tile.contains(event.getX(actionIndex), event.getY(actionIndex))) {
                                lost = true;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Memorize!";
    }

    @Override
    public String getLevelDescription() {
        return "Tap the tiles in\nthe correct order.";
    }

    private boolean anyIntersects(Point tile){
        for (Point p: tiles){
            if (RectF.intersects(tile, p))
                return true;
        }
        return false;
    }
}
