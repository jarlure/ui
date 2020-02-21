package com.jarlure.ui.system;

import com.jarlure.ui.input.KeyInputListener;
import com.jarlure.ui.input.MouseInputListener;
import com.jarlure.ui.input.TouchInputListener;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;

public class InputManager {

    private static InputState instance;

    /**
     * 使用前需要调用该方法对其初始化
     *
     * @param app 系统
     */
    public static void initialize(Application app) {
        instance = new InputState(app);
        app.getStateManager().attach(instance);
    }

    public static void cleanup(Application app) {
        app.getInputManager().removeRawInputListener(instance);
        app.getStateManager().detach(instance);
    }

    public static void add(MouseInputListener listener) {
        instance.mouseInputManager.add(listener);
    }

    public static void remove(MouseInputListener listener) {
        instance.mouseInputManager.remove(listener);
    }

    public static void add(KeyInputListener listener) {
        instance.keyInputManager.add(listener);
    }

    public static void remove(KeyInputListener listener) {
        instance.keyInputManager.remove(listener);
    }

    public static void add(TouchInputListener listener){
        instance.touchInputManager.add(listener);
    }

    public static void remove(TouchInputListener listener){
        instance.touchInputManager.remove(listener);
    }

    private final static class InputState extends AbstractAppState implements RawInputListener {

        private MouseInputManager mouseInputManager;
        private KeyInputManager keyInputManager;
        private TouchInputManager touchInputManager;

        public InputState(Application app) {
            mouseInputManager = new MouseInputManager();
            keyInputManager = new KeyInputManager();
            touchInputManager = new TouchInputManager();
            mouseInputManager.initialize(app);
            keyInputManager.initialize(app);
            touchInputManager.initialize(app);
            app.getInputManager().addRawInputListener(this);
        }

        @Override
        public void initialize(AppStateManager stateManager, Application app) {
        }

        @Override
        public void cleanup() {
            mouseInputManager.cleanup();
            keyInputManager.cleanup();
            touchInputManager.cleanup();
        }

        @Override
        public void update(float tpf) {
            mouseInputManager.update(tpf);
            keyInputManager.update(tpf);
        }

        @Override
        public void onMouseMotionEvent(MouseMotionEvent evt) {
            if (touchInputManager.pointerNum<2) {
                mouseInputManager.onMouseMotionEvent(evt);
            }
            evt.setConsumed();
        }

        @Override
        public void onMouseButtonEvent(MouseButtonEvent evt) {
            if (touchInputManager.pointerNum<2) {
                mouseInputManager.onMouseButtonEvent(evt);
            }
            evt.setConsumed();
        }

        @Override
        public void onKeyEvent(KeyInputEvent evt) {
            keyInputManager.onKeyEvent(evt);
            evt.setConsumed();
        }

        @Override
        public void onTouchEvent(TouchEvent evt) {
            touchInputManager.onTouchEvent(evt);
            evt.setConsumed();
        }

        @Override
        public void onJoyAxisEvent(JoyAxisEvent evt) {
            evt.setConsumed();
        }

        @Override
        public void onJoyButtonEvent(JoyButtonEvent evt) {
            evt.setConsumed();
        }

        @Override
        public void beginInput() {
        }

        @Override
        public void endInput() {
        }

    }

}