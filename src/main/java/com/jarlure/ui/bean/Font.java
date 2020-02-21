package com.jarlure.ui.bean;

import com.jme3.math.ColorRGBA;

public class Font {

    private String name;
    private int style;
    private int size;
    private ColorRGBA color;
    private int outlineWidth;
    private ColorRGBA outlineColor;

    public Font() {
    }

    public Font(String name) {
        this.name = name;
    }

    public Font(String name, int size, ColorRGBA color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    /**
     * 获取字体名称。例如：黑体
     *
     * @return 字体名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字体名称。在PC平台这里的字体名称将会设置进java.awt.Font中。Android平台这里的字体名称是一个相对asset文件夹的
     * 字体存放路径，例如：(src/main/assets/)Fonts/黑体.ttf
     *
     * @param name 字体名称。一般是操作系统Font库里后缀为.ttf的字体名。不含后缀名。
     * @return this
     */
    public Font setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获取字体样式。
     *
     * @return 字体样式
     */
    public int getStyle() {
        return style;
    }

    /**
     * 设置字体样式。在PC上这里的字体样式将会设置进java.awt.Font中。
     *
     * @param style 字体样式。
     */
    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * 获取字体最大高度。
     *
     * @return 字体最大高度。单位是像素
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置字体最大高度。字体的实际宽高跟字体名称、字体样式、字符有关。例如黑体字的宽高比为1:1而腾祥嘉丽中简黑的宽高比
     * 则跟字符字节数有关；粗体样式比普通样式的字宽些；'l'、'j'、'|'的高度可能会等于size，而宽度可能只有size/2；'a'的宽
     * 高均小于size；'啊'的宽高可能基本等于size。
     *
     * @param size 字体最大高度。单位是像素。
     * @return this
     */
    public Font setSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * 获取字体颜色。
     *
     * @return 字体颜色
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * 设置字体颜色。
     *
     * @param color 字体颜色
     * @return this
     */
    public Font setColor(ColorRGBA color) {
        this.color = color;
        return this;
    }

    /**
     * 获取字体描边边线宽度
     *
     * @return 描边边线宽度。单位是像素
     */
    public int getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * 设置字体描边边线宽度。单位是像素。目前不支持Android
     *
     * @param outlineWidth 字体描边边线宽度
     * @return this
     */
    public Font setOutlineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;
        return this;
    }

    /**
     * 获取字体描边边线颜色
     *
     * @return 字体描边边线颜色
     */
    public ColorRGBA getOutlineColor() {
        return outlineColor;
    }

    /**
     * 设置字体描边边线颜色。目前不支持Android
     *
     * @param outlineColor 描边边线颜色
     * @return this
     */
    public Font setOutlineColor(ColorRGBA outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

}
