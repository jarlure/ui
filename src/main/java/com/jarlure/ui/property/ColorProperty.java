package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.Property;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

public class ColorProperty extends Property<ColorRGBA> implements WithUIComponent {

    /**
     * 组件的颜色属性
     */
    public ColorProperty() {
        super(ColorRGBA.White);
    }

    @Override
    public void set(UIComponent component) {
        Spatial view = (Spatial) component.get(UIComponent.VIEW);
        if (view instanceof Geometry) {
            ColorRGBA color = (ColorRGBA) ((Geometry) view).getMaterial().getParam("Color").getValue();
            setColor(color);
        }
        addPropertyListener(new ColorPropertyListener(component));
    }

    /**
     * 获得颜色
     *
     * @return 当前颜色
     */
    public ColorRGBA getColor() {
        return super.getValue();
    }

    /**
     * 设置颜色
     *
     * @param value 新颜色
     */
    public void setColor(ColorRGBA value) {
        super.setValue(value);
    }

    @Override
    @Deprecated
    public ColorRGBA getValue() {
        return super.getValue();
    }

    @Override
    @Deprecated
    public void setValue(ColorRGBA value) {
        super.setValue(value);
    }

}
