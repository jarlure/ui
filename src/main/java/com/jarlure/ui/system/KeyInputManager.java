package com.jarlure.ui.system;

import com.jarlure.ui.input.KeyEvent;
import com.jarlure.ui.input.KeyInputListener;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.util.SafeArrayList;

public final class KeyInputManager {

    private boolean isLShiftPressed;
    private boolean isRShiftPressed;
    private boolean isLCtrlPressed;
    private boolean isRCtrlPressed;
    private boolean isLAltPressed;
    private boolean isRAltPressed;
    private SafeArrayList<KeyInputListener> queue;

    public void add(KeyInputListener listener) {
        if (queue == null) queue = new SafeArrayList<>(KeyInputListener.class);
        queue.add(0, listener);
    }

    public void remove(KeyInputListener listener) {
        if (queue == null) return;
        queue.remove(listener);
    }

    public void onKeyEvent(KeyInputEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyInput.KEY_LSHIFT:
                isLShiftPressed = evt.isPressed();
                break;
            case KeyInput.KEY_RSHIFT:
                isRShiftPressed = evt.isPressed();
                break;
            case KeyInput.KEY_LCONTROL:
                isLCtrlPressed = evt.isPressed();
                break;
            case KeyInput.KEY_RCONTROL:
                isRCtrlPressed = evt.isPressed();
                break;
            case KeyInput.KEY_LMENU:
                isLAltPressed = evt.isPressed();
                break;
            case KeyInput.KEY_RMENU:
                isRAltPressed = evt.isPressed();
                break;
        }

        if (evt.isPressed()) {
            if (evt.isRepeating()) {
                KeyEvent key = new KeyEvent(evt.getKeyCode(), evt.getKeyChar(), isLShiftPressed, isRShiftPressed, isLCtrlPressed, isRCtrlPressed, isLAltPressed, isRAltPressed);
                for (KeyInputListener listener : queue.getArray()) {
                    listener.onKeyPressing(key);
                }
            } else {
                KeyEvent key = new KeyEvent(evt.getKeyCode(), evt.getKeyChar(), isLShiftPressed, isRShiftPressed, isLCtrlPressed, isRCtrlPressed, isLAltPressed, isRAltPressed);
                for (KeyInputListener listener : queue.getArray()) {
                    listener.onKeyPressed(key);
                }
            }
        } else {
            KeyEvent key = new KeyEvent(evt.getKeyCode(), evt.getKeyChar(), isLShiftPressed, isRShiftPressed, isLCtrlPressed, isRCtrlPressed, isLAltPressed, isRAltPressed);
            for (KeyInputListener listener : queue.getArray()) {
                listener.onKeyReleased(key);
            }
        }
    }

    public void initialize(Application app) {
        queue = new SafeArrayList<>(KeyInputListener.class);
    }

    public void cleanup() {
    }

    public void update(float tpf) {
    }

}