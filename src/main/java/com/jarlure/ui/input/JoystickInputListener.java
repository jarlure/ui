package com.jarlure.ui.input;

import com.jme3.input.controls.InputListener;

public interface JoystickInputListener extends InputListener {

    /**
     * 当游戏手柄的按钮按下时触发该事件
     *
     * @param joystick 游戏手柄事件
     */
    void onButtonPressed(JoystickEvent joystick);

    /**
     * 当游戏手柄的按钮释放时触发该事件
     *
     * @param joystick 游戏手柄事件
     */
    void onButtonReleased(JoystickEvent joystick);

    /**
     * 当游戏手柄的摇杆发生变化时触发该事件。目前尚未测试高级手柄
     *
     * @param joystick 游戏手柄事件
     */
    void onAxisMove(JoystickEvent joystick);

}
