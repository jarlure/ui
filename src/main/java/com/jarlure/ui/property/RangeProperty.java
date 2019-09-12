package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.Property;
import com.jme3.math.Vector4f;

public class RangeProperty extends Property<Vector4f> implements WithUIComponent{

    private Vector4f oldValue=new Vector4f();

    /**
     * 组件的渲染范围属性
     */
    public RangeProperty(){
        super(new Vector4f());
    }

    @Override
    public void set(UIComponent component) {
        component.get(SpatialProperty.class).addPropertyListener((property, oldValue, newValue) -> {
            if (property.equals(SpatialProperty.Property.WORLD_SCALE)){
                AABB box = component.get(AABB.class);
                setRange(box.getXLeft(),box.getYBottom(),box.getXRight(),box.getYTop());
            }
        });
    }

    public void setRange(float startX, float startY, float endX, float endY){
        Vector4f range=this.oldValue.set(startX,startY,endX,endY);
        range=filterInputProperty(range);
        boolean pass= interceptProperty(range);
        if (!pass)return;
        this.oldValue.set(this.value);
        this.value.set(startX,startY,endX,endY);

        propertyChanged(this.oldValue,this.value);
    }

    public boolean contains(float x,float y){
        if (x<value.x)return false;
        if (y<value.y)return false;
        if (x>value.z)return false;
        if (y>value.w)return false;
        return true;
    }

    @Override
    @Deprecated
    public void setValue(Vector4f range) {
        range=filterInputProperty(range);
        boolean pass= interceptProperty(range);
        if (!pass)return;
        this.oldValue.set(this.value);
        this.value.set(range);

        propertyChanged(this.oldValue,this.value);
    }

}
