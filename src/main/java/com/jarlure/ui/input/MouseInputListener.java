package com.jarlure.ui.input;

import com.jme3.input.controls.InputListener;

public interface MouseInputListener extends InputListener {

    void onLeftButtonPress(MouseEvent mouse);

    void onLeftButtonPressLongTime(MouseEvent mouse);

    void onRightButtonPress(MouseEvent mouse);

    void onRightButtonPressLongTime(MouseEvent mouse);

    void onLeftButtonClick(MouseEvent mouse);

    void onRightButtonClick(MouseEvent mouse);

    void onLeftButtonDoubleClick(MouseEvent mouse);

    void onRightButtonDoubleClick(MouseEvent mouse);

    void onLeftButtonDragging(MouseEvent mouse);

    void onRightButtonDragging(MouseEvent mouse);

    void onLeftButtonRelease(MouseEvent mouse);

    void onRightButtonRelease(MouseEvent mouse);

    void onMove(MouseEvent mouse);

    void onWheelRolling(MouseEvent mouse);

    void onStayLongTime(MouseEvent mouse);

}
