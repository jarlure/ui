package component.picture;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.AABB;
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

import java.util.Arrays;

public class TestLabel extends SimpleApplication {

    public static void main(String[] args) {
        TestLabel app = new TestLabel();
        app.setShowSettings(false);
        app.start();
    }

    public TestLabel() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //1.无图片标签
        //注意：未设置Image的透明图片使用的是一个尺寸为1x1的全局静态常量Image，相当于没有Image。
        //      因此在填充颜色或显示文本前请先给组件设置一个Image
        UIComponent p1 = new Picture("透明图片组件",100,100);

        //2.纯色图片标签
        Image img2 = ImageHandler.createEmptyImage(100,100);
        ImageHandler.drawColor(img2, ColorRGBA.Yellow);
        UIComponent p2 = new Picture("纯色图片组件",img2);

        //3.图像图片标签
        Image img3 = ImageHandler.loadImage("src/test/resources/Textures/刺猬.png");
        UIComponent p3 = new Picture("图像图片组件",img3);

        //4.透明背景文本标签
        Image img4 = ImageHandler.createEmptyImage(100,100);
        UIComponent p4 = new Picture("透明文本组件",img4);
        p4.get(FontProperty.class).getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.White).setSize(14);
        p4.get(TextProperty.class).setAlign(Direction.CENTER).setText("你好");

        //5.图像背景文本标签
        Image img5 = ImageHandler.loadImage("src/test/resources/Textures/按钮.png");
        UIComponent p5 = new Picture("透明文本组件",img5);
        p5.get(FontProperty.class).getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.White).setSize(14);
        p5.get(TextProperty.class).setAlign(Direction.CENTER).setText("你好");

        int x=0;
        for (UIComponent component: Arrays.asList(p1,p2,p3,p4,p5)){
            //移动组件
            //将组件移至底边离窗口底边200像素、从左到右无间距依次排列
            AABB box = component.get(AABB.class);
            component.move(x-box.getXLeft(),200);
            x+=box.getWidth();

            //渲染组件
            //相当于guiNode.attachChild((Spatial)component.get(UIComponent.VIEW));
            stateManager.getState(UIRenderState.class).attachChildToNode(component);

            //输出信息
            String name = (String) component.get(UIComponent.NAME);
            System.out.println(name+"的包围盒："+box);
            System.out.println(name+"的图片数据："+component.get(ImageProperty.class).getImage());
        }
    }

}