package com.jarlure.ui.property;

import com.jarlure.ui.bean.Font;
import com.jarlure.ui.property.common.EnumProperty;
import com.jme3.math.ColorRGBA;

public class FontProperty extends EnumProperty {

    public enum Property {
        NAME, SIZE, COLOR, OUTLINE_WIDTH, OUTLINE_COLOR
    }

    private Font font;

    public FontProperty() {
        this(new Font());
    }

    /**
     * 组件的字体属性。如果要触发监听器，请直接使用该属性提供的方法设置字体的名称、颜色、尺寸等参数；否则建议先获取Font
     * 然后直接设置，这样可以绕开监听器提高效率。
     *
     * @param font 字体参数
     */
    public FontProperty(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public String getName() {
        return font.getName();
    }

    public void setName(String name) {
        String oldValue = getName();
        font.setName(name);
        propertyChanged(Property.NAME, oldValue, name);
    }

    public int getSize() {
        return font.getSize();
    }

    public void setSize(int size) {
        int oldSize = getSize();
        font.setSize(size);
        propertyChanged(Property.SIZE, oldSize, size);
    }

    public ColorRGBA getColor() {
        return font.getColor();
    }

    public void setColor(ColorRGBA color) {
        ColorRGBA oldColor = getColor();
        font.setColor(color);
        propertyChanged(Property.COLOR, oldColor, color);
    }

    public int getOutlineWidth() {
        return font.getOutlineWidth();
    }

    public void setOutlineWidth(int outlineWidth) {
        int oldOutlineWidth = getOutlineWidth();
        font.setOutlineWidth(outlineWidth);
        propertyChanged(Property.OUTLINE_WIDTH, oldOutlineWidth, outlineWidth);
    }

    public ColorRGBA getOutlineColor() {
        return font.getOutlineColor();
    }

    public void setOutlineColor(ColorRGBA outlineColor) {
        ColorRGBA oldOutlineColor = getOutlineColor();
        font.setOutlineColor(outlineColor);
        propertyChanged(Property.OUTLINE_COLOR, oldOutlineColor, outlineColor);
    }

}