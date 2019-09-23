package com.jarlure.ui.property.common;

public interface MapPropertyListener<K, V> {

    void propertyAdded(K key, V value);

    void propertyRemoved(K key, V value);

    void propertyChanged(K key, V oldValue, V newValue);

}