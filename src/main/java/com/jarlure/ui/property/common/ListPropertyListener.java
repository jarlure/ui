package com.jarlure.ui.property.common;

public interface ListPropertyListener<T> {

    /**
     * 属性对象当中有了新的数据
     *
     * @param index 新数据的起始索引
     * @param value 一系列新的数据。这些数据的索引值=自身在数组中的索引值+index
     */
    void propertyAdded(int index, T[] value);

    /**
     * 属性对象当中有了新的数据
     *
     * @param index 新数据的索引值
     * @param value 新数据
     */
    void propertyAdded(int index, T value);

    /**
     * 属性对象移除了数据
     *
     * @param index 数据的索引值。与数据一一对应
     * @param value 数据。与数据的索引值一一对应
     */
    void propertyRemoved(int[] index, Object[] value);

    /**
     * 属性对象移除了数据
     *
     * @param index 数据的索引值
     * @param value 数据
     */
    void propertyRemoved(int index, T value);

    /**
     * 属性对象当中的数据变更了
     *
     * @param index    数据的索引值。与新旧数据一一对应
     * @param oldValue 旧数据。与数据的索引值一一对应
     * @param newValue 新数据。与数据的索引值一一对应
     */
    void propertyChanged(int[] index, T[] oldValue, T[] newValue);

    /**
     * 属性对象当中的数据变更了
     *
     * @param index    数据的索引值
     * @param oldValue 该索引值对应的旧数据
     * @param newValue 该索引值对应的新数据
     */
    void propertyChanged(int index, T oldValue, T newValue);

    /**
     * 属性当中的数据索引发生了交换
     *
     * @param indexA 交换到B的索引值
     * @param valueA 交换到B的数据
     * @param indexB 交换的A的索引值
     * @param valueB 交换到A的数据
     */
    void propertyExchanged(int[] indexA, T[] valueA, int[] indexB, T[] valueB);

    /**
     * 属性当中的数据索引发生了交换
     *
     * @param indexA 交换到B的索引值
     * @param valueA 交换到B的数据
     * @param indexB 交换的A的索引值
     * @param valueB 交换到A的数据
     */
    void propertyExchanged(int indexA, T valueA, int indexB, T valueB);

}