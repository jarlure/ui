package component.picture;

import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.SwitchEffect;
import com.jarlure.ui.input.extend.ButtonMouseInputListener;
import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.texture.Image;

public class TestButton extends SimpleApplication {

    public static void main(String[] args) {
        TestButton app = new TestButton();
        app.setShowSettings(false);
        app.start();
    }

    public TestButton() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建按钮
        Image img = ImageHandler.loadImage("src/test/resources/Textures/按钮.png");
        UIComponent button = new Picture("按钮",img);
        button.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(button);

        //设置按钮的图片切换效果
        SwitchEffect switchEffect = new SwitchEffect(button.get(ImageProperty.class));
        Image img_pressed = ImageHandler.loadImage("src/test/resources/Textures/按钮（按下）.png");
        switchEffect.addImage(ButtonMouseInputListener.NOTHING,img);
        switchEffect.addImage(ButtonMouseInputListener.MOVE_ON,img);
        switchEffect.addImage(ButtonMouseInputListener.PRESSED,img_pressed);
        button.set(SwitchEffect.class,switchEffect);

        //添加鼠标输入监听器
        InputManager.add(new ButtonMouseInputListener(button) {
            @Override
            protected SelectConverter getSelectConverter() {
                return button.get(SelectConverter.class);
            }
        });
    }

}