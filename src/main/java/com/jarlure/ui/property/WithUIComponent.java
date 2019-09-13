package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;

/**
 * 这个接口用于组件属性的自动初始化。例如假定有个组件component自身没有ImageProperty这个属性，那么调用
 * component.get(ImageProperty.class)时程序会通过反射机制创建一个ImageProperty，接着由于ImageProperty实现了该接口，因此
 * 会调用set(component)方法自动对其进行初始化。从而确保ImageProperty中的Image数据与component中view的Image数据一致。
 */
public interface WithUIComponent {

    /**
     * 用于对自动创建的、实现了该方法的属性自动调用该方法初始化
     * @param component 组件
     */
    void set(UIComponent component);

}
