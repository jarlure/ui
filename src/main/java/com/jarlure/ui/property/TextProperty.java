package com.jarlure.ui.property;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.CustomProperty;
import com.jarlure.ui.property.common.CustomPropertyListener;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;
import com.jme3.util.NativeObjectManager;

import java.nio.ByteBuffer;

public class TextProperty extends CustomProperty implements WithUIComponent {

    public enum Property {
        TEXT, ALIGN, SRC, DES
    }

    protected String text;
    protected Direction align = Direction.LEFT;
    private Image src;
    public int startX, startY, endX, endY;
    public Image des;
    private int[] textPosInImg;

    /**
     * 用于反射自动创建。不要使用
     */
    public TextProperty() {
    }

    /**
     * 组件的文本属性
     *
     * @param background 文本背景图片
     */
    public TextProperty(Image background) {
        this.src = background;
        this.startX = 0;
        this.startY = 0;
        this.endX = background.getWidth();
        this.endY = background.getHeight();
    }

    @Override
    public void set(UIComponent component) {
        src = component.get(ImageProperty.class).getImage();
        startX = 0;
        startY = 0;
        endX = src.getWidth();
        endY = src.getHeight();
        CustomPropertyListener listener = (property, oldValue, newValue) -> {
            if (src == null) return;
            if (text == null) return;
            if (newValue == null) return;
            if (!property.equals(Property.SRC) && (newValue.equals(oldValue))) return;
            if (property.equals(Property.SRC) && !src.isUpdateNeeded()) return;
            if (property.equals(Property.DES)) return;
            if (property.equals(Property.SRC) && des != null) {
                des.dispose();
                if (!NativeObjectManager.UNSAFE) {
                    for (ByteBuffer buf : des.getData()) {
                        BufferUtils.destroyDirectBuffer(buf);
                    }
                }
                des = null;
            }
            Image des = this.des;
            {
                if (des == null) des = ImageHandler.clone(src);
                else ImageHandler.drawCut(des, 0, 0, src, startX, startY, endX, endY);
            }
            FontProperty fontProperty = component.get(FontProperty.class);
            int[] textPosInImg = ImageHandler.drawFont(des, fontProperty.getFont(), text, startX, startY, endX, endY, align);
            component.get(ImageProperty.class).setImage(des);
            setDes(des, textPosInImg);
        };
        component.get(FontProperty.class).addPropertyListener(listener);
        this.addPropertyListener(listener);
    }

    /**
     * 获取文本
     *
     * @return 当前文本
     */
    public String getText() {
        return (String) filterOutputProperty(Property.TEXT, text);
    }

    /**
     * 设置文本
     *
     * @param text 新的文本
     */
    public void setText(String text) {
        text = (String) filterInputProperty(Property.TEXT, text);
        if (text == null) return;
        String oldText = this.text;
        this.text = text;
        propertyChanged(Property.TEXT, oldText, text);
    }

    /**
     * 获取对齐方式。LEFT表示上下居中左对齐、CENTER表示上下左右居中、RIGHT表示上下居中右对齐。
     *
     * @return 对齐方式
     */
    public Direction getAlign() {
        return (Direction) filterOutputProperty(Property.ALIGN, align);
    }

    /**
     * 设置对齐方式
     *
     * @param align 新的对齐方式
     * @return this
     */
    public TextProperty setAlign(Direction align) {
        align = (Direction) filterInputProperty(Property.ALIGN, align);
        if (align == null) return this;
        Direction oldAlign = this.align;
        this.align = align;
        propertyChanged(Property.ALIGN, oldAlign, align);
        return this;
    }

    /**
     * 获取文本背景图片
     *
     * @return 文本背景图片
     */
    public Image getSrc() {
        return (Image) filterOutputProperty(Property.SRC, src);
    }

    /**
     * 设置文本背景图片以及文本范围。文本不会渲染到文本范围以外
     *
     * @param src    文本背景图片
     * @param startX 文本左边界
     * @param startY 文本底边界
     * @param endX   文本右边界
     * @param endY   文本顶边界
     */
    public void setSrc(Image src, int startX, int startY, int endX, int endY) {
        src = (Image) filterInputProperty(Property.SRC, src);
        if (src == null) return;
        Image oldSrc = this.src;
        this.src = src;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        propertyChanged(Property.SRC, oldSrc, src);
    }

    /**
     * 获取文本左边界
     *
     * @return 文本左边界
     */
    public int getStartX() {
        return startX;
    }

    /**
     * 获取文本底边界
     *
     * @return 文本底边界
     */
    public int getStartY() {
        return startY;
    }

    /**
     * 获取文本右边界
     *
     * @return 文本右边界
     */
    public int getEndX() {
        return endX;
    }

    /**
     * 获取文本顶边界
     *
     * @return 文本顶边界
     */
    public int getEndY() {
        return endY;
    }

    /**
     * 获取绘制好文本后的图片
     *
     * @return 绘制好文本后的图片
     */
    public Image getDes() {
        return (Image) filterOutputProperty(Property.DES, des);
    }

    /**
     * 设置绘制好文本后的图片
     *
     * @param des          绘制好文本后的图片
     * @param textPosInImg 文本中每个字符对应的起始位置坐标(x,y)。例如第4个字符的左下角坐标x=textPosInImg[8]、y=
     *                     textPosInImg[9]
     */
    public void setDes(Image des, int[] textPosInImg) {
        des = (Image) filterInputProperty(Property.DES, des);
        if (des == null) return;
        Image oldDes = this.des;
        this.des = des;
        this.textPosInImg = textPosInImg;
        propertyChanged(Property.DES, oldDes, des);
    }

    /**
     * 获取文本中每个字符对应的起始位置坐标(x,y)。映射关系是字符左下角坐标x=字符索引值*2、y=字符索引值*2+1
     *
     * @return 文本中每个字符对应的起始位置坐标(x, y)
     */
    public int[] getTextPosInImg() {
        return textPosInImg;
    }

}