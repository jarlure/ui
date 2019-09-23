package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.ListProperty;
import com.jarlure.ui.property.common.ListPropertyAdapter;
import com.jme3.material.Material;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ElementProperty extends ListProperty<UIComponent> implements WithUIComponent {

    private static final Logger LOG = Logger.getLogger(ElementProperty.class.getSimpleName());

    @Override
    public void set(UIComponent component) {
        ElementPropertyListener listener = new ElementPropertyListener();
        listener.set(component);
        this.addPropertyListener(listener);
        //确保ElementProperty总是ChildrenProperty的子集
        component.get(ChildrenProperty.class).addPropertyListener(new ListPropertyAdapter<UIComponent>() {
            @Override
            public void propertyAdded(int index, UIComponent[] value) {
            }

            @Override
            public void propertyRemoved(int index, UIComponent value) {
                if (ElementProperty.this.exist(value)) {
                    ElementProperty.this.remove(value);
                }
            }
        });
        if (component.exist(RangeProperty.class)) {
            this.addPropertyListener(new ListPropertyAdapter<UIComponent>() {
                @Override
                public void propertyAdded(int index, UIComponent element) {
                    Vector4f range = component.get(RangeProperty.class).getValue();
                    Spatial view = (Spatial) element.get(UIComponent.VIEW);
                    setRange(view, range);
                }

                @Override
                public void propertyRemoved(int index, UIComponent element) {
                    Vector4f range = null;
                    if (element.exist(RangeProperty.class)) range = element.get(RangeProperty.class).getValue();
                    Spatial view = (Spatial) element.get(UIComponent.VIEW);
                    setRange(view, range);
                }

                private void setRange(Spatial view, Vector4f range) {
                    if (view == null) {
                    } else if (view instanceof Geometry) {
                        Material mat = ((Geometry) view).getMaterial();
                        if (range == null) mat.clearParam("Range");
                        else mat.setVector4("Range", range);
                    } else if (view instanceof Node) {
                        for (Spatial child : ((Node) view).getChildren()) {
                            setRange(child, range);
                        }
                    } else {
                        LOG.log(Level.WARNING, "无法限制组件的显示范围！不支持的类型：" + view.getClass().getName());
                    }
                }
            });
        }
    }

}
