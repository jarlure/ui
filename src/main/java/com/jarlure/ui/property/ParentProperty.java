package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.Property;

import java.util.ArrayList;
import java.util.List;

public class ParentProperty extends Property<UIComponent> {

    public ParentProperty(){
        super();
    }

    /**
     * 组件的父结点属性
     * @param parent
     */
    public ParentProperty(UIComponent parent) {
        super(parent);
    }

    public UIComponent getParent(){
        return super.getValue();
    }

    public void setParent(UIComponent parent){
        super.setValue(parent);
    }

    public void detachFromParent(){
        UIComponent parent = getParent();
        if (parent==null)return;
        if (!parent.exist(ChildrenProperty.class)){
            parent=null;
            return;
        }
        ChildrenProperty childrenProperty = parent.get(ChildrenProperty.class);
        UIComponent theChild=null;
        for (UIComponent child:childrenProperty.value){
            if (this.equals(child.get(ParentProperty.class))){
                theChild=child;
                break;
            }
        }
        if (theChild!=null) childrenProperty.detachChild(theChild);
        parent=null;
    }

    public List<UIComponent> getAncestors(){
        List<UIComponent> list = new ArrayList<>();
        UIComponent parent = getParent();
        while (parent!=null){
            list.add(parent);
            if (!parent.exist(ParentProperty.class))break;
            parent = parent.get(ParentProperty.class).getParent();
        }
        return list;
    }

    @Override
    @Deprecated
    public UIComponent getValue() {
        return getParent();
    }

    @Override
    @Deprecated
    public void setValue(UIComponent value) {
        setParent(value);
    }
}
