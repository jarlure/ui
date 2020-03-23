package component.property;

import com.jarlure.ui.property.FocusProperty;
import com.jarlure.ui.property.common.PropertyListener;
import com.jme3.app.SimpleApplication;

public class TestFocusProperty extends SimpleApplication {

    public static void main(String[] args) {
        SimpleApplication app = new TestFocusProperty();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        FocusProperty focusProperty = new FocusProperty();
        focusProperty.addPropertyListener(new PropertyListener() {
            @Override
            public void propertyChanged(Object oldValue, Object newValue) {
                focusProperty.removePropertyListener(this);
                System.out.println("ok");
            }
        });
        focusProperty.setFocus(this);
    }
}
