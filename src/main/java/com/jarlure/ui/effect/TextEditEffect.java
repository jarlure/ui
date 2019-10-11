package com.jarlure.ui.effect;

public abstract class TextEditEffect implements AnimEffect {

    protected float timer;//光标闪烁间隔时间记录

    /**
     * 设置鼠标位置
     *
     * @param row    行索引值
     * @param column 列索引值
     */
    public abstract void setCursorPosition(int row, int column);

    /**
     * 选中文本
     *
     * @param fromRow    从第fromRow行
     * @param fromColumn 从第fromColumn列
     * @param toRow      到第toRow行
     * @param toColumn   到第toColumn列
     */
    public abstract void select(int fromRow, int fromColumn, int toRow, int toColumn);

    /**
     * 全选
     */
    public abstract void selectAll();

    /**
     * 绘制光标
     */
    protected abstract void drawCursor();

    /**
     * 清除光标
     */
    protected abstract void clearCursor();

    @Override
    public void update(float tpf) {
        if (timer == 0) {
            drawCursor();
        }
        timer += tpf;
        if (timer < 0.5f) {

        } else if (timer < 1f) {
            clearCursor();
            timer += 1f;
        } else if (2f < timer) {
            timer = 0;
        }
    }

    @Override
    public void finishImmediately() {
        clearCursor();
        timer = 0;
    }

    @Override
    @Deprecated
    public boolean isFinished() {
        return false;
    }

}
