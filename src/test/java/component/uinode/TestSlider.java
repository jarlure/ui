package component.uinode;

import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.component.UINode;
import com.jarlure.ui.converter.PercentConverter;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.ProgressEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputAdapter;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.ChildrenProperty;
import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.property.PercentProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestSlider extends SimpleApplication {

    public static void main(String[] args) {
        TestSlider app = new TestSlider();
        app.setShowSettings(false);
        app.start();
    }

    public TestSlider() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建滑块的子组件：进度条、按钮
        Image progressBarImg = ImageHandler.createEmptyImage(100,5);
        Image btnImg = ImageHandler.createEmptyImage(10,10);
        ImageHandler.drawColor(progressBarImg,ColorRGBA.DarkGray);
        ImageHandler.drawColor(btnImg, ColorRGBA.White);
        UIComponent progressBar = new Picture("进度条",progressBarImg);
        UIComponent button = new Picture("按钮",btnImg);

        //设置按钮的位置
        AABB buttonBox = button.get(AABB.class);
        AABB progressBarBox = progressBar.get(AABB.class);
        button.move(progressBarBox.getXLeft()-buttonBox.getXCenter(),
                progressBarBox.getYCenter()-buttonBox.getYCenter());

        //设置进度条的进度效果
        Image emptyProgressBarImg = ImageHandler.clone(progressBarImg);
        Image fullProgressBarImg = ImageHandler.createEmptyImage(progressBarImg.getWidth(),progressBarImg.getHeight());
        ImageHandler.drawColor(fullProgressBarImg,ColorRGBA.Green);
        ImageProperty imageProperty = progressBar.get(ImageProperty.class);
        ProgressEffect progressEffect = new ProgressEffect(emptyProgressBarImg,fullProgressBarImg,imageProperty);
        progressBar.set(ProgressEffect.class,progressEffect);

        //创建滑块
        UIComponent slider = new UINode("滑块");
        slider.get(ChildrenProperty.class).attachChild(progressBar,button);
        slider.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(slider);

        //设置滑块的鼠标位置-百分比转换器、进度条效果、百分比属性
        PercentConverter percentConverter = new PercentConverter() {
            @Override
            public float getPercent(float x, float y) {
                float minX = progressBarBox.getXLeft();
                if (x<minX)return 0;
                float maxX = progressBarBox.getXRight();
                if (x>maxX)return 1;
                return (x-minX)/(maxX-minX);
            }

            @Override
            public float getX(float percent) {
                float x = progressBarBox.getXLeft();
                x+=progressBarBox.getWidth()*percent;
                return x;
            }

            @Override
            public float getY(float percent) {
                return progressBarBox.getYCenter();
            }
        };
        slider.set(PercentConverter.class,percentConverter);
        slider.set(ProgressEffect.class,progressEffect);
        slider.get(PercentProperty.class).addPropertyListener((oldValue, newValue) -> {
            float x = slider.get(PercentConverter.class).getX(newValue);
            button.move(x-buttonBox.getXCenter(),0);
            slider.get(ProgressEffect.class).setPercent(newValue);
        });

        //添加鼠标输入监听器
        InputManager.add(new MouseInputAdapter(){

            private boolean pressed;

            @Override
            public void onLeftButtonClick(MouseEvent mouse) {
                if (slider.get(SelectConverter.class).isSelect(slider,mouse)){
                    float percent = slider.get(PercentConverter.class).getPercent(mouse.x,mouse.y);
                    slider.get(PercentProperty.class).setPercent(percent);
                }
            }

            @Override
            public void onLeftButtonPress(MouseEvent mouse) {
                if (slider.get(SelectConverter.class).isSelect(slider,mouse)){
                    pressed=true;
                }
            }

            @Override
            public void onLeftButtonDragging(MouseEvent mouse) {
                if (!pressed)return;
                float percent = slider.get(PercentConverter.class).getPercent(mouse.x,mouse.y);
                slider.get(PercentProperty.class).setPercent(percent);
            }

            @Override
            public void onLeftButtonRelease(MouseEvent mouse) {
                pressed=false;
            }

        });
    }

}