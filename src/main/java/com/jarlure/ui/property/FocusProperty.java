package com.jarlure.ui.property;

import com.jarlure.ui.property.common.Property;

/**
 * 焦点属性。该类用于解决快捷键或关联组件一对多的情况，例如按下Esc键会优先关闭处于焦点状态的弹出框而非弹出是否关闭
 * 程序的提示框
 */
public class FocusProperty extends Property {

    /**
     * 判断对象是否处于焦点状态。
     *
     * @param obj 对象
     * @return true如果处于焦点状态；false如果不处于焦点状态
     */
    public boolean isFocus(Object obj){
        if (value==null) return false;
        return value.equals(obj);
    }

    /**
     * 设置焦点对象
     *
     * @param focus 准备处于焦点状态的对象
     */
    public void setFocus(Object focus) {
        this.value = focus;
    }

    /**
     * 获取焦点对象
     *
     * @return 处于焦点状态的对象
     */
    public Object getFocus() {
        return value;
    }

    @Override
    @Deprecated
    public Object getValue() {
        return getFocus();
    }

    @Override
    @Deprecated
    public void setValue(Object value) {
        setFocus(value);
    }

}
