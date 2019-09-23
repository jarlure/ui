package com.jarlure.ui.effect;

import com.jarlure.ui.property.FontProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;

public class TextLineEditEffect extends TextEditEffect {

    protected FontProperty fontProperty;
    protected TextProperty textProperty;
    protected int fromIndex, toIndex;
    protected ColorRGBA cursorColor;
    protected boolean isCursorShowing;
    protected int[] textMarkForCursor;
    protected ColorRGBA selectionBackgroundColor;
    protected ColorRGBA selectionTextColor;
    protected int[] textMarkForSelection;

    /**
     * 单行文本编辑效果
     *
     * @param fontProperty 字体属性。用于提供字体名称、样式、颜色、尺寸等信息
     * @param textProperty 文本属性。用于提供文本区域等信息
     */
    public TextLineEditEffect(FontProperty fontProperty, TextProperty textProperty) {
        this.fontProperty = fontProperty;
        this.textProperty = textProperty;
        this.cursorColor = ColorRGBA.Black;
        this.selectionBackgroundColor = new ColorRGBA(0f, 0.5f, 1f, 1f);
        this.selectionTextColor = ColorRGBA.White;
        textProperty.addPropertyListener((property, oldValue, newValue) -> {
            if (property.equals(TextProperty.Property.DES)) {
                isCursorShowing = false;
                textMarkForCursor = null;
                textMarkForSelection = null;
            }
        });
    }

    /**
     * 设置光标颜色。默认为黑色
     *
     * @param cursorColor 光标颜色
     */
    public void setCursorColor(ColorRGBA cursorColor) {
        this.cursorColor = cursorColor;
    }

    /**
     * 设置选中文本背景色。默认为天蓝色
     *
     * @param selectionBackgroundColor 选中文本背景色
     */
    public void setSelectionBackgroundColor(ColorRGBA selectionBackgroundColor) {
        this.selectionBackgroundColor = selectionBackgroundColor;
    }

    /**
     * 设置选中文本字体色。默认为白色
     *
     * @param selectionTextColor 选中文本字体色
     */
    public void setSelectionTextColor(ColorRGBA selectionTextColor) {
        this.selectionTextColor = selectionTextColor;
    }

    @Override
    public void setCursorPosition(int row, int column) {
        clearCursor();
        timer = 0;
        clearSelect();

        fromIndex = toIndex = column;
    }

    @Override
    public void selectAll() {
        int toColumn = textProperty.getText().length();
        select(0, 0, 0, toColumn);
    }

    @Override
    public void select(int fromRow, int fromColumn, int toRow, int toColumn) {
        clearCursor();
        timer = 0;
        clearSelect();

        fromIndex = fromColumn;
        toIndex = toColumn;
        drawSelect();
    }

    /**
     * 绘制选中区域
     */
    protected void drawSelect() {
        Image src = textProperty.getSrc();
        if (src == null) return;
        Image des = textProperty.getDes();
        if (des == null) return;
        int[] textPosInImg = textProperty.getTextPosInImg();
        int minIndex = Math.min(fromIndex, toIndex);
        int maxIndex = Math.max(fromIndex, toIndex);
        int startX = Math.max(textPosInImg[2 * minIndex], textProperty.getStartX());
        int startY = Math.max(textPosInImg[2 * minIndex + 1], textProperty.getStartY());
        int endX = Math.min(textPosInImg[2 * maxIndex], textProperty.getEndX());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        if (startX == endX) return;
        ImageRaster srcRaster = ImageRaster.create(src);
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA color1 = new ColorRGBA();
        ColorRGBA color2 = new ColorRGBA();
        textMarkForSelection = new int[(endY - startY) * (endX - startX) / 2];
        for (int y = startY, i = 0; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                srcRaster.getPixel(x, y, color1);
                desRaster.getPixel(x, y, color2);
                if (color1.equals(color2)) desRaster.setPixel(x, y, selectionBackgroundColor);
                else {
                    if (i == textMarkForSelection.length) {
                        int[] newArray = new int[textMarkForSelection.length * 2];
                        System.arraycopy(textMarkForSelection, 0, newArray, 0, textMarkForSelection.length);
                        textMarkForSelection = newArray;
                    }
                    textMarkForSelection[i++] = color2.asIntRGBA();
                    desRaster.setPixel(x, y, selectionTextColor);
                }
            }
        }
    }

    /**
     * 清除选中区域
     */
    protected void clearSelect() {
        if (textMarkForSelection == null) return;
        Image src = textProperty.getSrc();
        Image des = textProperty.getDes();
        int[] textPosInImg = textProperty.getTextPosInImg();
        if (src == null) return;
        if (des == null) return;
        int minIndex = Math.min(fromIndex, toIndex);
        int maxIndex = Math.max(fromIndex, toIndex);
        int startX = Math.max(textPosInImg[2 * minIndex], textProperty.getStartX());
        int startY = Math.max(textPosInImg[2 * minIndex + 1], textProperty.getStartY());
        int endX = Math.min(textPosInImg[2 * maxIndex], textProperty.getEndX());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        if (startX == endX) return;
        ImageRaster srcRaster = ImageRaster.create(src);
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA color = new ColorRGBA();
        for (int y = startY, i = 0; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                desRaster.getPixel(x, y, color);
                if (color.equals(selectionTextColor)) {
                    color.fromIntRGBA(textMarkForSelection[i++]);
                    desRaster.setPixel(x, y, color);
                } else {
                    srcRaster.getPixel(x, y, color);
                    desRaster.setPixel(x, y, color);
                }
            }
        }
        textMarkForSelection = null;
    }

    @Override
    protected void drawCursor() {
        Image src = textProperty.getSrc();
        if (src == null) return;
        Image des = textProperty.getDes();
        if (des == null) {
            textProperty.setText("");
            des = textProperty.getDes();
        }
        if (des == null) return;
        int[] textPosInImg = textProperty.getTextPosInImg();
        int x = textPosInImg[2 * toIndex];
        int startY = Math.max(textPosInImg[2 * toIndex + 1], textProperty.getStartY());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA color = new ColorRGBA();
        if (textMarkForCursor == null || textMarkForCursor.length < endY - startY)
            textMarkForCursor = new int[endY - startY];
        for (int y = startY, i = 0; y < endY; y++) {
            desRaster.getPixel(x, y, color);
            textMarkForCursor[i++] = color.asIntRGBA();
            desRaster.setPixel(x, y, cursorColor);
        }
        isCursorShowing = true;
    }

    @Override
    protected void clearCursor() {
        if (!isCursorShowing) return;
        Image src = textProperty.getSrc();
        if (src == null) return;
        Image des = textProperty.getDes();
        if (des == null) return;
        int[] textPosInImg = textProperty.getTextPosInImg();
        int x = textPosInImg[2 * toIndex];
        int startY = Math.max(textPosInImg[2 * toIndex + 1], textProperty.getStartY());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA color = new ColorRGBA();
        for (int y = startY, i = 0; y < endY; y++) {
            color.fromIntRGBA(textMarkForCursor[i++]);
            desRaster.setPixel(x, y, color);
        }
        isCursorShowing = false;
    }

}
