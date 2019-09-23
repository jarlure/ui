package com.jarlure.ui.effect;

/**
 * 拖拽移动效果
 */
public interface DragEffect extends AnimEffect {

    /**
     * 开始拖拽。对应鼠标按下按键事件
     *
     * @param x 拖拽开始时鼠标的水平坐标x值
     * @param y 拖拽开始时鼠标的垂直坐标y值
     */
    void start(int x, int y);

    /**
     * 更新拖拽。对应鼠标拖拽事件
     *
     * @param x 拖拽过程中鼠标的水平坐标x值
     * @param y 拖拽过程中鼠标的垂直坐标y值
     */
    void update(int x, int y);

    /**
     * 结束拖拽。对应鼠标释放按键事件
     */
    void end();

}
