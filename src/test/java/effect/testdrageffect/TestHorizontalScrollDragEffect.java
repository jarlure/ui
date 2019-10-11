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

public class TestHorizontalScrollDragEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestHorizontalScrollDragEffect app = new TestHorizontalScrollDragEffect();
        app.setShowSettings(false);
        app.start();
    }

    private UIComponent panel1;//子组件很少，没有超出面板的情况
    private UIComponent panel2;//子组件很多，超出面板的情况

    public TestHorizontalScrollDragEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        Image backgroundImg = ImageHandler.createEmptyImage(settings.getWidth(),settings.getHeight()/8);
        ImageHandler.drawColor(backgroundImg, ColorRGBA.DarkGray);
        panel1 = new Panel("面板1",backgroundImg);
        panel2 = new Panel("面板2",backgroundImg);

        //设置整齐效果
        panel1.set(OrderEffect.class,createOrderEffect(panel1));
        panel2.set(OrderEffect.class,createOrderEffect(panel2));

        panel1.move(0,settings.getHeight()*2/3f-panel1.get(AABB.class).getYCenter());
        panel2.move(0,settings.getHeight()/3f-panel2.get(AABB.class).getYCenter());
        stateManager.getState(UIRenderState.class).attachChildToNode(panel1,panel2);

        //设置水平滚动拖拽效果
        AABB box1=panel1.get(AABB.class);
        float fixPos0 = 25;//第一个组件的位置坐标x值
        float halfWidthOfFixSize= 40/2f;//子组件的宽度的一半
        DynamicCoord primaryReference1 = new DynamicCoord(box1,fixPos0,box1.getYCenter());
        DynamicCoord secondaryReference1 = new DynamicCoord(box1,box1.getXRight()-halfWidthOfFixSize,box1.getYCenter());
        DragEffect dragEffect1 = new HorizontalScrollDragEffect(panel1.get(ElementProperty.class),primaryReference1,secondaryReference1);
        panel1.set(DragEffect.class,dragEffect1);

        AABB box2=panel2.get(AABB.class);
        DynamicCoord primaryReference2 = new DynamicCoord(box2,fixPos0,box2.getYCenter());
        DynamicCoord secondaryReference2 = new DynamicCoord(box2,box2.getXRight()-halfWidthOfFixSize,box2.getYCenter());
        DragEffect dragEffect2 = new HorizontalScrollDragEffect(panel2.get(ElementProperty.class),primaryReference2,secondaryReference2);
        panel2.set(DragEffect.class,dragEffect2);

        //添加组件到面板上
        for (int i=0;i<5;i++){
            panel1.get(ElementProperty.class).add(createPicture());
        }
        for (int i=0;i<15;i++){
            panel2.get(ElementProperty.class).add(createPicture());
        }

        //注册鼠标拖拽操作
        InputManager.add(new MouseInputAdapter(){

            private boolean pressed;

            @Override
            public void onLeftButtonPress(MouseEvent mouse) {
                if (panel1.get(SelectConverter.class).isSelect(panel1,mouse)){
                    pressed=true;
                    panel1.get(DragEffect.class).start(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonDragging(MouseEvent mouse) {
                if (pressed){
                    panel1.get(DragEffect.class).update(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonRelease(MouseEvent mouse) {
                if (pressed){
                    pressed=false;
                    panel1.get(DragEffect.class).end();
                }
            }

        });
        InputManager.add(new MouseInputAdapter(){

            private boolean pressed;

            @Override
            public void onLeftButtonPress(MouseEvent mouse) {
                if (panel2.get(SelectConverter.class).isSelect(panel2,mouse)){
                    pressed=true;
                    panel2.get(DragEffect.class).start(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonDragging(MouseEvent mouse) {
                if (pressed){
                    panel2.get(DragEffect.class).update(mouse.x,mouse.y);
                }
            }

            @Override
            public void onLeftButtonRelease(MouseEvent mouse) {
                if (pressed){
                    pressed=false;
                    panel2.get(DragEffect.class).end();
                }
            }

        });
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!panel1.get(DragEffect.class).isFinished()){
            panel1.get(DragEffect.class).update(tpf);
        }
        if (!panel2.get(DragEffect.class).isFinished()){
            panel2.get(DragEffect.class).update(tpf);
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