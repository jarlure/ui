package com.jarlure.ui.system;

import com.jarlure.ui.input.JoystickEvent;
import com.jarlure.ui.input.JoystickInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.util.SafeArrayList;

public final class JoystickInputManager {

    private SafeArrayList<JoystickInputListener> queue=new SafeArrayList<>(JoystickInputListener.class);

    public void add(JoystickInputListener listener){
        if (queue == null) return;
        queue.add(0, listener);
    }

    public void remove(JoystickInputListener listener) {
        if (queue == null) return;
        queue.remove(listener);
    }

    public void onJoyAxisEvent(JoyAxisEvent evt){
        JoystickEvent joystick = new JoystickEvent(evt.getJoyIndex(),evt.getAxisIndex(),evt.getValue());
        joystick.setTime(evt.getTime());
        for (JoystickInputListener listener : queue){
            listener.onAxisMove(joystick);
        }
        //TODO:需要更好的手柄来测试evt中isAnalog、isRelative以及getDeadZone值的效果
    }

    public void onJoyButtonEvent(JoyButtonEvent evt){
        JoystickEvent joystick = new JoystickEvent(evt.getJoyIndex(),evt.getButtonIndex());
        joystick.setTime(evt.getTime());
        if (evt.isPressed()){
            for (JoystickInputListener listener : queue){
                listener.onButtonPressed(joystick);
            }
        }else{
            for (JoystickInputListener listener:queue){
                listener.onButtonReleased(joystick);
            }
        }
    }

}
