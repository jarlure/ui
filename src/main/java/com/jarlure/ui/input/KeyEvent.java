package com.jarlure.ui.input;

import com.jme3.input.event.InputEvent;

public class KeyEvent extends InputEvent {

    private int code;
    private char value;
    private boolean isLShiftPressed;
    private boolean isRShiftPressed;
    private boolean isLCtrlPressed;
    private boolean isRCtrlPressed;
    private boolean isLAltPressed;
    private boolean isRAltPressed;

    public KeyEvent(int code, char value, boolean isLShiftPressed, boolean isRShiftPressed, boolean isLCtrlPressed, boolean isRCtrlPressed, boolean isLAltPressed, boolean isRAltPressed) {
        this.code = code;
        this.value = value;
        this.isLShiftPressed = isLShiftPressed;
        this.isRShiftPressed = isRShiftPressed;
        this.isLCtrlPressed = isLCtrlPressed;
        this.isRCtrlPressed = isRCtrlPressed;
        this.isLAltPressed = isLAltPressed;
        this.isRAltPressed = isRAltPressed;
    }

    /**
     * 获取按键值。详见com.jme3.input.KeyInput
     *
     * @return 按键值。
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取按键字符
     *
     * @return 按键字符
     */
    public char getValue() {
        return value;
    }

    public boolean isShiftPressed() {
        return isLShiftPressed || isRShiftPressed;
    }

    public boolean isCtrlPressed() {
        return isLCtrlPressed || isRCtrlPressed;
    }

    public boolean isAltPressed() {
        return isLAltPressed || isRAltPressed;
    }

    public boolean isAnyControlKeyPressed() {
        return isShiftPressed() || isCtrlPressed() || isAltPressed();
    }

    public boolean isShiftPressedOnly() {
        return isShiftPressed() && !isCtrlPressed() && !isAltPressed();
    }

    public boolean isCtrlPressedOnly() {
        return isCtrlPressed() && !isShiftPressed() && !isAltPressed();
    }

    public boolean isAltPressedOnly() {
        return isAltPressed() && !isShiftPressed() && !isCtrlPressed();
    }

    public boolean isCtrlAndAltPressed() {
        return isCtrlPressed() && isAltPressed();
    }

    public boolean isCtrlAndShiftPressed() {
        return isCtrlPressed() && isShiftPressed();
    }

    public boolean isAltAndShiftPressed() {
        return isAltPressed() && isShiftPressed();
    }

    public boolean isAllControlKeyPressed() {
        return isShiftPressed() && isCtrlPressed() && isAltPressed();
    }

    public boolean isLShiftPressed() {
        return isLShiftPressed;
    }

    public boolean isRShiftPressed() {
        return isRShiftPressed;
    }

    public boolean isLCtrlPressed() {
        return isLCtrlPressed;
    }

    public boolean isRCtrlPressed() {
        return isRCtrlPressed;
    }

    public boolean isLAltPressed() {
        return isLAltPressed;
    }

    public boolean isRAltPressed() {
        return isRAltPressed;
    }

    /**
     * 重置已有监听器处理该事件标记。
     */
    public void resetConsumed() {
        consumed = false;
    }

    @Override
    public String toString() {
        return "KeyEvent[code=" + code + '；' +
                "value=" + value + '；' +
                "isShiftPressed(" + isLShiftPressed + ',' + isRShiftPressed + ")；" +
                "isCtrlPressed(" + isLCtrlPressed + ',' + isRCtrlPressed + ")；" +
                "isAltPressed(" + isLAltPressed + ',' + isRAltPressed + ")；" +
                "consumed=" + consumed+"]";
    }

}
