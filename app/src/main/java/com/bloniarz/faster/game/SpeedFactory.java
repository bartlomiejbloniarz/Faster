package com.bloniarz.faster.game;

public class SpeedFactory implements ISpeedFactory {

    private float speed, quotient, start;

    public SpeedFactory(float start, float quotient){
        speed = start;
        this.start = start;
        this.quotient = quotient;
    }

    @Override
    public float getNextSpeed() {
        float temp = speed;
        speed *= quotient;
        speed += start;
        return temp;
    }
}
