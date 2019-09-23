package com.jarlure.ui.converter;

import com.jarlure.ui.property.AABB;
import com.jme3.math.FastMath;

public class ScrollConverter {

    private AABB window;
    private AABB object;
    private float minX, minY, maxX, maxY;

    /**
     * （滚动条的）滚动位置转换器。
     *
     * @param minX 允许滚动条移动的最小水平坐标x值
     * @param minY 允许滚动条移动的最小垂直坐标y值
     * @param maxX 允许滚动条移动的最大水平坐标x值
     * @param maxY 允许滚动条移动的最大垂直坐标y值
     */
    public ScrollConverter(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * 设置视窗窗口
     *
     * @param window 视窗窗口的包围盒
     */
    public void setWindow(AABB window) {
        this.window = window;
    }

    /**
     * 设置观察对象
     *
     * @param object 观察对象的包围盒
     */
    public void setObject(AABB object) {
        this.object = object;
    }

    /**
     * 获取滚动条最大宽度
     *
     * @return 滚动条最大宽度
     */
    public float getFullWidth() {
        return maxX - minX;
    }

    /**
     * 获取滚动条最大高度
     *
     * @return 滚动条最大高度
     */
    public float getFullHeight() {
        return maxY - minY;
    }

    /**
     * 获取滚动条的宽度缩放比例
     *
     * @return 滚动条的宽度缩放比例
     */
    public float getPercentWidth() {
        return FastMath.clamp(window.getWidth() / object.getWidth(), 0, 1);
    }

    /**
     * 获取滚动条的高度缩放比例
     *
     * @return 滚动条的高度缩放比例
     */
    public float getPercentHeight() {
        return FastMath.clamp(window.getHeight() / object.getHeight(), 0, 1);
    }

    /**
     * 计算滚动条的宽度
     *
     * @return 计算得到的滚动条的宽度
     */
    public float getWidth() {
        return getPercentWidth() * getFullWidth();
    }

    /**
     * 计算滚动条的高度
     *
     * @return 计算得到的滚动条的高度
     */
    public float getHeight() {
        return getPercentHeight() * getFullHeight();
    }

    /**
     * 计算滚动条水平左边位置坐标x值
     *
     * @return 计算得到的滚动条水平左边位置坐标x值
     */
    public float getXLeft() {
        float percent = FastMath.clamp((window.getXLeft() - object.getXLeft()) / object.getWidth(), 0, 1);
        return Math.min(minX + percent * getFullWidth(), maxX - getWidth());
    }

    /**
     * 计算滚动条垂直顶边位置坐标y值
     *
     * @return 计算得到的滚动条垂直顶边位置坐标y值
     */
    public float getYTop() {
        float percent = FastMath.clamp((object.getYTop() - window.getYTop()) / object.getHeight(), 0, 1);
        return Math.max(maxY - percent * getFullHeight(), minY + getHeight());
    }

    /**
     * 计算当滚动条左边位置作为输入值时，观察对象的左边应当处于的水平坐标x值
     *
     * @param scrollXLeft 滚动条左边位置水平坐标x值
     * @return 观察对象的左边应当处于的水平坐标x值
     */
    public float getObjectXLeft(float scrollXLeft) {
        if (scrollXLeft < minX) scrollXLeft = minX;
        else scrollXLeft = Math.min(scrollXLeft, maxX - getWidth());
        float percent = FastMath.clamp((scrollXLeft - minX) / getFullWidth(), 0, 1);
        return window.getXLeft() - percent * object.getWidth();
    }

    /**
     * 计算当滚动条顶边位置作为输入值时，观察对象的顶边应当处于的垂直坐标y值
     *
     * @param scrollYTop 滚动条顶边位置垂直坐标y值
     * @return 观察对象的顶边应当处于的垂直坐标y值
     */
    public float getObjectYTop(float scrollYTop) {
        if (scrollYTop > maxY) scrollYTop = maxY;
        else scrollYTop = Math.max(scrollYTop, minY + getHeight());
        float percent = FastMath.clamp((maxY - scrollYTop) / getFullHeight(), 0, 1);
        return window.getYTop() + percent * object.getHeight();
    }

}
