package com.jarlure.ui.effect;

public interface AnimEffect {

    /**
     * 更新动画
     *
     * @param tpf 每帧时长
     */
    void update(float tpf);

    /**
     * 立即播放完毕动画
     */
    void finishImmediately();

    /**
     * 判断动画是否已经播放完毕
     * @return  动画是否已经播放完毕。如果是则返回true；否则返回false
     */
    boolean isFinished();

}
