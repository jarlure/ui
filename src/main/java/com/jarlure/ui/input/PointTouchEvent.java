package com.jarlure.ui.input;

public class PointTouchEvent {

    private final int pointerId;
    public final float startX, startY;
    public final float x, y;
    public final float dx, dy;
    private final float pressure;//压力
    private final int numberOfPointer;
    private boolean consumed;

    public PointTouchEvent(int pointerId, float x, float y, float pressure,int numberOfPointer) {
        this.pointerId = pointerId;
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.pressure = pressure;
        this.numberOfPointer = numberOfPointer;
    }

    public PointTouchEvent(int pointerId, float startX, float startY, float x, float y, float pressure,int numberOfPointer) {
        this.pointerId = pointerId;
        this.startX = startX;
        this.startY = startY;
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.pressure = pressure;
        this.numberOfPointer =numberOfPointer;
    }

    public PointTouchEvent(int pointerId, float startX, float startY, float x, float y, float dx, float dy, float pressure,int numberOfPointer) {
        this.pointerId = pointerId;
        this.startX = startX;
        this.startY = startY;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.pressure = pressure;
        this.numberOfPointer =numberOfPointer;
    }

    public int getPointerId() {
        return pointerId;
    }

    public float getPressX() {
        return startX;
    }

    public float getPressY() {
        return startY;
    }

    public float getReleaseX() {
        return x;
    }

    public float getReleaseY() {
        return y;
    }

    public float getPressure() {
        return pressure;
    }

    /**
     * 获取当前触点数量
     * @return  当前触点数量
     */
    public int getNumberOfPointer() {
        return numberOfPointer;
    }

    /**
     * 设置已有监听器处理该事件标记。注意：触屏输入管理器不会读取该标记。每个监听器需要自己判断是否存在该标记以决定是否
     * 处理该事件。
     */
    public void setConsumed() {
        consumed = true;
    }

    /**
     * 重置已有监听器处理该事件标记。
     */
    public void resetConsumed() {
        consumed = false;
    }

    @Override
    public String toString() {
        return "PointTouchEvent[pointerId=" + pointerId + "；" +
                "start(" + startX + ',' + startY + ")；" +
                "current(" + x + ',' + y + ")；" +
                "delta(" + dx + ',' + dy + ")；" +
                "pressure=" + pressure + "；" +
                "numberOfPointer=" + numberOfPointer + "；" +
                "consumed=" + consumed+"]";
    }
}
