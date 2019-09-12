package com.jarlure.ui.property;

import com.jarlure.ui.property.common.Property;

public class FocusProperty extends Property<Boolean> {

    public FocusProperty(){
        setFocus(false);
    }

    /**
     * 组件的焦点属性
     * @param focus
     */
    public FocusProperty(boolean focus){
        setFocus(focus);
    }

    public boolean isFocus() {
        return super.getValue();
    }

    public void setFocus(boolean focus) {
        super.setValue(focus);
    }

    @Override
    @Deprecated
    public Boolean getValue() {
        return isFocus();
    }

    @Override
    @Deprecated
    public void setValue(Boolean value) {
        setFocus(value);
    }
}
