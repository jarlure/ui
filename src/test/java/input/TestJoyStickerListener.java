package input;

import com.jarlure.ui.input.JoystickEvent;
import com.jarlure.ui.input.JoystickInputListener;
import com.jarlure.ui.system.InputManager;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class TestJoyStickerListener extends SimpleApplication {

    public static void main(String[] args) {
        TestJoyStickerListener app = new TestJoyStickerListener();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);//开启游戏手柄检测
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        InputManager.initialize(this);
        InputManager.add(new JoystickInputListener() {
            @Override
            public void onButtonPressed(JoystickEvent joystick) {
                System.out.println(joystick);
            }

            @Override
            public void onButtonReleased(JoystickEvent joystick) {
                System.out.println(joystick);
            }

            @Override
            public void onAxisMove(JoystickEvent joystick) {
                System.out.println(joystick);
            }
        });
    }

}
