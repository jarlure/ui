package com.jarlure.ui.property.common;

public interface PropertyFilter<T> {

    /**
     * 过滤输入值。该方法常用于外部执行设置值操作，内部对该值进行过滤将超过范围的非法值转变为范围内的合法值的情景
     * @param value   接受过滤的值
     * @return 过滤后的值
     */
    T filterProperty(T value);

}
