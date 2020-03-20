package com.jarlure.ui.input;

import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.OBB;
import com.jme3.input.event.InputEvent;

public class TouchMouseAdapter implements TouchInputListener,MouseInputListener {

    public void onPointPress(InputEvent point){
    }

    public void onPointDragging(InputEvent point){
    }

    public void onPointRelease(InputEvent point){
    }

    public void onPointClick(InputEvent point){
    }

    public void onPointDoubleClick(InputEvent point){
    }

    public void onPointPressLongTime(InputEvent point){
    }

    public float getX(InputEvent point){
        if (point instanceof MouseEvent){
            return ((MouseEvent) point).x;
        }else {
            return ((PointTouchEvent) point).x;
        }
    }

    public float getY(InputEvent point){
        if (point instanceof MouseEvent){
            return ((MouseEvent) point).y;
        }else {
            return ((PointTouchEvent) point).y;
        }
    }

    public float getPressX(InputEvent point){
        if (point instanceof MouseEvent){
            return ((MouseEvent) point).getPressX();
        }else {
            return ((PointTouchEvent) point).getPressX();
        }
    }

    public float getPressY(InputEvent point){
        if (point instanceof MouseEvent){
            return ((MouseEvent) point).getPressY();
        }else {
            return ((PointTouchEvent) point).getPressY();
        }
    }

    public boolean contains(AABB box,InputEvent point){
        if (point instanceof MouseEvent) return box.contains(((MouseEvent) point).x,((MouseEvent) point).y);
        else return box.contains(((PointTouchEvent) point).x,((PointTouchEvent) point).y);
    }

    public boolean contains(OBB box,InputEvent point){
        if (point instanceof MouseEvent) return box.contains(((MouseEvent) point).x,((MouseEvent) point).y);
        else return box.contains(((PointTouchEvent) point).x,((PointTouchEvent) point).y);
    }

    @Override
    public void onLeftButtonPress(MouseEvent mouse) {
        onPointPress(mouse);
    }

    @Override
    public void onLeftButtonPressLongTime(MouseEvent mouse) {
        onPointPressLongTime(mouse);
    }

    @Override
    public void onRightButtonPress(MouseEvent mouse) {
        onPointPress(mouse);
    }

    @Override
    public void onRightButtonPressLongTime(MouseEvent mouse) {
        onPointPressLongTime(mouse);
    }

    @Override
    public void onLeftButtonClick(MouseEvent mouse) {
        onPointClick(mouse);
    }

    @Override
    public void onRightButtonClick(MouseEvent mouse) {
        onPointClick(mouse);
    }

    @Override
    public void onLeftButtonDoubleClick(MouseEvent mouse) {
        onPointDoubleClick(mouse);
    }

    @Override
    public void onRightButtonDoubleClick(MouseEvent mouse) {
        onPointDoubleClick(mouse);
    }

    @Override
    public void onLeftButtonDragging(MouseEvent mouse) {
        onPointDragging(mouse);
    }

    @Override
    public void onRightButtonDragging(MouseEvent mouse) {
        onPointDragging(mouse);
    }

    @Override
    public void onLeftButtonRelease(MouseEvent mouse) {
        onPointRelease(mouse);
    }

    @Override
    public void onRightButtonRelease(MouseEvent mouse) {
        onPointRelease(mouse);
    }

    @Override
    public void onMove(MouseEvent mouse) {

    }

    @Override
    public void onWheelRolling(MouseEvent mouse) {

    }

    @Override
    public void onStayLongTime(MouseEvent mouse) {

    }

    @Override
    public void onTouchPress(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointPress(finger);
    }

    @Override
    public void onTouchDragging(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointDragging(finger);
    }

    @Override
    public void onTouchRelease(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointRelease(finger);
    }

    @Override
    public void onTouchClick(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointClick(finger);
    }

    @Override
    public void onTouchDoubleClick(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointDoubleClick(finger);
    }

    @Override
    public void onTouchPressShortTime(PointTouchEvent finger) {

    }

    @Override
    public void onTouchPressLongTime(PointTouchEvent finger) {
        if (finger.getNumberOfPointer()>1)return;
        onPointPressLongTime(finger);
    }

    @Override
    public void onSecondTouchPress(LineTouchEvent twoFingers) {

    }

    @Override
    public void onSecondTouchDragging(LineTouchEvent twoFingers) {

    }

    @Override
    public void onSecondTouchRelease(LineTouchEvent twoFingers) {

    }

    protected void foldAnonymousInnerClassCode(TouchMouseAdapter instance) {
    }

}
