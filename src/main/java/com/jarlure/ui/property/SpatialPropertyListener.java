package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.CustomPropertyListener;

public class SpatialPropertyListener implements CustomPropertyListener, WithUIComponent {

    private UIComponent component;

    @Override
    public void set(UIComponent component) {
        this.component = component;
    }

    @Override
    public void propertyChanged(Enum property, Object oldValue, Object newValue) {
        if (!(property instanceof SpatialProperty.Property)) return;
        boolean existChildren = component.exist(ChildrenProperty.class);
        ChildrenProperty childrenProperty = existChildren ? component.get(ChildrenProperty.class) : null;
        if (childrenProperty != null) existChildren = !childrenProperty.isEmpty();
        switch ((SpatialProperty.Property) property) {
            case LOCAL_SCALE:
                component.get(SpatialProperty.class).worldScaleChanged();
                break;
            case LOCAL_ROTATION:
                component.get(SpatialProperty.class).worldRotationChanged();
                break;
            case LOCAL_TRANSLATION:
                component.get(SpatialProperty.class).worldTranslationChanged();
                break;
            case WORLD_SCALE:
                if (existChildren) {
                    for (UIComponent child : childrenProperty.value) {
                        child.get(SpatialProperty.class).worldScaleChanged();
                    }
                }
                break;
            case WORLD_ROTATION:
                if (existChildren) {
                    for (UIComponent child : childrenProperty.value) {
                        child.get(SpatialProperty.class).worldRotationChanged();
                    }
                }
                break;
            case WORLD_TRANSLATION:
                if (existChildren) {
                    for (UIComponent child : childrenProperty.value) {
                        child.get(SpatialProperty.class).worldTranslationChanged();
                    }
                }
                break;
        }
    }

}
