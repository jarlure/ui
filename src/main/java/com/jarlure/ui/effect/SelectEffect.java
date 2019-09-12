package com.jarlure.ui.effect;

import com.jarlure.ui.component.UIComponent;

/**
 * 选中移动效果
 */
public interface SelectEffect extends AnimEffect {

    /**
     * 当选中组件时触发的移动效果。
     *
     * @param component 选中的组件
     */
    void select(UIComponent component);

}
