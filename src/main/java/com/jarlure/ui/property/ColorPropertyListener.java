package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.PropertyListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ColorPropertyListener implements PropertyListener<ColorRGBA> {

    private UIComponent component;

    public ColorPropertyListener(UIComponent component) {
        this.component = component;
    }

    @Override
    public void propertyChanged(ColorRGBA oldValue, ColorRGBA newValue) {
        if (newValue == null || newValue.equals(oldValue)) return;
        Spatial view = (Spatial) component.get(UIComponent.VIEW);
        setColor(view, newValue);
    }

    protected void setColor(Spatial view, ColorRGBA color) {
        if (view instanceof Geometry) {
            Material mat = ((Geometry) view).getMaterial();
            mat.setColor("Color", color);
        } else if (view instanceof Node) {
            for (Spatial child : ((Node) view).getChildren()) {
                setColor(child, color);
            }
        }
    }

}
