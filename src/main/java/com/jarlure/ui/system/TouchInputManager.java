package com.jarlure.ui.system;

import com.jarlure.ui.input.LineTouchEvent;
import com.jarlure.ui.input.PointTouchEvent;
import com.jarlure.ui.input.TouchInputListener;
import com.jme3.input.event.TouchEvent;
import com.jme3.util.SafeArrayList;

public final class TouchInputManager {

    private static final int NULL = -1;
    private final int MAX_POINTER_NUM = 5;//可支持的最多触点数量
    protected int pointerNum = 0;//当前触点数量
    private float[] pressX = new float[]{NULL,NULL,NULL,NULL,NULL};
    private float[] pressY = new float[]{NULL,NULL,NULL,NULL,NULL};
    private long[] timeWhenPress = new long[]{NULL,NULL,NULL,NULL,NULL};
    private long[] timeWhenClick = new long[]{NULL,NULL,NULL,NULL,NULL};
    private float p0_x, p0_y, p1_x, p1_y;
    private SafeArrayList<TouchInputListener> queue=new SafeArrayList<>(TouchInputListener.class);

    public void add(TouchInputListener listener){
        if (queue == null) return;
        queue.add(0, listener);
    }

    public void remove(TouchInputListener listener) {
        if (queue == null) return;
        queue.remove(listener);
    }

    public void onTouchEvent(TouchEvent evt) {
        int id = evt.getPointerId();
        if (id>=MAX_POINTER_NUM) return;

        //记录数据
        if (evt.getType()== TouchEvent.Type.DOWN){
            if (pressX[id]==NULL) pointerNum++;
            pressX[id]=evt.getX();
            pressY[id]=evt.getY();
            timeWhenPress[id]=evt.getTime();
        }
        switch (evt.getType()){
            case DOWN:
            case MOVE:
            case UP:
                switch (id) {
                    case 0:
                        p0_x = evt.getX();
                        p0_y = evt.getY();
                        break;
                    case 1:
                        p1_x = evt.getX();
                        p1_y = evt.getY();
                        break;
                }
                break;
        }
        //触点事件
        PointTouchEvent pointEvent;
        switch (evt.getType()){
            case DOWN:
                pointEvent=new PointTouchEvent(id,pressX[id],pressY[id],evt.getPressure(),pointerNum);
                pointEvent.setTime(evt.getTime());
                for (TouchInputListener listener:queue){
                    listener.onTouchPress(pointEvent);
                }
                break;
            case MOVE:
                pointEvent=new PointTouchEvent(id,pressX[id],pressY[id],evt.getX(),evt.getY(),evt.getDeltaX(),evt.getDeltaY(),evt.getPressure(),pointerNum);
                pointEvent.setTime(evt.getTime());
                for (TouchInputListener listener:queue){
                    listener.onTouchDragging(pointEvent);
                }
                break;
            case UP:
                long dt = evt.getTime() - timeWhenPress[id];
                boolean isClicked = dt < 150;
                boolean isDoubleClicked = false;
                if (isClicked) {
                    if (timeWhenClick[id] == NULL) timeWhenClick[id] = evt.getTime();
                    else {
                        dt = evt.getTime() - timeWhenClick[id];
                        if (dt < 200) {
                            isDoubleClicked = true;
                            timeWhenClick[id] = NULL;
                        } else {
                            timeWhenClick[id] = evt.getTime();
                        }
                    }
                } else {
                    timeWhenClick[id] = NULL;
                }
                pointEvent=new PointTouchEvent(id,pressX[id],pressY[id],evt.getX(),evt.getY(),evt.getPressure(),pointerNum);
                pointEvent.setTime(evt.getTime());
                for (TouchInputListener listener:queue){
                    listener.onTouchRelease(pointEvent);
                }
                if (isClicked){
                    pointEvent.resetConsumed();
                    for (TouchInputListener listener : queue.getArray()) {
                        listener.onTouchClick(pointEvent);
                    }
                }
                if (isDoubleClicked){
                    pointEvent.resetConsumed();
                    for (TouchInputListener listener : queue.getArray()) {
                        listener.onTouchDoubleClick(pointEvent);
                    }
                }
                break;
        }
        //单触点事件
        if (id==0){
            switch (evt.getType()){
                case SHOWPRESS:
                    pointEvent=new PointTouchEvent(id,pressX[id],pressY[id],evt.getPressure(),pointerNum);
                    pointEvent.setTime(evt.getTime());
                    for (TouchInputListener listener:queue){
                        listener.onTouchPressShortTime(pointEvent);
                    }
                    break;
                case LONGPRESSED:
                    pointEvent=new PointTouchEvent(id,pressX[id],pressX[id],evt.getPressure(),pointerNum);
                    pointEvent.setTime(evt.getTime());
                    for (TouchInputListener listener:queue){
                        listener.onTouchPressLongTime(pointEvent);
                    }
                    break;
            }
        }
        //双触点事件
        if (id < 2 && pressX[0] != NULL && pressX[1] != NULL) {
            LineTouchEvent lineTouchEvent;
            switch (evt.getType()){
                case DOWN:
                    lineTouchEvent =new LineTouchEvent(id,pressX[0],pressY[0],p0_x,p0_y,pressX[1],pressY[1],p1_x,p1_y,pointerNum);
                    lineTouchEvent.setTime(evt.getTime());
                    for (TouchInputListener listener : queue){
                        listener.onSecondTouchPress(lineTouchEvent);
                    }
                    break;
                case MOVE:
                    lineTouchEvent =new LineTouchEvent(id,pressX[0],pressY[0],p0_x,p0_y,pressX[1],pressY[1],p1_x,p1_y,evt.getDeltaX(),evt.getDeltaY(),pointerNum);
                    lineTouchEvent.setTime(evt.getTime());
                    for (TouchInputListener listener : queue){
                        listener.onSecondTouchDragging(lineTouchEvent);
                    }
                    break;
                case UP:
                    lineTouchEvent =new LineTouchEvent(id,pressX[0],pressY[0],p0_x,p0_y,pressX[1],pressY[1],p1_x,p1_y,pointerNum);
                    lineTouchEvent.setTime(evt.getTime());
                    for (TouchInputListener listener : queue){
                        listener.onSecondTouchRelease(lineTouchEvent);
                    }
                    break;
            }
        }
        //清除数据
        if (evt.getType()== TouchEvent.Type.UP){
            if (pressX[id]!=NULL) pointerNum--;
            pressX[id]=NULL;
            pressY[id]=NULL;
            timeWhenPress[id] = NULL;
        }
    }

}
