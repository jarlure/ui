package com.jarlure.ui.property.common;

public interface PropertyListener<T> {

    /**
     * 属性值更新通知
     * @param oldValue  更新前的属性值
     * @param newValue  更新后的属性值
     */
    void propertyChanged(T oldValue,T newValue);

}