package effect;

import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.effect.SwitchEffect;
import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.texture.Image;

public class TestSwitchEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestSwitchEffect app = new TestSwitchEffect();
        app.setShowSettings(false);
        app.start();
    }

    private UIComponent picture;
    private float t;

    public TestSwitchEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建组件
        Image img = ImageHandler.loadImage("src/test/resources/Textures/刺猬.png");
        picture = new Picture("图片",img);
        picture.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(picture);

        //设置组件的图片切换效果
        Image img1 = ImageHandler.loadImage("src/test/resources/Textures/刺猬1.png");
        Image img2 = ImageHandler.loadImage("src/test/resources/Textures/刺猬2.png");
        SwitchEffect switchEffect = new SwitchEffect(picture.get(ImageProperty.class));
        switchEffect.addImage(0,img);
        switchEffect.addImage(1,img1);
        switchEffect.addImage(2,img2);
        picture.set(SwitchEffect.class,switchEffect);
    }

    @Override
    public void simpleUpdate(float tpf) {
        t+=tpf;
        if (t<.25f)return;
        picture.move(-5f,0);
        t-=.25f;
        picture.get(SwitchEffect.class).switchToNext();
    }
}