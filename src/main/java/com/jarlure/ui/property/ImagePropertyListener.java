package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.PropertyListener;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.scene.Geometry;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

public class ImagePropertyListener implements PropertyListener<Image>, WithUIComponent {

    private Geometry view;

    @Override
    public void set(UIComponent component) {
        view= (Geometry) component.get(UIComponent.VIEW);
    }

    @Override
    public void propertyChanged(Image oldValue, Image newValue) {
        if (oldValue==newValue)return;
        if (newValue==null) newValue= ImageHandler.createEmptyImage(1,1);
        Texture2D tex = (Texture2D) view.getMaterial().getParam("Texture").getValue();
        tex.setImage(newValue);
    }

}