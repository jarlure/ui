package com.jarlure.ui.converter;

public abstract class PercentConverter {

    /**
     * 将屏幕坐标转换为百分比
     *
     * @param x 水平x值
     * @param y 垂直y值
     * @return 百分比
     */
    public abstract float getPercent(float x, float y);

    /**
     * 将百分比转换为屏幕水平坐标x值
     *
     * @param percent 百分比
     * @return 水平坐标x值
     */
    public abstract float getX(float percent);

    /**
     * 将百分比转换为屏幕垂直坐标y值
     *
     * @param percent 百分比
     * @return 垂直坐标y值
     */
    public abstract float getY(float percent);

}
