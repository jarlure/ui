package com.jarlure.ui.input;

import com.jme3.input.controls.InputListener;

public interface TouchInputListener extends InputListener {

    /**
     * 第n个触点的按下事件。当手指按压触摸屏时，触发该事件。
     * @param finger    第n个触点的触点事件（n小于等于5）
     */
    void onTouchPress(PointTouchEvent finger);

    /**
     * 第n个触点的拖拽事件。当手指在触摸屏上滑动时，多次触发该事件。
     * @param finger    第n个触点的触点事件（n小于等于5）
     */
    void onTouchDragging(PointTouchEvent finger);

    /**
     * 第n个触点的释放事件。当手指离开触摸屏时，触发该事件。
     * @param finger    第n个触点的触点事件（n小于等于5）
     */
    void onTouchRelease(PointTouchEvent finger);

    /**
     * 第n个触点的点击事件。当手指在0.15s内按下并离开触摸屏时，触发该事件。
     * @param finger    第n个触点的触点事件（n小于等于5）
     */
    void onTouchClick(PointTouchEvent finger);

    /**
     * 第n个触点的双击事件。当触发了点击事件后的触点在0.2s内再次触发点击事件后，触发该事件。
     * 注意：该事件在触发前，会触发总共两次onPointClick()
     * @param finger    第n个触点的触点事件（n小于等于5）
     */
    void onTouchDoubleClick(PointTouchEvent finger);

    /**
     * 短时间按压事件。该事件在onPointPress()之后马上触发，常用于模拟鼠标停留（显示控件提示）
     * @param finger    触点ID为0的触点事件。（换句话说，其他触点不会触发这个事件）
     */
    void onTouchPressShortTime(PointTouchEvent finger);

    /**
     * 长时间按压事件。该事件在onPointPress()触发后又持续按压0.5s后触发
     * @param finger    触点ID为0的触点事件。（换句话说，其他触点不会触发这个事件）
     */
    void onTouchPressLongTime(PointTouchEvent finger);

    /**
     * 双触点按压事件。当ID为0和ID为1的两个点，其中一点处于按压状态而另一点按下时，触发该事件
     * @param twoFingers    触点ID为0和触点ID为1的触点构成的事件
     */
    void onSecondTouchPress(LineTouchEvent twoFingers);

    /**
     * 双触点拖拽事件。当ID为0和ID为1的两个点中，其中一点发生移动时，触发该事件。该事件常用于缩放图片
     * @param twoFingers    触点ID为0和触点ID为1的触点构成的事件
     */
    void onSecondTouchDragging(LineTouchEvent twoFingers);

    /**
     * 双触点释放事件。当ID为0和ID为1的两个点中，其中一点被释放时，触发该事件
     * @param twoFingers    触点ID为0和触点ID为1的触点构成的事件
     */
    void onSecondTouchRelease(LineTouchEvent twoFingers);

}
