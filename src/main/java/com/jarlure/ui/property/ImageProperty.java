package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.Property;
import com.jarlure.ui.property.common.PropertyListener;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.scene.Geometry;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

public class ImageProperty extends Property<Image> implements WithUIComponent {

    public ImageProperty() {
        super();
    }

    /**
     * 组件的图片属性
     *
     * @param img   图片参数
     */
    public ImageProperty(Image img) {
        super(img);
    }

    @Override
    public void set(UIComponent component) {
        Geometry view = (Geometry) component.get(UIComponent.VIEW);
        Texture2D tex = (Texture2D) view.getMaterial().getParam("Texture").getValue();
        Image img = tex.getImage();
        setImage(img);
        ImagePropertyListener listener = new ImagePropertyListener();
        listener.set(component);
        this.addPropertyListener(listener);
    }

    /**
     * 获得图片
     *
     * @return 当前图片
     */
    public Image getImage() {
        return super.getValue();
    }

    /**
     * 设置图片
     *
     * @param img 新图片
     */
    public void setImage(Image img) {
        super.setValue(img);
    }

    @Override
    protected void propertyChanged(Image oldValue, Image newValue) {
        if (this.listenerList == null) return;
        for (PropertyListener<Image> listener : listenerList) {
            listener.propertyChanged(oldValue, newValue);
        }
    }

    @Override
    @Deprecated
    public Image getValue() {
        return getImage();
    }

    @Override
    @Deprecated
    public void setValue(Image value) {
        setImage(value);
    }

}
