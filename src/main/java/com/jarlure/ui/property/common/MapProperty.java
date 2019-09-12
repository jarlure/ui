package com.jarlure.ui.property.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapProperty<K,V> {

    public Map<K,V> value;
    protected List<MapPropertyFilter<K,V>> filterList;
    protected List<MapPropertyListener<K,V>> listenerList;

    public MapProperty(){
        this(new HashMap<>());
    }

    public MapProperty(Map<K,V> value){
        this.value=value;
    }

    public boolean existKey(K key){
        return this.value.containsKey(key);
    }

    public boolean existValue(V value){
        return this.value.containsValue(value);
    }

    public V getOrDefault(K key,V value){
        return this.value.getOrDefault(key,value);
    }

    public V getValue(K key){
        return this.value.get(key);
    }

    public K getKey(V value){
        for (Map.Entry<K,V> entry:this.value.entrySet()){
            if (entry.getValue().equals(value)) return entry.getKey();
        }
        return null;
    }

    public V put(K key,V value){
        boolean pass=filterAddedProperty(key,value);
        if (!pass)return value;
        V oldValue = this.value.put(key,value);
        if (oldValue==null) propertyAdded(key,value);
        else propertyChanged(key,oldValue,value);
        return oldValue;
    }

    public boolean remove(K key,V value){
        boolean pass = filterRemovedProperty(key,value);
        if (!pass)return false;
        boolean success = this.value.remove(key,value);
        if (success) propertyRemoved(key,value);
        return success;
    }

    public V remove(K key){
        V value = this.value.get(key);
        boolean success=remove(key,value);
        if (!success) return null;
        return value;
    }

    public void addPropertyFilter(MapPropertyFilter filter){
        if (filterList==null)filterList=new ArrayList<>();
        filterList.add(filter);
    }

    public void removePropertyFilter(MapPropertyFilter filter){
        if (filterList==null)return;
        filterList.remove(filter);
    }

    protected boolean filterAddedProperty(K key,V value){
        boolean pass=true;
        if (filterList==null)return pass;
        for (MapPropertyFilter<K,V> filter:filterList){
            pass = filter.filterAddedProperty(key,value) && pass;
        }
        return pass;
    }

    protected boolean filterRemovedProperty(K key,V value){
        boolean pass=true;
        if (filterList==null)return pass;
        for (MapPropertyFilter filter:filterList){
            pass = filter.filterRemovedProperty(key,value) && pass;
        }
        return pass;
    }

    protected boolean filterChangedProperty(K key,V oldValue,V newValue){
        boolean pass = true;
        if (filterList==null)return pass;
        for (MapPropertyFilter filter:filterList){
            pass = filter.filterChangedProperty(key,oldValue,newValue) && pass;
        }
        return pass;
    }

    public void addPropertyListener(MapPropertyListener listener){
        if (listenerList==null)listenerList=new ArrayList<>();
        listenerList.add(listener);
    }

    public void removePropertyListener(MapPropertyListener listener){
        if (listenerList==null)return;
        listenerList.remove(listener);
    }

    private void propertyAdded(K key,V value){
        if (listenerList==null)return;
        for (MapPropertyListener<K,V> listener:listenerList){
            listener.propertyAdded(key,value);
        }
    }

    private void propertyRemoved(K key,V value){
        if (listenerList==null)return;
        for (MapPropertyListener<K,V> listener:listenerList){
            listener.propertyRemoved(key,value);
        }
    }

    private void propertyChanged(K key,V oldValue,V newValue){
        if (listenerList==null)return;
        for (MapPropertyListener<K,V> listener:listenerList){
            listener.propertyChanged(key,oldValue,newValue);
        }
    }

}