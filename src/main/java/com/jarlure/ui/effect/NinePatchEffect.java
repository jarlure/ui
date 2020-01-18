package com.jarlure.ui.effect;

import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import com.jme3.util.NativeObjectManager;

import java.nio.ByteBuffer;

public class NinePatchEffect {

    private Image src;
    private Image des;
    private boolean[] horizontalScale;
    private boolean[] verticalScale;
    private boolean[] horizontalFill;
    private boolean[] verticalFill;
    private int horizontalScalePixel;
    private int verticalScalePixel;
    private int fillTop, fillBottom, fillLeft, fillRight;

    private ImageProperty imageProperty;
    private TextProperty textProperty;

    public NinePatchEffect(Image src, boolean[][] edge, TextProperty textProperty) {
        this(src, edge, null, textProperty);
    }

    public NinePatchEffect(Image src, boolean[][] edge, ImageProperty imageProperty) {
        this(src, edge, imageProperty, null);
    }

    /**
     * 点九图效果。点九图是一种非比例拉伸图片技术，用于解决通常方法拉伸图片后图片变形失真问题。同时点九图还可以携带文本
     * 区域数据，决定文本在图片中的显示位置。
     *
     * @param src           去掉点九图拉伸像素数据和文本区域数据后的图片。直白些就是裁剪了黑边后的普通图片
     * @param edge          点九图拉伸像素数据和文本区域数据。可通过ImageHandler裁剪点九图得到
     * @param imageProperty 图片拉伸后会自动设置进这个属性中
     * @param textProperty  图片拉伸后新的文本区域数据和图片数据会自动设置进这个属性中
     */
    public NinePatchEffect(Image src, boolean[][] edge, ImageProperty imageProperty, TextProperty textProperty) {
        this.src = src;
        this.des = src;
        horizontalScale = edge[0];
        horizontalFill = edge[1];
        verticalScale = edge[2];
        verticalFill = edge[3];
        horizontalScalePixel = Helper.getNumberOfScalePixel(edge[0]);
        verticalScalePixel = Helper.getNumberOfScalePixel(edge[2]);
        this.fillTop = src.getHeight();
        this.fillBottom = 0;
        this.fillLeft = 0;
        this.fillRight = src.getWidth();

        this.imageProperty = imageProperty;
        this.textProperty = textProperty;
    }

    /**
     * 判断是否存在水平拉伸数据
     *
     * @return true如果存在水平拉伸数据；false如果不存在水平拉伸数据
     */
    public boolean existHorizontalScale() {
        return horizontalScale != null;
    }

    /**
     * 判断是否存在垂直拉伸数据
     *
     * @return true如果存在垂直拉伸数据；false如果不存在垂直拉伸数据
     */
    public boolean existVerticalScale() {
        return verticalScale != null;
    }

    /**
     * 判断是否存在水平文本区域数据
     *
     * @return true如果存在水平文本区域数据；false如果不存在水平文本区域数据
     */
    public boolean existHorizontalFill() {
        return horizontalFill != null;
    }

    /**
     * 判断是否存在垂直文本区域数据
     *
     * @return true如果存在垂直文本区域数据；false如果不存在垂直文本区域数据
     */
    public boolean existVerticalFill() {
        return verticalFill != null;
    }

    /**
     * 获得拉伸前的图片数据
     *
     * @return 拉伸前的图片数据
     */
    public Image getSrc() {
        return src;
    }

    /**
     * 设置拉伸前的图片数据。新的图片数据必须与旧的图片数据尺寸一致
     *
     * @param src 新的图片数据
     */
    public void setSrc(Image src) {
        if (this.src.getWidth() != src.getWidth()) return;
        if (this.src.getHeight() != src.getHeight()) return;
        this.src = src;
        if (des != null) setSize(des.getWidth(), des.getHeight());
    }

    /**
     * 获得拉伸后的图片数据
     *
     * @return 拉伸后的图片数据
     */
    public Image getDes() {
        return des;
    }

