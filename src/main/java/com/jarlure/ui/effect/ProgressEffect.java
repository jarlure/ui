package com.jarlure.ui.effect;

import com.jarlure.ui.property.ImageProperty;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.texture.Image;

import java.nio.ByteBuffer;

public class ProgressEffect {

    protected Image currentImg, emptyImg, fullImg;
    protected ImageProperty imageProperty;
    protected float percent;

    /**
     * 进度绘制效果。
     *
     * @param emptyProgressImg 进度为0%时的图片
     * @param fullProgressImg  进度为100%时的图片
     * @param imageProperty    设置进度百分比后新的图片会自动设置进这个属性中
     */
    public ProgressEffect(Image emptyProgressImg, Image fullProgressImg, ImageProperty imageProperty) {
        emptyImg = emptyProgressImg;
        fullImg = fullProgressImg;
        currentImg = ImageHandler.clone(emptyImg);
        this.imageProperty = imageProperty;
    }

    /**
     * 设置进度百分比。
     *
     * @param percent 新的进度百分比
     */
    public void setPercent(float percent) {
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;
        if (this.percent == percent) return;
        drawPercent(this.percent, percent);
        this.percent = percent;
        imageProperty.setImage(currentImg);
    }

    /**
     * 绘制进度。
     *
     * @param currentPercent 当前进度百分比
     * @param aimPercent     新进度百分比
     */
    protected void drawPercent(float currentPercent, float aimPercent) {
        int width = currentImg.getWidth();
        int height = currentImg.getHeight();
        ByteBuffer srcData;
        int left, right;
        if (currentPercent < aimPercent) {
            srcData = fullImg.getData(0);
            left = Math.round(width * currentPercent);
            right = Math.round(width * aimPercent);
        } else {
            srcData = emptyImg.getData(0);
            left = Math.round(width * aimPercent);
            right = Math.round(width * currentPercent);
        }
        ByteBuffer desData = currentImg.getData(0);
        byte[] store = new byte[4*(right-left)];
        int pos=4*left;
        int line=4*width;
        for (int y=0;y<height;y++){
            srcData.position(pos);
            desData.position(pos);
            srcData.get(store);
            desData.put(store);
            pos+=line;
        }
        currentImg.setUpdateNeeded();
    }

}
