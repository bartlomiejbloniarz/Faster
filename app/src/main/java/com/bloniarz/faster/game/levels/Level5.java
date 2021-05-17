package com.bloniarz.faster.game.levels;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Random;

public class Level5 implements Level{

    private final Random random;
    private final float speed;

    public Level5(float speed){
        random = new Random();
        this.speed = speed;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public State update(float time) {
        return null;
    }

    @Override
    public boolean touch(MotionEvent event) {
        return false;
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public String getLevelDescription() {
        return null;
    }
}
