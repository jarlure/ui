package com.jarlure.ui.input;

import com.jme3.input.event.InputEvent;

public class LineTouchEvent extends InputEvent {

    private final int pointerId;
    public final float startX0, startY0;
    public final float x0, y0;
    public final float startX1, startY1;
    public final float x1, y1;
    public final float dx, dy;
    private final int numberOfPointer;

    public LineTouchEvent(int pointerId, float startX0, float startY0, float x0, float y0, float startX1, float startY1, float x1, float y1, int numberOfPointer) {
        this.pointerId = pointerId;
        this.startX0 = startX0;
        this.startY0 = startY0;
        this.x0 = x0;
        this.y0 = y0;
        this.startX1 = startX1;
        this.startY1 = startY1;
        this.x1 = x1;
        this.y1 = y1;
        this.dx = 0;
        this.dy = 0;
        this.numberOfPointer=numberOfPointer;
    }

    public LineTouchEvent(int pointerId, float startX0, float startY0, float x0, float y0, float startX1, float startY1, float x1, float y1, float dx, float dy, int numberOfPointer) {
        this.pointerId = pointerId;
        this.startX0 = startX0;
        this.startY0 = startY0;
        this.x0 = x0;
        this.y0 = y0;
        this.startX1 = startX1;
        this.startY1 = startY1;
        this.x1 = x1;
        this.y1 = y1;
        this.dx = dx;
        this.dy = dy;
        this.numberOfPointer = numberOfPointer;
    }

    public int getPointerId() {
        return pointerId;
    }

    public float getPressX0() {
        return startX0;
    }

    public float getPressY0() {
        return startY0;
    }

    public float getReleaseX0() {
        return x0;
    }

    public float getReleaseY0() {
        return y0;
    }

    public float getPressX1() {
        return startX1;
    }

    public float getPressY1() {
        return startY1;
    }

    public float getReleaseX1() {
        return x1;
    }

    public float getReleaseY1() {
        return y1;
    }

    /**
     * 获取当前触点数量
     * @return  当前触点数量
     */
    public int getNumberOfPointer() {
        return numberOfPointer;
    }

    /**
     * 重置已有监听器处理该事件标记。
     */
    public void resetConsumed() {
        consumed = false;
    }

    @Override
    public String toString() {
        return "LineTouchEvent[pointerId=" + pointerId + "；" +
                "start0(" + startX0 + ',' + startY0 + ")；" +
                "current0(" + x0 + ',' + y0 + ")；" +
                "start1(" + startX1 + ',' + startY1 + ")；" +
                "current1(" + x1 + ',' + y1 + ")；" +
                "delta(" + dx + ',' + dy + ")；" +
                "numberOfPointer=" + numberOfPointer + "；" +
                "consumed=" + consumed+"]";
    }

}
