package com.bloniarz.faster.game;

public class SpeedFactory implements ISpeedFactory {

    private float speed, quotient, start, bias;

    public SpeedFactory(float start, float quotient, float bias){
        speed = start;
        this.start = start;
        this.quotient = quotient;
        this.bias = bias;
    }

    @Override
    public float getNextSpeed() {
        float temp = speed;
        speed *= quotient;
        speed += start;
        return temp + bias;
    }
}
