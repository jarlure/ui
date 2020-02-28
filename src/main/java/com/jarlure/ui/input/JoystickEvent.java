package com.jarlure.ui.input;

public class JoystickEvent {

    private final int joystickId;
    public final int buttonIndex;
    public final int axisIndex;
    public final float value;
    private boolean consumed;

    public JoystickEvent(int joystickId,int buttonIndex){
        this.joystickId=joystickId;
        this.buttonIndex=buttonIndex;
        this.axisIndex=-1;
        this.value=0;
    }

    public JoystickEvent(int joystickId,int axisIndex,float value){
        this.joystickId=joystickId;
        this.buttonIndex=-1;
        this.axisIndex=axisIndex;
        this.value=value;
    }

    public int getJoystickId() {
        return joystickId;
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
        return  "JoystickEvent[joystickId=" + joystickId + "；" +
                "buttonIndex=" + buttonIndex + "；" +
                "axisIndex=" + axisIndex + "；" +
                "value=" + value + "；" +
                "consumed=" + consumed+"]";
    }

}
