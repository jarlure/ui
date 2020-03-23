package com.jarlure.ui.effect;

import com.jarlure.ui.property.FontProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

import java.nio.ByteBuffer;

public class TextLineEditEffect extends TextEditEffect {

    protected FontProperty fontProperty;
    protected TextProperty textProperty;
    protected int fromIndex, toIndex;
    protected byte[] cursorColor=new byte[4];
    protected boolean isCursorShowing;
    protected byte[] textMarkForCursor;
    protected byte[] selectionBackgroundColor=new byte[4];
    protected byte[] selectionTextColor=new byte[4];
    protected byte[] textMarkForSelection;

    /**
     * 单行文本编辑效果
     *
     * @param fontProperty 字体属性。用于提供字体名称、样式、颜色、尺寸等信息
     * @param textProperty 文本属性。用于提供文本区域等信息
     */
    public TextLineEditEffect(FontProperty fontProperty, TextProperty textProperty) {
        this.fontProperty = fontProperty;
        this.textProperty = textProperty;
        setCursorColor(ColorRGBA.Black);
        setSelectionBackgroundColor(new ColorRGBA(0f, 0.5f, 1f, 1f));
        setSelectionTextColor(ColorRGBA.White);
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
        this.cursorColor[0]=ImageHandler.toByte(cursorColor.r);
        this.cursorColor[1]=ImageHandler.toByte(cursorColor.g);
        this.cursorColor[2]=ImageHandler.toByte(cursorColor.b);
        this.cursorColor[3]=ImageHandler.toByte(cursorColor.a);
    }

    /**
     * 设置选中文本背景色。默认为天蓝色
     *
     * @param selectionBackgroundColor 选中文本背景色
     */
    public void setSelectionBackgroundColor(ColorRGBA selectionBackgroundColor) {
        this.selectionBackgroundColor[0]=ImageHandler.toByte(selectionBackgroundColor.r);
        this.selectionBackgroundColor[1]=ImageHandler.toByte(selectionBackgroundColor.g);
        this.selectionBackgroundColor[2]=ImageHandler.toByte(selectionBackgroundColor.b);
        this.selectionBackgroundColor[3]=ImageHandler.toByte(selectionBackgroundColor.a);
    }

    /**
     * 设置选中文本字体色。默认为白色
     *
     * @param selectionTextColor 选中文本字体色
     */
    public void setSelectionTextColor(ColorRGBA selectionTextColor) {
        this.selectionTextColor[0]=ImageHandler.toByte(selectionTextColor.r);
        this.selectionTextColor[1]=ImageHandler.toByte(selectionTextColor.g);
        this.selectionTextColor[2]=ImageHandler.toByte(selectionTextColor.b);
        this.selectionTextColor[3]=ImageHandler.toByte(selectionTextColor.a);
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
        ByteBuffer srcData = src.getData(0);
        ByteBuffer desData = des.getData(0);
        int growthLen = 8*(endX - startX)+4;
        textMarkForSelection = new byte[growthLen];
        int line = 4*src.getWidth();
        int pos = startY*line + 4*startX;
        byte[] srcStore=new byte[4*(endX-startX)];
        byte[] desStore=new byte[4*(endX-startX)];
        for (int y=startY,i=0;y<endY;y++){
            srcData.position(pos);
            desData.position(pos);
            srcData.get(srcStore);
            desData.get(desStore);
            for (int xi=0;xi<srcStore.length;xi+=4){
                if (srcStore[xi]==desStore[xi] && srcStore[xi+1]==desStore[xi+1] && srcStore[xi+2]==desStore[xi+2] && srcStore[xi+3]==desStore[xi+3]){
                    System.arraycopy(selectionBackgroundColor,0,desStore,xi,4);
                }else {
                    //存储被替换的颜色数据
                    if (i==textMarkForSelection.length){
                        byte[] array = new byte[textMarkForSelection.length+growthLen];
                        System.arraycopy(textMarkForSelection,0,array,0,textMarkForSelection.length);
                        textMarkForSelection=array;
                    }
                    System.arraycopy(desStore,xi,textMarkForSelection,i,4);
                    i+=4;
                    System.arraycopy(selectionTextColor,0,desStore,xi,4);
                }
            }
            desData.position(pos);
            desData.put(desStore);
            pos+=line;
        }
        des.setUpdateNeeded();
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
        ByteBuffer srcData = src.getData(0);
        ByteBuffer desData = des.getData(0);
        int line = 4*src.getWidth();
        int pos = startY*line + 4*startX;
        byte[] srcStore=new byte[4*(endX-startX)];
        byte[] desStore=new byte[4*(endX-startX)];
        for (int y=startY,i=0;y<endY;y++){
            srcData.position(pos);
            desData.position(pos);
            srcData.get(srcStore);
            desData.get(desStore);
            for (int xi=0;xi<srcStore.length;xi+=4){
                if (selectionTextColor[0]==desStore[xi] && selectionTextColor[1]==desStore[xi+1] && selectionTextColor[2]==desStore[xi+2] && selectionTextColor[3]==desStore[xi+3]){
                    System.arraycopy(textMarkForSelection,i,desStore,xi,4);
                    i+=4;
                }else {
                    System.arraycopy(srcStore,xi,desStore,xi,4);
                }
            }
            desData.position(pos);
            desData.put(desStore);
            pos+=line;
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
        int x = Math.min(textPosInImg[2 * toIndex], textProperty.getEndX()-1);
        int startY = Math.max(textPosInImg[2 * toIndex + 1], textProperty.getStartY());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        if (textMarkForCursor == null || textMarkForCursor.length < 4*(endY - startY)){
            textMarkForCursor = new byte[4*(endY-startY)];
        }
        ByteBuffer data = des.getData(0);
        int line = 4*des.getWidth();
        int pos = line*startY+4*x;
        byte[] store = new byte[4];
        for (int y=startY,i=0;y<endY;y++){
            data.position(pos);
            data.get(store);
            System.arraycopy(store,0,textMarkForCursor,i,4);
            i+=4;
            data.position(pos);
            data.put(cursorColor);
            pos+=line;
        }
        des.setUpdateNeeded();
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
        int x = Math.min(textPosInImg[2 * toIndex], textProperty.getEndX()-1);
        int startY = Math.max(textPosInImg[2 * toIndex + 1], textProperty.getStartY());
        int endY = Math.min(startY + (int) Math.ceil(1.1f * fontProperty.getSize()), textProperty.getEndY());
        ByteBuffer data = des.getData(0);
        int line = 4*des.getWidth();
        int pos = line*startY+4*x;
        for (int y=startY,i=0;y<endY;y++){
            data.position(pos);
            data.put(textMarkForCursor,i,4);
            i+=4;
            pos+=line;
        }
        des.setUpdateNeeded();
        isCursorShowing = false;
    }

}
