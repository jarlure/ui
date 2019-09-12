package com.jarlure.ui.property.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListProperty<T> {

    public List<T> value;
    private List<ListPropertyListener<T>> listenerList;

    public ListProperty(){
        this(new ArrayList<>());
    }

    public ListProperty(int initialCapacity){
        this(new ArrayList<>(initialCapacity));
    }

    public ListProperty(List<T> value){
        this.value=value;
    }

    /**
     * @return true如果属性值列表为空；false如果属性值列表不为空
     */
    public boolean isEmpty(){
        return this.value.isEmpty();
    }

    /**
     * @param value 属性值列表中的元素
     * @return  true如果属性值列表中有该元素；false如果属性值列表中没有该元素
     */
    public boolean exist(T value) {
        return this.value.contains(value);
    }

    /**
     * 根据索引值获取属性值列表中的元素
     * @param index 索引值
     * @return  属性值列表中的元素
     */
    public T get(int index) {
        return this.value.get(index);
    }

    /**
     * @return  属性值列表中元素的个数
     */
    public int size() {
        return this.value.size();
    }

    /**
     * 获取该元素在属性值列表中的索引值
     * @param value 属性值列表中的元素
     * @return  索引值
     */
    public int indexOf(T value) {
        return this.value.indexOf(value);
    }

    /**
     * 在属性值列表中元素末尾添加一个元素
     * @param value 元素
     */
    public void add(T value) {
        add(size(),value);
    }

    /**
     * 在属性值列表中插入一个元素
     * @param index 插入后的索引值
     * @param value 插入的元素
     */
    public void add(int index,T value){
        int size = size();
        if (index > size) index = size;
        this.value.add(index,value);
        propertyAdded(index,value);
    }

    /**
     * 在属性值列表中元素末尾添加几个元素
     * @param values 几个元素
     */
    public void add(T... values) {
        add(size(),values);
    }

    /**
     * 在属性值列表中插入几个元素
     * @param index 参数数组中第一个元素插入后的索引值
     * @param values 插入的几个元素
     */
    public boolean add(int index,T... values){
        int size = size();
        if (index>size) index = size;
        this.value.addAll(index,Arrays.asList(values));
        propertyAdded(index,values);
        return true;
    }

    /**
     * 设置属性值列表的索引值对应的元素
     * @param index 索引值
     * @param newValue  索引值对应的新元素
     * @return  索引值对应的旧元素
     */
    public T set(int index,T newValue){
        T value = this.value.get(index);
        this.value.set(index,newValue);
        propertyChanged(index,value,newValue);
        return value;
    }

    /**
     * 设置属性值列表的索引值对应的元素
     * @param index 索引值
     * @param newValues  索引值对应的新元素
     */
    public void set(int[] index,T... newValues){
        T[] oldValues = Arrays.copyOf(newValues,newValues.length);
        for (int i=0;i<newValues.length;i++){
            T value = this.value.get(index[i]);
            this.value.set(index[i],value);
            oldValues[i]=value;
        }
        propertyChanged(index,oldValues,newValues);
    }

    /**
     * 从属性值列表中移除该元素
     * @param value 元素
     * @return  true如果在属性值列表中找到了该元素；false如果在属性列表中找不到该元素
     */
    public boolean remove(T value){
        int index = indexOf(value);
        if (index<0)return false;
        this.value.remove(index);
        propertyRemoved(index,value);
        return true;
    }

    /**
     * 从属性值列表中移除几个元素
     * @param values
     */
    public void remove(T... values){
        int[] index = new int[values.length];
        for (int i=0;i<values.length;i++){
            T value = values[i];
            index[i]=indexOf(value);
            if (index[i]<0)continue;
            this.value.remove(index[i]);
        }
        propertyRemoved(index,values);
    }

    /**
     * 从属性值列表中移除索引值为index的元素
     * @param index 索引值
     * @return  被移除的元素
     */
    public T remove(int index) {
        T value = this.value.get(index);
        propertyRemoved(index,value);
        return value;
    }

    /**
     * 从属性值列表中移除索引值从fromIndex开始到toIndex结束（包含）的元素
     * @param fromIndex 起始索引值
     * @param toIndex   终止索引值（包含）
     */
    public void remove(int fromIndex,int toIndex){
        if (fromIndex>toIndex) throw new IllegalArgumentException("起始索引值不能大于终止索引值。fromIndex="+fromIndex+"，toIndex="+toIndex);
        int size=size();
        if (size<=fromIndex)return;
        if (size<=toIndex) toIndex=size-1;
        int[] index = new int[toIndex-fromIndex+1];
        Object[] values = new Object[index.length];
        for (int i=index.length-1;i>=0;i--){
            index[i]=fromIndex+i;
            values[i]=remove(index[i]);
        }
        propertyRemoved(index,values);
    }

    /**
     * 从属性值列表中移除所有元素
     */
    public void removeAll() {
        int size=size();
        int[] index = new int[size];
        Object[] values = new Object[index.length];
        for (int i=index.length-1;i>=0;i--){
            index[i]=i;
            values[i] = remove(index[i]);
        }
        propertyRemoved(index,values);
    }

    /**
     * 交换两个索引值对应的元素
     * @param indexA    索引值A
     * @param indexB    索引值B
     */
    public void exchange(int indexA, int indexB) {
        T valueA=get(indexA);
        T valueB=get(indexB);
        this.value.set(indexB,valueA);
        this.value.set(indexA,valueB);
        propertyExchanged(indexA,valueA,indexB,valueB);
    }

    public void addPropertyListener(ListPropertyListener<T> listener){
        if (listenerList==null) listenerList=new ArrayList<>(1);
        listenerList.add(listener);
    }

    public boolean removePropertyListener(ListPropertyListener<T> listener){
        return listenerList!=null && listenerList.remove(listener);
    }

    private void propertyAdded(int index,T[] value){
        if (listenerList==null)return;
        for (ListPropertyListener<T> listener:listenerList){
            listener.propertyAdded(index,value);
        }
    }

    private void propertyAdded(int index,T value){
        if (listenerList==null)return;
        for (ListPropertyListener<T>  listener:listenerList){
            listener.propertyAdded(index,value);
        }
    }

    private void propertyRemoved(int[] index,Object[] value){
        if (listenerList==null)return;
        for (ListPropertyListener<T>  listener:listenerList){
            listener.propertyRemoved(index,value);
        }
    }

    private void propertyRemoved(int index,T value){
        if (listenerList==null)return;
        for (ListPropertyListener<T>  listener:listenerList){
            listener.propertyRemoved(index,value);
        }
    }

    private void propertyChanged(int[] index,T[] oldValue,T[] newValue){
        if (listenerList==null)return;
        for (ListPropertyListener<T> listener:listenerList){
            listener.propertyChanged(index,oldValue,newValue);
        }
    }

    private void propertyChanged(int index,T oldValue,T newValue){
        if (listenerList==null)return;
        for (ListPropertyListener<T> listener:listenerList){
            listener.propertyChanged(index,oldValue,newValue);
        }
    }

    private void propertyExchanged(int indexA,T valueA,int indexB,T valueB){
        if (listenerList==null)return;
        for (ListPropertyListener<T> listener:listenerList){
            listener.propertyExchanged(indexA,valueA,indexB,valueB);
        }
    }

}