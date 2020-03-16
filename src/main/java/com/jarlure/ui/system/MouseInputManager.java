package com.jarlure.ui.system;

import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputListener;
import com.jme3.input.MouseInput;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.system.JmeSystem;
import com.jme3.util.SafeArrayList;

public final class MouseInputManager {

    private static final int NULL = -1;
    private long clickMaxDelay;
    private long doubleClickMaxDelay;
    private int pressedButtonIndex = NULL;
    private long timeWhenMove = NULL;
    private long timeWhenPress = NULL;
    private long timeWhenClick = NULL;
    private float timeLongPressing = NULL;
    private float timeLongStay = NULL;
    private int press_x = NULL, press_y = NULL;
    private int move_x = NULL, move_y = NULL;
    private SafeArrayList<MouseInputListener> queue = new SafeArrayList<>(MouseInputListener.class);

    public void add(MouseInputListener listener) {
        queue.add(0, listener);
    }

    public void remove(MouseInputListener listener) {
        queue.remove(listener);
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) {
        //不对滚轮键的按下、释放做处理
        if (evt.getButtonIndex() == MouseInput.BUTTON_MIDDLE) return;
        //如果之前没有鼠标按键按下，则执行按下操作
        if (evt.isPressed()) {
            //记录
            pressedButtonIndex = evt.getButtonIndex();
            press_x = evt.getX();
            press_y = evt.getY();
            timeWhenPress = evt.getTime();
            timeLongPressing = 0;

            MouseEvent mouse = new MouseEvent(evt.getX(), evt.getY(), pressedButtonIndex, true);
            mouse.setTime(evt.getTime());
            switch (pressedButtonIndex) {
                case MouseInput.BUTTON_LEFT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onLeftButtonPress(mouse);
                    }
                    break;
                case MouseInput.BUTTON_RIGHT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onRightButtonPress(mouse);
                    }
                    break;
            }
            return;
        }
        //如果之前按下的鼠标按键就是这个键，则执行释放操作
        if (evt.isReleased() && pressedButtonIndex == evt.getButtonIndex()) {
            long dt = evt.getTime() - timeWhenPress;
            boolean isClicked = dt < clickMaxDelay && press_x == evt.getX() && press_y == evt.getY();
            boolean isDoubleClicked = false;
            if (isClicked) {
                if (timeWhenClick == NULL) timeWhenClick = evt.getTime();
                else {
                    dt = evt.getTime() - timeWhenClick;
                    if (dt < doubleClickMaxDelay) {
                        isDoubleClicked = true;
                        timeWhenClick = NULL;
                    } else {
                        timeWhenClick = evt.getTime();
                    }
                }
            } else {
                timeWhenClick = NULL;
            }

            MouseEvent mouse = new MouseEvent(press_x, press_y, evt.getX(), evt.getY(), evt.getButtonIndex(), false);
            mouse.setTime(evt.getTime());
            switch (pressedButtonIndex) {
                case MouseInput.BUTTON_LEFT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onLeftButtonRelease(mouse);
                    }
                    if (isClicked) {
                        mouse.resetConsumed();
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onLeftButtonClick(mouse);
                        }
                    }
                    if (isDoubleClicked) {
                        mouse.resetConsumed();
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onLeftButtonDoubleClick(mouse);
                        }
                    }
                    break;
                case MouseInput.BUTTON_RIGHT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onRightButtonRelease(mouse);
                    }
                    if (isClicked) {
                        mouse.resetConsumed();
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onRightButtonClick(mouse);
                        }
                    }
                    if (isDoubleClicked) {
                        mouse.resetConsumed();
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onRightButtonDoubleClick(mouse);
                        }
                    }
                    break;
            }
        }
        //清除
        pressedButtonIndex = NULL;
        press_x = press_y = NULL;
        timeWhenPress = NULL;
        timeLongPressing = NULL;
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if (evt.getDX() != 0 || evt.getDY() != 0) {
            move_x = evt.getX();
            move_y = evt.getY();
            timeWhenMove = evt.getTime();
            timeLongStay = 0;
            MouseEvent mouse = null;
            if (pressedButtonIndex != NULL) {
                if (timeLongPressing != NULL) timeLongPressing = NULL;
                //鼠标拖拽
                mouse = new MouseEvent(press_x, press_y, evt.getX(), evt.getY(), evt.getDX(), evt.getDY(), pressedButtonIndex, true);
                mouse.setTime(evt.getTime());
                switch (pressedButtonIndex) {
                    case MouseInput.BUTTON_LEFT:
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onLeftButtonDragging(mouse);
                        }
                        break;
                    case MouseInput.BUTTON_RIGHT:
                        for (MouseInputListener listener : queue.getArray()) {
                            listener.onRightButtonDragging(mouse);
                        }
                        break;
                }
            }
            //鼠标移动
            if (mouse == null) {
                mouse = new MouseEvent(evt.getX(), evt.getY(), evt.getDX(), evt.getDY());
                mouse.setTime(evt.getTime());
            } else mouse.resetConsumed();
            for (MouseInputListener listener : queue.getArray()) {
                listener.onMove(mouse);
            }
            return;
        }
        if (evt.getDeltaWheel() != 0) {
            MouseEvent mouse = new MouseEvent(evt.getX(), evt.getY(), evt.getDeltaWheel());
            mouse.setTime(evt.getTime());
            for (MouseInputListener listener : queue.getArray()) {
                listener.onWheelRolling(mouse);
            }
        }
    }

    public void initialize(){
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                clickMaxDelay=150000000;
                doubleClickMaxDelay=200000000;
                break;
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                clickMaxDelay=150;
                doubleClickMaxDelay=200;
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void update(float tpf) {
        updateLongPressTime(tpf);
        updateLongStayTime(tpf);
    }

    private void updateLongPressTime(float tpf) {
        if (timeLongPressing == NULL) return;
        timeLongPressing += tpf;
        if (timeLongPressing > 0.5f) {
            timeLongPressing = NULL;
            MouseEvent mouse = new MouseEvent(press_x, press_y, pressedButtonIndex, true);
            mouse.setTime(timeWhenPress);
            switch (pressedButtonIndex) {
                case MouseInput.BUTTON_LEFT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onLeftButtonPressLongTime(mouse);
                    }
                    break;
                case MouseInput.BUTTON_RIGHT:
                    for (MouseInputListener listener : queue.getArray()) {
                        listener.onRightButtonPressLongTime(mouse);
                    }
                    break;
            }
        }
    }

    private void updateLongStayTime(float tpf) {
        if (timeLongStay == NULL) return;
        timeLongStay += tpf;
        if (timeLongStay > 0.5f) {
            timeLongStay = NULL;
            MouseEvent mouse = new MouseEvent(move_x, move_y);
            mouse.setTime(timeWhenMove);
            for (MouseInputListener listener : queue) {
                listener.onStayLongTime(mouse);
            }
        }
    }

}