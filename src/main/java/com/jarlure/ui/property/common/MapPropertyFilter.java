package com.jarlure.ui.property.common;

public interface MapPropertyFilter<K,V> {

    default boolean filterAddedProperty(K key, V value){
        return true;
    }

    default boolean filterRemovedProperty(K key, V value){
        return true;
    }

    default boolean filterChangedProperty(K key, V oldValue, V newValue){
        return true;
    }

}