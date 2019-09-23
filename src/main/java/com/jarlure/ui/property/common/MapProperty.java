package com.jarlure.ui.property.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapProperty<K, V> {

    public Map<K, V> value;
    protected List<MapPropertyListener<K, V>> listenerList;

    public MapProperty() {
        this(new HashMap<>());
    }

    public MapProperty(Map<K, V> value) {
        this.value = value;
    }

    /**
     * @return true如果属性值映射表为空；false如果属性值列表不为空
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * @param key 键
     * @return true如果属性值映射表中有该键；false如果属性值列表中无该键
     */
    public boolean existKey(K key) {
        return this.value.containsKey(key);
    }

    /**
     * @param value 值
     * @return true如果属性值映射表中有该值；false如果属性值列表中无该值
     */
    public boolean existValue(V value) {
        return this.value.containsValue(value);
    }

    /**
     * 通过键获取该键映射的值。如果键不存在，则返回默认值
     *
     * @param key   键
     * @param value 默认值
     * @return 该键映射的值。但如果键不存在，则返回默认值
     */
    public V getOrDefault(K key, V value) {
        return this.value.getOrDefault(key, value);
    }

    /**
     * 通过键获取该键映射的值。
     *
     * @param key 键
     * @return 该键映射的值。可能为null
     */
    public V getValue(K key) {
        return this.value.get(key);
    }

    /**
     * 通过值获取该值对应的键。
     *
     * @param value 值
     * @return 该值对应的键。可能为null
     */
    public K getKey(V value) {
        for (Map.Entry<K, V> entry : this.value.entrySet()) {
            if (entry.getValue().equals(value)) return entry.getKey();
        }
        return null;
    }

    /**
     * 设置键值映射。
     *
     * @param key   键
     * @param value 值
     * @return 未设置前该键映射的旧有的值。
     */
    public V put(K key, V value) {
        V oldValue = this.value.put(key, value);
        if (oldValue == null) propertyAdded(key, value);
        else propertyChanged(key, oldValue, value);
        return oldValue;
    }

    /**
     * 移除键值映射。
     *
     * @param key   键
     * @param value 值
     * @return true如果键值被移除；false如果给定键或值与属性值映射表中的不匹配。
     */
    public boolean remove(K key, V value) {
        boolean success = this.value.remove(key, value);
        if (success) propertyRemoved(key, value);
        return success;
    }

    /**
     * 移除键。
     *
     * @param key 键
     * @return 键映射的值
     */
    public V remove(K key) {
        V value = this.value.get(key);
        boolean success = remove(key, value);
        if (!success) return null;
        return value;
    }

    public void addPropertyListener(MapPropertyListener<K, V> listener) {
        if (listenerList == null) listenerList = new ArrayList<>();
        listenerList.add(listener);
    }

    public void removePropertyListener(MapPropertyListener listener) {
        if (listenerList == null) return;
        listenerList.remove(listener);
    }

    private void propertyAdded(K key, V value) {
        if (listenerList == null) return;
        for (MapPropertyListener<K, V> listener : listenerList) {
            listener.propertyAdded(key, value);
        }
    }

    private void propertyRemoved(K key, V value) {
        if (listenerList == null) return;
        for (MapPropertyListener<K, V> listener : listenerList) {
            listener.propertyRemoved(key, value);
        }
    }

    private void propertyChanged(K key, V oldValue, V newValue) {
        if (listenerList == null) return;
        for (MapPropertyListener<K, V> listener : listenerList) {
            listener.propertyChanged(key, oldValue, newValue);
        }
    }

}