    /**
     * 设置拉伸尺寸。届时该方法会根据新的尺寸对拉伸前的图片数据进行拉伸，更新拉伸后的图片数据、新的文本区域数据
     *
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(float width, float height) {
        int roundWidth = Math.round(width);
        int roundHeight = Math.round(height);
        Image des = this.des;
        updateImageSize(roundWidth, roundHeight);
        updateFillPosition(roundWidth, roundHeight);
        if (imageProperty != null) imageProperty.setImage(this.des);
        if (textProperty != null) {
            textProperty.setSrc(this.des, fillLeft, fillBottom, fillRight, fillTop);
        }
        if (des != src && des != this.des) {
            des.dispose();
            if (!NativeObjectManager.UNSAFE) {
                for (ByteBuffer buf : des.getData()) {
                    BufferUtils.destroyDirectBuffer(buf);
                }
            }
        }
    }

    /**
     * 拉伸图片。如果新尺寸小于拉伸前的图片尺寸，则会按照拉伸前的图片尺寸拉伸。
     *
     * @param width  宽度
     * @param height 高度
     */
    private void updateImageSize(int width, int height) {
        float scaleX = 1;
        float scaleY = 1;
        if (horizontalScalePixel > 0) scaleX += (width - src.getWidth()) * 1f / horizontalScalePixel;
        if (verticalScalePixel > 0) scaleY += (height - src.getHeight()) * 1f / verticalScalePixel;
        if (scaleX < 1) scaleX = 1;
        if (scaleY < 1) scaleY = 1;
        if (scaleX == 1 && scaleY == 1) {
            if (des == null || des.getWidth() != width || des.getHeight() != height) des = ImageHandler.clone(src);
            return;
        }
        if (des == null || des.getWidth() != width || des.getHeight() != height) {
            des = ImageHandler.createEmptyImage(width, height);
        }
        ImageRaster srcRaster = ImageRaster.create(src);
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA color = new ColorRGBA();
        float repeatY = 0;
        for (int srcY = 0, desY = 0; srcY < src.getHeight(); srcY++) {
            if (verticalScale != null && verticalScale[srcY]) repeatY += scaleY;
            else repeatY += 1;
            while (repeatY >= 1) {
                repeatY -= 1;
                float repeatX = 0;
                for (int srcX = 0, desX = 0; srcX < src.getWidth(); srcX++) {
                    srcRaster.getPixel(srcX, srcY, color);
                    if (horizontalScale != null && horizontalScale[srcX]) repeatX += scaleX;
                    else repeatX += 1;
                    while (repeatX >= 1) {
                        repeatX -= 1;
                        desRaster.setPixel(desX, desY, color);
                        desX++;
                    }
                }
                desY++;
            }
        }
    }

    /**
     * 拉伸文本区域。
     *
     * @param width  宽度
     * @param height 高度
     */
    private void updateFillPosition(int width, int height) {
        if (existHorizontalFill()) {
            fillLeft = 0;
            {
                for (boolean filled : horizontalFill) {
                    if (filled) break;
                    fillLeft++;
                }
            }
            fillRight = Math.max(src.getWidth(), width);
            {
                for (int i = horizontalFill.length - 1; i >= 0; i--) {
                    if (horizontalFill[i]) break;
                    fillRight--;
                }
            }
        }
        if (existVerticalFill()) {
            fillTop = Math.max(src.getHeight(), height);
            {
                for (int i = verticalFill.length - 1; i >= 0; i--) {
                    if (verticalFill[i]) break;
                    fillTop--;
                }
            }
            fillBottom = 0;
            {
                for (boolean filled : verticalFill) {
                    if (filled) break;
                    fillBottom++;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "src=" + src +
                "\ndes=" + des +
                "\nhorizontalScale=" + horizontalScale +
                "\nverticalScale=" + verticalScale +
                "\nhorizontalFill=" + horizontalFill +
                "\nverticalFill=" + verticalFill +
                "\nhorizontalScalePixel=" + horizontalScalePixel +
                "\nverticalScalePixel=" + verticalScalePixel +
                "\nfillTop=" + fillTop +
                "\nfillBottom=" + fillBottom +
                "\nfillLeft=" + fillLeft +
                "\nfillRight=" + fillRight;
    }

    private final static class Helper {

        private static int getNumberOfScalePixel(boolean[] side) {
            if (side == null) return 0;
            int count = 0;
            for (boolean i : side) {
                if (i) count++;
            }
            return count;
        }

    }

}
