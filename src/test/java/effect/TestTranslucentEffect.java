package effect;

import com.jarlure.ui.component.Picture;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.ColorProperty;
import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.system.InputManager;
import com.jarlure.ui.system.UIRenderState;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class TestTranslucentEffect extends SimpleApplication {

    public static void main(String[] args) {
        TestTranslucentEffect app = new TestTranslucentEffect();
        app.setShowSettings(false);
        app.start();
    }

    private UIComponent component;
    private float t;
    private int sign=1;

    public TestTranslucentEffect() {
        super(new UIRenderState());
    }

    @Override
    public void simpleInitApp() {
        AssetManager.initialize(this);
        InputManager.initialize(this);

        Image img = ImageHandler.loadImage("src/test/resources/Textures/五角星.png");
        component = new Picture("component",img);
        component.get(ColorProperty.class).setColor(new ColorRGBA(1,1,1,1));
        component.move(100,100);
        getStateManager().getState(UIRenderState.class).attachChildToNode(component);
    }

    @Override
    public void simpleUpdate(float tpf) {
        t+=sign*tpf;
        if (t>1f){
            t=1f;
            sign=-sign;
        }
        if (t<0f){
            t=0f;
            sign=-sign;
        }
        ColorProperty colorProperty = component.get(ColorProperty.class);
        colorProperty.setColor(new ColorRGBA(1,1,1,t));
    }
}