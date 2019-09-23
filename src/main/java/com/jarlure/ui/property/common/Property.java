package com.jarlure.ui.property.common;

import java.util.ArrayList;
import java.util.List;

public class Property<T> {

    public T value;
    protected List<PropertyFilter<T>> inputFilterList;
    protected List<PropertyFilter<T>> outputFilterList;
    protected List<PropertyInterceptor<T>> interceptorList;
    protected List<PropertyListener<T>> listenerList;

    public Property() {
    }

    public Property(T value) {
        this.value = value;
    }

    /**
     * 获取属性值
     *
     * @return 当前的属性值
     */
    public T getValue() {
        T value = this.value;
        if (outputFilterList != null) {
            for (PropertyFilter<T> filter : outputFilterList) {
                value = filter.filterProperty(value);
            }
        }
        return value;
    }

    /**
     * 设置属性值。注意：要设置的值会经过过滤、拦截，最后设置时可能与输入时不同。
     *
     * @param value 要设置的属性值
     */
    public void setValue(T value) {
        value = filterInputProperty(value);
        boolean pass = interceptProperty(value);
        if (!pass) return;
        T oldValue = this.value;
        this.value = value;
        propertyChanged(oldValue, value);
    }

    /**
     * 添加属性值过滤器。该过滤器会在外部设置属性值时触发。
     *
     * @param inputFilter 在外部设置属性值时触发的过滤器
     */
    public void addInputPropertyFilter(PropertyFilter<T> inputFilter) {
        if (inputFilterList == null) inputFilterList = new ArrayList<>(1);
        inputFilterList.add(inputFilter);
    }

    /**
     * 移除在外部设置属性值时触发的过滤器。
     *
     * @param inputFilter 在外部设置属性值时触发的过滤器
     * @return true如果能找到该过滤器；false如果找不到该过滤器
     */
    public boolean removeInputPropertyFilter(PropertyFilter<T> inputFilter) {
        return inputFilterList != null && inputFilterList.remove(inputFilter);
    }

    protected T filterInputProperty(T value) {
        if (inputFilterList != null) {
            for (PropertyFilter<T> filter : inputFilterList) {
                value = filter.filterProperty(value);
            }
        }
        return value;
    }

    /**
     * 添加属性值过滤器。该过滤器会在外部获取属性值时触发。
     *
     * @param outputFilter 在外部获取属性值时触发的过滤器
     */
    public void addOutputPropertyFilter(PropertyFilter<T> outputFilter) {
        if (outputFilterList == null) outputFilterList = new ArrayList<>(1);
        outputFilterList.add(outputFilter);
    }

    /**
     * 移除在外部获取属性值时触发的过滤器。
     *
     * @param outputFilter 在外部获取属性值时触发的过滤器
     * @return true如果能找到该过滤器；false如果找不到该过滤器
     */
    public boolean removeOutputPropertyFilter(PropertyFilter<T> outputFilter) {
        return outputFilterList != null && outputFilterList.remove(outputFilter);
    }

    /**
     * 添加属性值拦截器。该拦截器会在外部设置属性值时触发。
     *
     * @param interceptor 在外部设置属性值时触发的拦截器
     */
    public void addPropertyInterceptor(PropertyInterceptor<T> interceptor) {
        if (interceptorList == null) interceptorList = new ArrayList<>(1);
        interceptorList.add(interceptor);
    }

    /**
     * 移除属性值拦截器。
     *
     * @param interceptor 在外部设置属性值时触发的拦截器
     * @return true如果能找到该拦截器；false如果找不到该拦截器
     */
    public boolean removePropertyInterceptor(PropertyInterceptor interceptor) {
        return interceptorList != null && interceptorList.remove(interceptor);
    }

    /**
     * 依次调用拦截器列表中拦截器的拦截方法对值进行检测拦截。注意：所有拦截器都会被触发一次。
     *
     * @param value 待检测的对象
     * @return true如果该值通过检测；false如果该值被拦截
     */
    protected boolean interceptProperty(T value) {
        boolean pass = true;
        if (interceptorList == null) return pass;
        for (PropertyInterceptor<T> interceptor : interceptorList) {
            pass = interceptor.interceptProperty(value) && pass;//触发所有的拦截器
        }
        return pass;
    }

    /**
     * 添加属性值监听器。当属性值发生更新后触发该监听器
     *
     * @param listener 在外部设置属性值时触发的监听器
     */
    public void addPropertyListener(PropertyListener<T> listener) {
        if (listenerList == null) listenerList = new ArrayList<>(1);
        listenerList.add(listener);
    }

    /**
     * 移除属性值监听器。
     *
     * @param listener 要移除的监听器
     * @return true如果能找到该监听器；false如果找不到该监听器
     */
    public boolean removePropertyListener(PropertyListener listener) {
        return listenerList != null && listenerList.remove(listener);
    }

    /**
     * 依次调用监听器列表中监听器的属性值更新通知方法。
     *
     * @param oldValue 更新前的属性值
     * @param newValue 更新后的属性值。可能跟更新前的属性值一样
     */
    protected void propertyChanged(T oldValue, T newValue) {
        if (listenerList == null) return;
        for (PropertyListener<T> listener : listenerList) {
            listener.propertyChanged(oldValue, newValue);
        }
    }

}
