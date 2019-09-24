package component.picture;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.TextEditEffect;
import com.jarlure.ui.effect.TextLineEditEffect;
import com.jarlure.ui.input.extend.TextEditKeyInputListener;
import com.jarlure.ui.input.extend.TextEditMouseInputListener;
import com.jarlure.ui.property.FocusProperty;
import com.jarlure.ui.property.FontProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.property.common.Property;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestTextLineEditor extends SimpleApplication {

    public static void main(String[] args) {
        TestTextLineEditor app = new TestTextLineEditor();
        app.setShowSettings(false);
        app.start();
    }

    private UIComponent editor;

    public TestTextLineEditor() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        //创建单行文本编辑器
        Image img = ImageHandler.createEmptyImage(100,20);
        ImageHandler.drawColor(img, ColorRGBA.White);
        editor = new Picture("单行文本编辑器",img);
        editor.move(100,100);
        stateManager.getState(UIRenderState.class).attachChildToNode(editor);

        //设置文本编辑效果
        FontProperty fontProperty = editor.get(FontProperty.class);
        fontProperty.getFont().setName("腾祥嘉丽中黑简").setColor(ColorRGBA.Black).setSize(16);
        TextProperty textProperty = editor.get(TextProperty.class);
        textProperty.setAlign(Direction.LEFT).setText("你好");
        TextEditEffect textEditEffect = new TextLineEditEffect(fontProperty,textProperty);
        editor.set(TextEditEffect.class,textEditEffect);

        //设置编辑器的属性
        Property<Integer> cursorPositionIndex=new Property<>();
        Property<Integer> selectFromIndex=new Property<>();
        cursorPositionIndex.addInputPropertyFilter(value -> {
            if (value<0) return 0;
            int numberOfText = textProperty.getTextPosInImg().length/2;
            if (value>numberOfText) return numberOfText;
            return value;
        });

        //添加鼠标输入监听器
        InputManager.add(new TextEditMouseInputListener(editor) {
            @Override
            public SelectConverter getSelectConverter() {
                return editor.get(SelectConverter.class);
            }

            @Override
            public Property<Integer> getCursorPositionIndex() {
                return cursorPositionIndex;
            }

            @Override
            public Property<Integer> getSelectFromIndex() {
                return selectFromIndex;
            }
        });
        //添加键盘输入监听器
        InputManager.add(new TextEditKeyInputListener(editor) {
            @Override
            protected Property<Integer> getCursorPositionIndex() {
                return cursorPositionIndex;
            }

            @Override
            protected Property<Integer> getSelectFromIndex() {
                return selectFromIndex;
            }
        });
    }

    @Override
    public void simpleUpdate(float tpf) {
        //驱动光标闪烁
        if (editor.get(FocusProperty.class).isFocus()){
            editor.get(TextEditEffect.class).update(tpf);
        }
    }
}