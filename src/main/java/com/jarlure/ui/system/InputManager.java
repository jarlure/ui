package com.jarlure.ui.system;

import com.jarlure.ui.input.JoystickInputListener;
import com.jarlure.ui.input.KeyInputListener;
import com.jarlure.ui.input.MouseInputListener;
import com.jarlure.ui.input.TouchInputListener;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.event.*;
import com.jme3.system.JmeContext;

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

    public static void add(InputListener listener){
        instance.add(listener);
    }

    public static void remove(InputListener listener){
        instance.remove(listener);
    }

    private final static class InputState extends AbstractAppState implements RawInputListener {

        private MouseInputManager mouseInputManager;
        private KeyInputManager keyInputManager;
        private TouchInputManager touchInputManager;
        private JoystickInputManager joystickInputManager;

        public InputState(Application app) {
            mouseInputManager = new MouseInputManager();
            keyInputManager = new KeyInputManager();

            JmeContext context = app.getContext();
            if (context!=null){
                if (null!=context.getTouchInput()){
                    touchInputManager=new TouchInputManager();
                }
                if (null!=context.getJoyInput()){
                    joystickInputManager=new JoystickInputManager();
                }
            }
            app.getInputManager().setSimulateMouse(false);
            app.getInputManager().setSimulateKeyboard(false);
            app.getInputManager().addRawInputListener(this);
        }

        @Override
        public void initialize(AppStateManager stateManager, Application app) {
            mouseInputManager.initialize();
        }

        @Override
        public void cleanup() {
        }

        @Override
        public void update(float tpf) {
            mouseInputManager.update(tpf);
        }

        public void add(InputListener listener){
            if (listener instanceof MouseInputListener){
                if (mouseInputManager!=null) mouseInputManager.add((MouseInputListener) listener);
            }
            if (listener instanceof KeyInputListener){
                if (keyInputManager!=null) keyInputManager.add((KeyInputListener) listener);
            }
            if (listener instanceof TouchInputListener){
                if (touchInputManager!=null) touchInputManager.add((TouchInputListener) listener);
            }
            if (listener instanceof JoystickInputListener){
                if (joystickInputManager!=null) joystickInputManager.add((JoystickInputListener) listener);
            }
        }

        public void remove(InputListener listener){
            if (listener instanceof MouseInputListener){
                if (mouseInputManager!=null) mouseInputManager.remove((MouseInputListener) listener);
            }
            if (listener instanceof KeyInputListener){
                if (keyInputManager!=null) keyInputManager.remove((KeyInputListener) listener);
            }
            if (listener instanceof TouchInputListener){
                if (touchInputManager!=null) touchInputManager.remove((TouchInputListener) listener);
            }
            if (listener instanceof JoystickInputListener){
                if (joystickInputManager!=null) joystickInputManager.remove((JoystickInputListener) listener);
            }
        }

        @Override
        public void onMouseMotionEvent(MouseMotionEvent evt) {
            mouseInputManager.onMouseMotionEvent(evt);
        }

        @Override
        public void onMouseButtonEvent(MouseButtonEvent evt) {
            mouseInputManager.onMouseButtonEvent(evt);
        }

        @Override
        public void onKeyEvent(KeyInputEvent evt) {
            keyInputManager.onKeyEvent(evt);
        }

        @Override
        public void onTouchEvent(TouchEvent evt) {
            touchInputManager.onTouchEvent(evt);
        }

        @Override
        public void onJoyAxisEvent(JoyAxisEvent evt) {
            joystickInputManager.onJoyAxisEvent(evt);
        }

        @Override
        public void onJoyButtonEvent(JoyButtonEvent evt) {
            joystickInputManager.onJoyButtonEvent(evt);
        }

        @Override
        public void beginInput() {
        }

        @Override
        public void endInput() {
        }

    }

}