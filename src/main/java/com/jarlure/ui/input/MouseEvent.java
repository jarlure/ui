package com.jarlure.ui.input;

public class MouseEvent {

    public final int startX, startY;
    public final int x, y;
    public final int dx, dy, dw;
    private final int buttonIndex;
    private final boolean isPressed;
    private boolean consumed;

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
     * 判断是否已有监听器处理该事件。
     * @return  若有标记则返回true；否则返回false
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * 设置已有监听器处理该事件标记。注意：鼠标输入管理器不会读取该标记。每个监听器需要自己判断是否存在该标记以决定是否
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
        return "MouseEvent[start(" + startX + ',' + startY + ")；" +
                "current(" + x + ',' + y + ")；" +
                "delta(" + dx + ',' + dy + ',' + dw + ")；" +
                "buttonIndex=" + buttonIndex + "；" +
                "isPressed=" + isPressed + "；" +
                "consumed=" + consumed+"]";
    }

}