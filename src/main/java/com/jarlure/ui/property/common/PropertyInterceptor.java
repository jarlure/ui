package com.jarlure.ui.property.common;

public interface PropertyInterceptor<T> {

    /**
     * 判断输入值是否可以通过检查。该方法常用于外部执行设置值操作，内部对该值进行检查防止设置不合法值的情景
     * @param value   接受检查的值
     * @return  true如果该值通过了检查；false如果该值不能通过检查
     */
    boolean interceptProperty(T value);

}