package com.jarlure.ui.input;

import com.jme3.input.event.InputEvent;

public class JoystickEvent extends InputEvent {

    private final int joystickId;
    public final int buttonIndex;
    public final int axisIndex;
    public final float value;

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
