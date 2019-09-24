package com.jarlure.ui.effect;

import com.jarlure.ui.property.ImageProperty;
import com.jme3.texture.Image;
import com.jme3.util.IntMap;

public class SwitchEffect {

    private IntMap<Image> imgMap;
    private int currentIndex;
    private ImageProperty imageProperty;

    /**
     * 切换图片效果。常用于各种按钮的状态图片切换
     *
     * @param imageProperty 切换后的图片会自动设置进这个属性中
     */
    public SwitchEffect(ImageProperty imageProperty) {
        this.imgMap = new IntMap<>(4,0.8f);
        this.imageProperty = imageProperty;
    }

    /**
     * 添加一张用于切换的备用图片
     *
     * @param index 用于寻找该图片的索引值
     * @param img   图片
     */
    public void addImage(int index, Image img) {
        imgMap.put(index, img);
    }

    /**
     * 移除给定索引值对应的图片
     *
     * @param index 添加图片时设置的索引值
     * @return 移除的图片。如果未找到则返回null
     */
    public Image removeImage(int index) {
        return imgMap.remove(index);
    }

    /**
     * 获得当前图片对应的索引值
     *
     * @return 当前图片对应的索引值
     */
    public int getIndexOfCurrentImage() {
        return currentIndex;
    }

    /**
     * 切换到给定索引值对应的图片
     *
     * @param index 图片对应的索引值
     * @return 是否存在该索引值对应的图片
     */
    public boolean switchTo(int index) {
        if (!imgMap.containsKey(index)) return false;
        Image img = imgMap.get(index);
        currentIndex = index;
        imageProperty.setImage(img);
        return true;
    }

    /**
     * 切换到下一张图片
     */
    public void switchToNext() {
        int nextIndex = (currentIndex + 1) % imgMap.size();
        switchTo(nextIndex);
    }

}