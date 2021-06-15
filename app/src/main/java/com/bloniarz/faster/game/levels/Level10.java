package com.bloniarz.faster.game.levels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Ball;
import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.Random;

public class Level10 implements Level{

    private final float ballRadius = 8*unit;
    private final Ball ball;
    private final float maxTime = 5*1000;
    private final ProgressBar progressBar;
    private boolean passed = false;
    private float timeElapsed = 0;
    private final Random random = new Random();

    public Level10(float speed, int neutralColor){
        ball = createBall(speed, neutralColor);
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
    }

    @Override
    public void draw(Canvas canvas) {
        ball.draw(canvas);
        progressBar.draw(canvas);
    }

    @Override
    public State update(float time) {
        if (passed)
            return State.PASSED;
        timeElapsed += time;
        if (timeElapsed >= maxTime)
            return State.LOST;
        ball.update(time);
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (ball.contains(event.getX(actionIndex), event.getY(actionIndex)))
                    passed = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Catch!";
    }

    @Override
    public String getLevelDescription() {
        return "Catch the flying ball";
    }

    private Ball createBall(float speed, int color){
        float x = ((random.nextFloat()*0.6f)+0.2f)*screenWidth, y = ((random.nextFloat()*0.6f)+0.2f)*screenHeight;
        float xVelocity = random.nextFloat(), yVelocity = random.nextFloat();
        float len = (float)Math.sqrt(xVelocity*xVelocity+yVelocity*yVelocity), desiredLen = 70*unit*speed;
        xVelocity = xVelocity * desiredLen / len;
        yVelocity = yVelocity * desiredLen / len;
        return new Ball(x, y, ballRadius, color, xVelocity, yVelocity);
    }
}
