package com.bloniarz.faster.game.objects;

import com.bloniarz.faster.game.levels.Level;

public class JumpingPoint extends Point {
    private float timeElapsed = 0;
    private boolean jumping = false;
    private final float initialTop;
    private final float speed;


    public JumpingPoint(float left, float top, float right, float bottom, int color, float speed) {
        super(left, top, right, bottom, color, 0, 0);
        initialTop = top;
        this.speed = speed;
    }

    @Override
    public void update(float time) {
        if (jumping){
            timeElapsed += time;
            this.offsetTo(this.left, initialTop-f(timeElapsed));
            if (this.top>initialTop){
                this.offsetTo(this.left, initialTop);
                jumping = false;
            }
        }
    }

    public void jump(){
        if (!jumping){
            jumping = true;
            timeElapsed = 0;
        }
    }

    float f(float x){
        float a = (float)Math.sqrt(speed), b = (float)Math.sqrt(Level.unit), c = 5;
        x = ((speed*b)/100f*x-c*b);
        return -(x*x)+c*c*b*b;
    }
}
