package component.panel;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.component.Panel;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.SwitchEffect;
import com.jarlure.ui.input.extend.ButtonMouseInputListener;
import com.jarlure.ui.property.ChildrenProperty;
import com.jarlure.ui.property.FontProperty;
import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestDialog extends SimpleApplication {

    public static void main(String[] args) {
        TestDialog app = new TestDialog();
        app.setShowSettings(false);
        app.start();
    }

    public TestDialog() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建对话框的子组件：文本
        Image textImg = ImageHandler.createEmptyImage(100,20);
        UIComponent text = new Picture("文本",textImg);
        text.move(20,55);
        text.get(FontProperty.class).getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.White).setSize(12);
        text.get(TextProperty.class).setAlign(Direction.LEFT).setText("提示内容");

        //创建对话框的子组件：按钮
        Image buttonImg = ImageHandler.loadImage("src/test/resources/Textures/按钮.png");
        UIComponent button = new Picture("按钮",buttonImg);
        button.move(65,5);

        //创建对话框的子组件：按钮文本
        Image buttonTextImg = ImageHandler.createEmptyImage(buttonImg.getWidth(),buttonImg.getHeight());
        UIComponent buttonText = new Picture("按钮文本",buttonTextImg);
        buttonText.move(65,6);
        buttonText.get(FontProperty.class).getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.White).setSize(12);
        buttonText.get(TextProperty.class).setAlign(Direction.CENTER).setText("确定");

        //设置按钮的图片切换效果
        SwitchEffect switchEffect = new SwitchEffect(button.get(ImageProperty.class));
        Image img_pressed = ImageHandler.loadImage("src/test/resources/Textures/按钮（按下）.png");
        switchEffect.addImage(ButtonMouseInputListener.NOTHING,buttonImg);
        switchEffect.addImage(ButtonMouseInputListener.MOVE_ON,buttonImg);
        switchEffect.addImage(ButtonMouseInputListener.PRESSED,img_pressed);
        button.set(SwitchEffect.class,switchEffect);

        //添加鼠标输入监听器
        InputManager.add(new ButtonMouseInputListener(button) {
            @Override
            protected SelectConverter getSelectConverter() {
                return button.get(SelectConverter.class);
            }
        });

        //创建对话框
        Image dialogImg = ImageHandler.loadImage("src/test/resources/Textures/对话框.png");
        UIComponent dialog = new Panel("对话框",dialogImg);
        dialog.get(ChildrenProperty.class).attachChild(text,button,buttonText);
        dialog.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(dialog);
    }

}