package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.ListPropertyListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ChildrenPropertyListener implements ListPropertyListener<UIComponent>, WithUIComponent {

    private UIComponent component;
    private Node view;

    @Override
    public void set(UIComponent component) {
        this.component=component;
        this.view= (Node) component.get(UIComponent.VIEW);
    }

    @Override
    public void propertyAdded(int index, UIComponent child) {
        ParentProperty parentProperty;
        if (child.exist(ParentProperty.class)) {
            parentProperty = child.get(ParentProperty.class);
            UIComponent parent = parentProperty.getParent();
            if (parent != null) parent.get(ChildrenProperty.class).detachChild(child);
        } else {
            parentProperty = new ParentProperty();
            child.set(ParentProperty.class, parentProperty);
        }
        parentProperty.setParent(component);

        AABB childBox = child.get(AABB.class);
        float locationXBeforeAttach = childBox.getXCenter();
        float locationYBeforeAttach = childBox.getYCenter();

        Spatial viewOfChild = (Spatial) child.get(UIComponent.VIEW);
        if (viewOfChild != null) view.attachChild(viewOfChild);

        float locationXAfterAttach = childBox.getXCenter();
        float locationYAfterAttach = childBox.getYCenter();

        child.move(locationXBeforeAttach - locationXAfterAttach,
                locationYBeforeAttach - locationYAfterAttach);
    }

    @Override
    public void propertyRemoved(int index, UIComponent child) {
        AABB childBox = child.get(AABB.class);
        float locationXBeforeDetach = childBox.getXCenter();
        float locationYBeforeDetach = childBox.getYCenter();

        Spatial viewOfChild = (Spatial) child.get(UIComponent.VIEW);
        if (viewOfChild != null) view.detachChild(viewOfChild);

        float locationXAfterDetach = childBox.getXCenter();
        float locationYAfterDetach = childBox.getYCenter();

        child.move(locationXBeforeDetach - locationXAfterDetach,
                locationYBeforeDetach - locationYAfterDetach);

        ParentProperty parentProperty = child.get(ParentProperty.class);
        parentProperty.setParent(null);
    }

}
