package com.jarlure.ui.converter;

import com.jarlure.ui.property.AABB;

public class DynamicCoord {

    private AABB box;
    private float x;
    private float y;

    /**
     * 动态坐标点。用于标记组件上某点的位置。当组件发生平移或缩放时该点位置可以随之平移、缩放。常用于记录布局时两组件间
     * 的相对位置关系。
     * @param box   某组件的包围盒
     * @param x 水平坐标x值
     * @param y 垂直坐标y值
     */
    public DynamicCoord(AABB box, float x, float y) {
        this.box = box;
        this.x = (x - box.getXLeft()) / box.getWidth();
        this.y = (y - box.getYBottom()) / box.getHeight();
    }

    /**
     * 获得相对于包围盒左边的水平坐标x值
     * @return  相对于包围盒左边的水平坐标x值
     */
    public float getLocalX() {
        float x = box.getWidth() * this.x;
        return x;
    }

    /**
     * 获得相对于包围盒底边的垂直坐标y值
     * @return  相对于包围盒底边的垂直坐标y值
     */
    public float getLocalY() {
        float y = box.getHeight() * this.y;
        return y;
    }

    /**
     * 获得相对于屏幕底边的水平坐标x值
     * @return  相对于屏幕底边的水平坐标x值
     */
    public float getWorldX() {
        return getLocalX() + box.getXLeft();
    }

    /**
     * 获得相对于屏幕左边的垂直坐标y值
     * @return  相对于屏幕左边的垂直坐标y值
     */
    public float getWorldY() {
        return getLocalY() + box.getYBottom();
    }

}
