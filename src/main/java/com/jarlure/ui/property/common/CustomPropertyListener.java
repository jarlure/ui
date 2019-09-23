package com.jarlure.ui.property.common;

public interface CustomPropertyListener {

    /**
     * 属性对象的引用发生了变化
     *
     * @param property 属性名
     * @param oldValue 属性对象的旧引用值
     * @param newValue 属性对象的新引用值
     */
    void propertyChanged(Enum property, Object oldValue, Object newValue);

}
