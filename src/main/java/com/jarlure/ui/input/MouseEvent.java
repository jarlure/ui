package com.jarlure.ui.input;

import com.jme3.input.event.InputEvent;

public class MouseEvent extends InputEvent {

    public final int startX, startY;
    public final int x, y;
    public final int dx, dy, dw;
    private final int buttonIndex;
    private final boolean isPressed;

    public MouseEvent(int x,int y,int buttonIndex,boolean isPressed){
        this.startX=x;
        this.startY=y;
        this.x=x;
        this.y=y;
        this.dx=0;
        this.dy=0;
        this.dw=0;
        this.buttonIndex=buttonIndex;
        this.isPressed=isPressed;
    }

    public MouseEvent(int startX,int startY,int x,int y,int buttonIndex,boolean isPressed){
        this.startX=startX;
        this.startY=startY;
        this.x=x;
        this.y=y;
        this.dx=0;
        this.dy=0;
        this.dw=0;
        this.buttonIndex=buttonIndex;
        this.isPressed=isPressed;
    }

    public MouseEvent(int startX,int startY,int x,int y,int dx,int dy,int buttonIndex,boolean isPressed){
        this.startX=startX;
        this.startY=startY;
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.dw=0;
        this.buttonIndex=buttonIndex;
        this.isPressed=isPressed;
    }

    public MouseEvent(int x,int y,int dx,int dy){
        this.startX=x;
        this.startY=y;
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.dw=0;
        this.buttonIndex=-1;
        this.isPressed=false;
    }

    public MouseEvent(int x,int y,int dw){
        this.startX=x;
        this.startY=y;
        this.x=x;
        this.y=y;
        this.dx=0;
        this.dy=0;
        this.dw=dw;
        this.buttonIndex=-1;
        this.isPressed=false;
    }

    public MouseEvent(int x,int y){
        this.startX=x;
        this.startY=y;
        this.x=x;
        this.y=y;
        this.dx=0;
        this.dy=0;
        this.dw=0;
        this.buttonIndex=-1;
        this.isPressed=false;
    }

    public int getPressX() {
        return startX;
    }

    public int getPressY() {
        return startY;
    }

    public int getReleaseX() {
        return x;
    }

    public int getReleaseY() {
        return y;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public int getButtonIndex() {
        return buttonIndex;
    }

    /**
     * 重置已有监听器处理该事件标记。
     */
    public void resetConsumed() {
        consumed = false;
    }

    @Override
    public String toString() {
        return "MouseEvent[start(" + startX + ',' + startY + ")；" +
                "current(" + x + ',' + y + ")；" +
                "delta(" + dx + ',' + dy + ',' + dw + ")；" +
                "buttonIndex=" + buttonIndex + "；" +
                "isPressed=" + isPressed + "；" +
                "consumed=" + consumed+"]";
    }

}