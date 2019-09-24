package effect;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.effect.NinePatchEffect;
import com.jarlure.ui.property.*;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestNinePatchEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestNinePatchEffect app = new TestNinePatchEffect();
        app.setShowSettings(false);
        app.start();
    }

    public TestNinePatchEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建聊天气泡
        Image dot9Img = ImageHandler.loadImage("src/test/resources/Textures/聊天气泡.png");
        boolean[][] edge = new boolean[4][];
        Image img = ImageHandler.cutNinePatchImage(dot9Img,edge);
        UIComponent chatBox = new Picture("聊天气泡",img);
        chatBox.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(chatBox);

        //设置聊天气泡的点九图效果
        TextProperty textProperty = chatBox.get(TextProperty.class);
        NinePatchEffect ninePatchEffect = new NinePatchEffect(img,edge,chatBox.get(ImageProperty.class),textProperty);
        chatBox.set(NinePatchEffect.class,ninePatchEffect);
        chatBox.get(SpatialProperty.class).addPropertyListener((property, oldValue, newValue) -> {
            if (SpatialProperty.Property.WORLD_SCALE.equals(property)) {
                AABB box = chatBox.get(AABB.class);
                float width = box.getWidth();
                float height = box.getHeight();
                chatBox.get(NinePatchEffect.class).setSize(width, height);
            }
        });
        ninePatchEffect.setSize(img.getWidth(), img.getHeight());//初始化填充位置

        //设置聊天气泡的文本属性
        chatBox.get(FontProperty.class).getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.Black).setSize(14);
        chatBox.get(TextProperty.class).setAlign(Direction.CENTER).setText("使用点九图可以有效解决图片拉伸形变问题");

        chatBox.get(AABB.class).setWidth(320);
    }

}
