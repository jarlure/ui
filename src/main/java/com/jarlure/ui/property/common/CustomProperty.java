package com.jarlure.ui.property.common;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomProperty {

    protected List<CustomPropertyFilter> inputFilterList;
    protected List<CustomPropertyFilter> outputFilterList;
    protected List<CustomPropertyInterceptor> interceptorList;
    protected List<CustomPropertyListener> listenerList;

    /**
     * 添加属性值过滤器。该过滤器会在外部设置属性值时触发。
     *
     * @param inputFilter 在外部设置属性值时触发的过滤器
     */
    public void addInputPropertyFilter(CustomPropertyFilter inputFilter) {
        if (inputFilterList == null) inputFilterList = new ArrayList<>(1);
        inputFilterList.add(inputFilter);
    }

    /**
     * 移除在外部设置属性值时触发的过滤器。
     *
     * @param inputFilter 在外部设置属性值时触发的过滤器
     * @return true如果能找到该过滤器；false如果找不到该过滤器
     */
    public boolean removeInputPropertyFilter(CustomPropertyFilter inputFilter) {
        return inputFilterList != null && inputFilterList.remove(inputFilter);
    }

    /**
     * 添加属性值过滤器。该过滤器会在外部获取属性值时触发。
     *
     * @param outputFilter 在外部获取属性值时触发的过滤器
     */
    public void addOutputPropertyFilter(CustomPropertyFilter outputFilter) {
        if (outputFilterList == null) outputFilterList = new ArrayList<>(1);
        outputFilterList.add(outputFilter);
    }

    /**
     * 移除在外部获取属性值时触发的过滤器。
     *
     * @param outputFilter 在外部获取属性值时触发的过滤器
     * @return true如果能找到该过滤器；false如果找不到该过滤器
     */
    public boolean removeOutputPropertyFilter(CustomPropertyFilter outputFilter) {
        return outputFilterList != null && outputFilterList.remove(outputFilter);
    }

    /**
     * 依次调用设置属性值过滤器列表中过滤器的过滤方法。
     *
     * @param property 属性名
     * @param value    接受过滤的值
     * @return 过滤后的值
     */
    protected Object filterInputProperty(Enum property, Object value) {
        if (inputFilterList == null) return value;
        for (CustomPropertyFilter filter : inputFilterList) {
            value = filter.filterProperty(property, value);
        }
        return value;
    }

    /**
     * 依次调用获取属性值过滤器列表中过滤器的过滤方法。
     *
     * @param property 属性名
     * @param value    接受过滤的值
     * @return 过滤后的值
     */
    protected Object filterOutputProperty(Enum property, Object value) {
        if (outputFilterList == null) return value;
        for (CustomPropertyFilter filter : outputFilterList) {
            value = filter.filterProperty(property, value);
        }
        return value;
    }

    /**
     * 添加属性值拦截器。该拦截器会在外部设置属性值时触发。
     *
     * @param interceptor 在外部设置属性值时触发的拦截器
     */
    public void addPropertyInterceptor(CustomPropertyInterceptor interceptor) {
        if (interceptorList == null) interceptorList = new ArrayList<>(1);
        interceptorList.add(interceptor);
    }

    /**
     * 移除属性值拦截器。
     *
     * @param interceptor 在外部设置属性值时触发的拦截器
     * @return true如果能找到该拦截器；false如果找不到该拦截器
     */
    public boolean removePropertyInterceptor(CustomPropertyInterceptor interceptor) {
        return interceptorList != null && interceptorList.remove(interceptor);
    }

    /**
     * 依次调用拦截器列表中拦截器的拦截方法对值进行检测拦截。注意：所有拦截器都会被触发一次。
     *
     * @param property 属性名
     * @param value    接受检查的属性值
     * @return true如果该值通过检测；false如果该值被拦截
     */
    protected boolean interceptProperty(Enum property, Object value) {
        boolean pass = true;
        if (interceptorList == null) return pass;
        for (CustomPropertyInterceptor filter : interceptorList) {
            pass = filter.interceptProperty(property, value) && pass;
        }
        return pass;
    }

    /**
     * 添加属性值监听器。当属性值发生更新后触发该监听器
     *
     * @param listener 在外部设置属性值时触发的监听器
     */
    public void addPropertyListener(CustomPropertyListener listener) {
        if (listenerList == null) listenerList = new ArrayList<>(1);
        listenerList.add(listener);
    }

    /**
     * 移除属性值监听器。
     *
     * @param listener 要移除的监听器
     * @return true如果能找到该监听器；false如果找不到该监听器
     */
    public boolean removePropertyListener(CustomPropertyListener listener) {
        return listenerList != null && listenerList.remove(listener);
    }

    protected void propertyChanged(Enum property){
        propertyChanged(property,null,null);
    }

    /**
     * 依次调用监听器列表中监听器的属性值更新通知方法。
     *
     * @param property 属性名
     * @param oldValue 更新前的属性值
     * @param newValue 更新后的属性值。可能跟更新前的属性值一样
     */
    protected void propertyChanged(Enum property, Object oldValue, Object newValue) {
        if (listenerList == null) return;
        for (CustomPropertyListener listener : listenerList) {
            listener.propertyChanged(property, oldValue, newValue);
        }
    }

}
