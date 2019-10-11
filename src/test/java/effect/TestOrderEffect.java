package effect;

import com.jarlure.ui.component.Panel;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.DynamicCoord;
import com.jarlure.ui.effect.OrderEffect;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.ChildrenProperty;
import com.jarlure.ui.property.ElementProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestOrderEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestOrderEffect app = new TestOrderEffect();
        app.setShowSettings(false);
        app.start();
    }

    public TestOrderEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建面板
        Image backgroundImg = ImageHandler.createEmptyImage(100, 100);
        ImageHandler.drawColor(backgroundImg, ColorRGBA.DarkGray);
        Panel panel = new Panel("panel", backgroundImg);
        stateManager.getState(UIRenderState.class).attachChildToNode(panel);

        //设置面板的整齐效果
        AABB panelBox = panel.get(AABB.class);
        //1.规定面板上组件的尺寸
        DynamicCoord fixSize = new DynamicCoord(panelBox, 10, 10);
        //2.规定面板上第一个组件的位置
        DynamicCoord fixPos0 = new DynamicCoord(panelBox, 5, panelBox.getHeight() / 2);
        //3.规定面板上第二个组件的位置
        DynamicCoord fixPos1 = new DynamicCoord(panelBox, 20, panelBox.getHeight() / 2);
        ElementProperty elementProperty = panel.get(ElementProperty.class);
        OrderEffect effect = new OrderEffect(elementProperty, fixSize, fixPos0, fixPos1);
        panel.set(OrderEffect.class, effect);

        //创建面板的子组件
        UIComponent[] children=new UIComponent[7];
        for (int i=0;i<children.length;i++){
            Image img = ImageHandler.createEmptyImage(100, 100);
            ImageHandler.drawColor(img, ColorRGBA.randomColor());
            children[i]=new Picture("子组件", img);
        }
        elementProperty.add(children);
    }


}