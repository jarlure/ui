package com.jarlure.ui.input;

public interface MouseInputListener {

    default void onLeftButtonPress(MouseEvent mouse) {
    }

    default void onLeftButtonPressLongTime(MouseEvent mouse) {
    }

    default void onRightButtonPress(MouseEvent mouse) {
    }

    default void onRightButtonPressLongTime(MouseEvent mouse) {
    }

    default void onLeftButtonClick(MouseEvent mouse) {
    }

    default void onRightButtonClick(MouseEvent mouse) {
    }

    default void onLeftButtonDoubleClick(MouseEvent mouse){
    }

    default void onRightButtonDoubleClick(MouseEvent mouse){
    }

    default void onLeftButtonDragging(MouseEvent mouse) {
    }

    default void onRightButtonDragging(MouseEvent mouse) {
    }

    default void onLeftButtonRelease(MouseEvent mouse) {
    }

    default void onRightButtonRelease(MouseEvent mouse) {
    }

    default void onMove(MouseEvent mouse) {
    }

    default void onWheelRolling(MouseEvent mouse) {
    }

    default void onStayLongTime(MouseEvent mouse) {
    }

    default void foldAnonymousInnerClassCode(MouseInputListener instance) {
    }

}
