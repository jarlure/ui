package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.ListPropertyAdapter;

public class ElementPropertyListener extends ListPropertyAdapter<UIComponent> implements WithUIComponent {

    private UIComponent component;

    @Override
    public void set(UIComponent component) {
        this.component = component;
    }

    @Override
    public void propertyAdded(int index, UIComponent element) {
        component.get(ChildrenProperty.class).attachChild(element);
    }

    @Override
    public void propertyRemoved(int index, UIComponent element) {
        component.get(ChildrenProperty.class).detachChild(element);
    }

}
