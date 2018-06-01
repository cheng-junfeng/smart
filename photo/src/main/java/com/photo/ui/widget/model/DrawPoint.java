package com.photo.ui.widget.model;


public class DrawPoint {
    public float x;
    public float y;
    public boolean isStart;

    public DrawPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public DrawPoint(float x, float y, boolean isStart) {
        this.x = x;
        this.y = y;
        this.isStart = isStart;
    }
}
