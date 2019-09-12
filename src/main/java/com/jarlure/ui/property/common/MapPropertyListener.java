package com.jarlure.ui.property.common;

public interface MapPropertyListener<K,V> {

    default void propertyAdded(K key, V value){
    }

    default void propertyRemoved(K key, V value){
    }

    default void propertyChanged(K key, V oldValue, V newValue){
    }

    default void foldAnonymousInnerClassCode(MapPropertyListener instance) {
    }

}