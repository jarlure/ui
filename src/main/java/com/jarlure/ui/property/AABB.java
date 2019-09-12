package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.MapPropertyListener;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Quaternion;

import java.util.ArrayList;

public class AABB implements WithUIComponent {

    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String THICKNESS = "thickness";

    protected SpatialProperty spatialProperty;
    protected ArrayList<MapPropertyListener<String, Float>> listenerList;

    /**
     * 用于反射自动创建。不要使用
     */
    public AABB() {
    }

    /**
     * 正交包围盒。正交包围盒总是将组件看作一个正正方方的、没有旋转过的矩形。因此你可以通过获得矩形的宽、高以及它的四条
     * 边或几何中心点确定组件的位置和尺寸。同时你也可以设置矩形的宽、高进而改变组件的尺寸。
     *
     * @param spatialProperty Spatial属性
     */
    public AABB(SpatialProperty spatialProperty) {
        this.spatialProperty = spatialProperty;
    }

    @Override
    public void set(UIComponent component) {
        this.spatialProperty = component.get(SpatialProperty.class);
    }

    /**
     * 获得正交包围盒的半个宽度
     *
     * @return 正交包围盒的半个宽度
     */
    public float getXExtent() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return box.getXExtent();
    }

    /**
     * 获得正交包围盒的半个高度
     *
     * @return 正交包围盒的半个高度
     */
    public float getYExtent() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return box.getYExtent();
    }

    /**
     * 获得正交包围盒的半个厚度
     *
     * @return 正交包围盒的半个厚度
     */
    public float getZExtent() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return box.getZExtent();
    }

    /**
     * 获得正交包围盒的几何中心水平坐标位置x值
     *
     * @return 正交包围盒的几何中心水平坐标位置x值
     */
    public float getXCenter() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return spatialProperty.getWorldTranslation().getX();
        return box.getCenter().getX();
    }

    /**
     * 获得正交包围盒的几何中心垂直坐标位置y值
     *
     * @return 正交包围盒的几何中心垂直坐标位置y值
     */
    public float getYCenter() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return spatialProperty.getWorldTranslation().getY();
        return box.getCenter().getY();
    }

    /**
     * 获得正交包围盒的几何中心纵深坐标位置z值
     *
     * @return 正交包围盒的几何中心纵深坐标位置z值
     */
    public float getZCenter() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return spatialProperty.getWorldTranslation().getZ();
        return box.getCenter().getZ();
    }

    /**
     * 获得正交包围盒的宽度
     *
     * @return 正交包围盒的宽度
     */
    public float getWidth() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return 2 * box.getXExtent();
    }

    /**
     * 设置正交包围盒的宽度
     *
     * @param width 新的宽度
     */
    public void setWidth(float width) {
        float oldWidth = getWidth();
        if (width == oldWidth) {
            spatialProperty.worldScaleChanged();
            propertyChanged(WIDTH, oldWidth, width);
            return;
        }
        float scale = width / oldWidth;
        Quaternion rotation = spatialProperty.getWorldRotation();
        if (rotation.isIdentity()) {
            spatialProperty.scale(scale, 1f, 1f);
        } else {
            //rotation.inverse();
            float z = -rotation.getZ();
            float w = rotation.getW();
            //inverse.multLocal(Vector3f.UnitX);
            float x = w * w - z * z;
            float y = 2 * w * z;
            spatialProperty.scale(x * scale, y * scale, 1f);
        }
        propertyChanged(WIDTH, oldWidth, width);
    }

    /**
     * 获得正交包围盒的高度
     *
     * @return 正交包围盒的高度
     */
    public float getHeight() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return 2 * box.getYExtent();
    }

    /**
     * 设置正交包围盒的高度
     *
     * @param height 正交包围盒的高度
     */
    public void setHeight(float height) {
        float oldHeight = getHeight();
        if (height == oldHeight) {
            spatialProperty.worldScaleChanged();
            propertyChanged(HEIGHT, oldHeight, height);
            return;
        }
        float scale = height / oldHeight;
        Quaternion rotation = spatialProperty.getWorldRotation();
        if (rotation.isIdentity()) {
            spatialProperty.scale(1f, scale, 1f);
        } else {
            //rotation.inverse();
            float z = -rotation.getZ();
            float w = rotation.getW();
            //inverse.multLocal(Vector3f.UnitX);
            float x = -2 * z * w;
            float y = -z * z + w * w;
            spatialProperty.scale(x * scale, y * scale, 1f);
        }
        propertyChanged(HEIGHT, oldHeight, height);
    }

    /**
     * 获得正交包围盒的厚度
     * @return  正交包围盒的厚度
     */
    public float getThickness() {
        BoundingBox box = (BoundingBox) spatialProperty.getWorldBound();
        if (box == null) return 0;
        return 2 * box.getZExtent();
    }

    /**
     * 设置正交包围盒的厚度
     * @param thickness 正交包围盒的厚度
     */
    public void setThickness(float thickness) {
        float oldThickness = getThickness();
        if (thickness == oldThickness) {
            spatialProperty.worldScaleChanged();
            propertyChanged(THICKNESS, oldThickness, thickness);
            return;
        }
        float scale = thickness / oldThickness;
        spatialProperty.scale(1f, 1f, scale);
        propertyChanged(THICKNESS, oldThickness, thickness);
    }

    /**
     * 获得正交包围盒的左边水平坐标位置x值
     * @return  正交包围盒的左边水平坐标位置x值
     */
    public float getXLeft() {
        return getXCenter() - getXExtent();
    }

    /**
     * 获得正交包围盒的右边水平坐标位置x值
     * @return  正交包围盒的右边水平坐标位置x值
     */
    public float getXRight() {
        return getXCenter() + getXExtent();
    }

    /**
     * 获得正交包围盒的顶边垂直坐标位置y值
     * @return  正交包围盒的顶边垂直坐标位置y值
     */
    public float getYBottom() {
        return getYCenter() - getYExtent();
    }

    /**
     * 获得正交包围盒的底边垂直坐标位置y值
     * @return  正交包围盒的底边垂直坐标位置y值
     */
    public float getYTop() {
        return getYCenter() + getYExtent();
    }

    /**
     * 判断坐标点是否在正交包围盒内部或边上
     * @param x 水平坐标位置x值
     * @param y 垂直坐标位置y值
     * @return  如果坐标点落入正交包围盒内部或边上则返回true；否则返回false
     */
    public boolean contains(float x, float y) {
        float maxY = getYTop();
        if (maxY < y) return false;
        float minY = getYBottom();
        if (y < minY) return false;
        float minX = getXLeft();
        if (x < minX) return false;
        float maxX = getXRight();
        if (maxX < x) return false;

        return true;
    }

    /**
     * 允许给正交包围盒添加监听器箭头包围盒尺寸的变化
     * @param listener  监听器
     */
    public void addPropertyListener(MapPropertyListener<String, Float> listener) {
        if (listenerList == null) listenerList = new ArrayList<>();
        listenerList.add(listener);
    }

    /**
     * 移除添加的监听器
     * @param listener  要移除的监听器
     */
    public void removePropertyListener(MapPropertyListener<String, Float> listener) {
        if (listenerList == null) return;
        listenerList.remove(listener);
    }

    private void propertyChanged(String key, Float oldValue, Float newValue) {
        if (listenerList == null) return;
        for (MapPropertyListener<String, Float> listener : listenerList) {
            listener.propertyChanged(key, oldValue, newValue);
        }
    }

    @Override
    public String toString() {
        return "width=" + getWidth() + ".height=" + getHeight() + ",left=" + getXLeft() + ",bottom=" + getYBottom() + ",right=" + getXRight() + ",top=" + getYTop();
    }

}