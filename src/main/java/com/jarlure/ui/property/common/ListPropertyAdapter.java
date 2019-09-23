package com.jarlure.ui.property.common;

public class ListPropertyAdapter<T> implements ListPropertyListener<T> {

    @Override
    public void propertyAdded(int index,T[] value){
        for (int i=0;i<value.length;i++){
            propertyAdded(index+i,value[i]);
        }
    }

    @Override
    public void propertyAdded(int index, T value) {

    }

    @Override
    public void propertyRemoved(int[] index,Object[] value){
        for (int i=0;i<index.length;i++){
            propertyRemoved(index[i], (T) value[i]);
        }
    }

    @Override
    public void propertyRemoved(int index, T value) {

    }

    @Override
    public void propertyChanged(int[] index, T[] oldValue, T[] newValue){
        for (int i=0;i<index.length;i++){
            propertyChanged(index[i],oldValue[i],newValue[i]);
        }
    }

    @Override
    public void propertyChanged(int index, T oldValue, T newValue) {

    }

    @Override
    public void propertyExchanged(int[] indexA, T[] valueA, int[] indexB, T[] valueB){
        for (int i=0;i<indexA.length;i++){
            propertyExchanged(indexA[i],valueA[i],indexB[i],valueB[i]);
        }
    }

    @Override
    public void propertyExchanged(int indexA, T valueA, int indexB, T valueB) {

    }

    protected void foldAnonymousInnerClassCode(ListPropertyAdapter instance) {}

}
