package com.jarlure.ui.property;

import com.jarlure.ui.property.common.Property;

public class PercentProperty extends Property<Float> {

    public PercentProperty(){
        super(0f);
    }

    /**
     * 组件的百分比属性
     * @param value
     */
    public PercentProperty(float value){
        super(value);
    }

    public float getPercent(){
        return super.getValue();
    }

    public void setPercent(float percent) {
        super.setValue(percent);
    }

    @Override
    @Deprecated
    public Float getValue() {
        return super.getValue();
    }

    @Override
    @Deprecated
    public void setValue(Float value) {
        super.setValue(value);
    }

}