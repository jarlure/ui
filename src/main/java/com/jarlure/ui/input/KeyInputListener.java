package com.jarlure.ui.input;

import com.jme3.input.controls.InputListener;

public interface KeyInputListener extends InputListener {

    /**
     * 当有非控制键（ctrl、shift、alt）按下时触发
     *
     * @param key 键盘按键事件
     */
    void onKeyPressed(KeyEvent key) ;

    /**
     * 当持续按压非控制键（ctrl、shift、alt）时触发
     *
     * @param key 键盘按键事件
     */
    void onKeyPressing(KeyEvent key) ;

    /**
     * 当有非控制键（ctrl、shift、alt）弹起时触发
     *
     * @param key 键盘按键事件
     */
    void onKeyReleased(KeyEvent key) ;

}