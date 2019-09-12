package com.jarlure.ui.input;

public interface KeyInputListener {

    /**
     * 当有非控制键（ctrl、shift、alt）按下时触发
     *
     * @param key 键盘按键事件
     */
    default void onKeyPressed(KeyEvent key) {
    }

    /**
     * 当持续按压非控制键（ctrl、shift、alt）时触发
     *
     * @param key 键盘按键事件
     */
    default void onKeyPressing(KeyEvent key) {
    }

    /**
     * 当有非控制键（ctrl、shift、alt）弹起时触发
     *
     * @param key 键盘按键事件
     */
    default void onKeyReleased(KeyEvent key) {
    }

    default void foldAnonymousInnerClassCode(KeyInputListener instance) {
    }

}