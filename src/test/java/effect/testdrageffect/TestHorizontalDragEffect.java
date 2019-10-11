package effect.testdrageffect;

import com.jarlure.ui.component.Panel;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.DynamicCoord;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.DragEffect;
import com.jarlure.ui.effect.OrderEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputAdapter;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.ElementProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestHorizontalDragEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestHorizontalDragEffect app = new TestHorizontalDragEffect();
        app.setShowSettings(false);
        app.start();
    }

    private UIComponent panel;

    public TestHorizontalDragEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        Image backgroundImg = ImageHandler.createEmptyImage(settings.getWidth(),settings.getHeight()/8);
        ImageHandler.drawColor(backgroundImg, ColorRGBA.DarkGray);
        panel = new Panel("面板",backgroundImg);

        //设置整齐效果
        panel.set(OrderEffect.class, createOrderEffect(panel));

        //设置水平拖拽效果
        DragEffect dragEffect=new HorizontalDragEffect(panel.get(ElementProperty.class));
        panel.set(DragEffect.class,dragEffect);

        panel.move(0,settings.getHeight()/2f-panel.get(AABB.class).getYCenter());
        stateManager.getState(UIRenderState.class).attachChildToNode(panel);

        //添加几个组件到面板上
        for (int i = 1+(int) Math.round(4*Math.random()); i>=0; i--){
            panel.get(ElementProperty.class).add(createPicture());
        }

        //注册鼠标拖拽操作
        InputManager.add(new MouseInputAdapter(){

            private boolean pressed;

            @Override
            public void onLeftButtonPress(MouseEvent mouse) {
                if (panel.get(SelectConverter.class).isSelect(panel,mouse)){
                    pressed=true;
                    panel.get(DragEffect.class).start(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonDragging(MouseEvent mouse) {
                if (pressed){
                    panel.get(DragEffect.class).update(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonRelease(MouseEvent mouse) {
                if (pressed){
                    pressed=false;
                    panel.get(DragEffect.class).end();
                }
            }

        });
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!panel.get(DragEffect.class).isFinished()){
            panel.get(DragEffect.class).update(tpf);
        }
    }

    private OrderEffect createOrderEffect(UIComponent panel){
        //设置整齐效果
        AABB panelBox = panel.get(AABB.class);
        //1.规定面板上组件的尺寸
        DynamicCoord fixSize = new DynamicCoord(panel.get(AABB.class),40,40);
        //2.规定面板上第一个组件的位置
        DynamicCoord fixPos0 = new DynamicCoord(panelBox, 25, panelBox.getHeight() / 2);
        //3.规定面板上第二个组件的位置
        DynamicCoord fixPos1 = new DynamicCoord(panelBox, 75, panelBox.getHeight() / 2);
        ElementProperty elementProperty = panel.get(ElementProperty.class);
        return new OrderEffect(elementProperty, fixSize, fixPos0, fixPos1);
    }

    private UIComponent createPicture(){
        Image img = ImageHandler.createEmptyImage(100,100);
        ImageHandler.drawColor(img,ColorRGBA.randomColor());
        return new Picture("picture",img);
    }

}