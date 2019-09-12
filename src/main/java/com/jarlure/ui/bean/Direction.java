package com.jarlure.ui.bean;

import com.jme3.input.KeyInput;

public enum Direction {

    NULL(0, 0, 0),
    DOWN_LEFT(1, -1, -1), DOWN(2, 0, -1), DOWN_RIGHT(3, 1, -1),
    LEFT(4, -1, 0), CENTER(5, 0, 0), RIGHT(6, 1, 0),
    UP_LEFT(7, -1, 1), UP(8, 0, 1), UP_RIGHT(9, 1, 1);

    private int index;
    private int x, y;

    /**
     * 通过索引值获取方向
     *
     * @param index 方向的索引值。NULL方向为0，其余方向对应数字键盘上的1-9。例如：右方对应6；左下方对应1；中心对应5
     * @return 该索引值对应的方向。未找到对应方向时返回NULL
     */
    public static Direction fromIndex(int index) {
        switch (index) {
            case 1:
                return DOWN_LEFT;
            case 2:
                return DOWN;
            case 3:
                return DOWN_RIGHT;
            case 4:
                return LEFT;
            case 5:
                return CENTER;
            case 6:
                return RIGHT;
            case 7:
                return UP_LEFT;
            case 8:
                return UP;
            case 9:
                return UP_RIGHT;
            default:
                return NULL;
        }
    }

    /**
     * 通过键盘键值获取方向
     *
     * @param keyCode 数字小键盘的键值
     * @return 该键值对应的方向。未找到对应方向时返回NULL
     */
    public static Direction fromKeyCode(int keyCode) {
        switch (keyCode) {
            case KeyInput.KEY_NUMPAD1:
                return DOWN_LEFT;
            case KeyInput.KEY_NUMPAD2:
                return DOWN;
            case KeyInput.KEY_NUMPAD3:
                return DOWN_RIGHT;
            case KeyInput.KEY_NUMPAD4:
                return LEFT;
            case KeyInput.KEY_NUMPAD5:
                return CENTER;
            case KeyInput.KEY_NUMPAD6:
                return RIGHT;
            case KeyInput.KEY_NUMPAD7:
                return UP_LEFT;
            case KeyInput.KEY_NUMPAD8:
                return UP;
            case KeyInput.KEY_NUMPAD9:
                return UP_RIGHT;
            default:
                return NULL;
        }
    }

    /**
     * 2D平面上的八个方向
     *
     * @param index 方向的索引值。NULL方向为0，其余方向对应数字键盘上的1-9。例如：右方对应6；左下方对应1；中心对应5
     * @param x     方向的x坐标。以CENTER方向的坐标为原点，在CENTER左边的记为-1，右边的记为1，其余记为0
     * @param y     方向的y坐标。以CENTER方向的坐标为原点，在CENTER下边的记为-1，上边的记为1，其余记为0
     */
    Direction(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    /**
     * 获得该方向的索引值。NULL方向为0，其余方向对应数字键盘上的1-9。例如：右方对应6；左下方对应1；中心对应5
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获得该方向的x坐标。以CENTER方向的坐标为原点，在CENTER左边的记为-1，右边的记为1，其余记为0
     *
     * @return 该方向的x坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 获得该方向的y坐标。以CENTER方向的坐标为原点，在CENTER下边的记为-1，上边的记为1，其余记为0
     *
     * @return 该方向的y坐标
     */
    public int getY() {
        return y;
    }

    /**
     * 获得该方向的相反方向。例如this为左下方（DOWN_LEFT）则返回右上方（UP_RIGHT）
     *
     * @return 该方向的相反方向
     */
    public Direction inverse() {
        switch (this) {
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN:
                return UP;
            case DOWN_RIGHT:
                return UP_LEFT;
            case LEFT:
                return RIGHT;
            case CENTER:
                return CENTER;
            case RIGHT:
                return LEFT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP:
                return DOWN;
            case UP_RIGHT:
                return DOWN_LEFT;
            default:
                return NULL;
        }
    }

    /**
     * 从给定序列中找到当前方向，并以当前方向为起始重新排列这个序列。例如：你打算以逆时针方向进行遍历四个方向，并且想从
     * this方向开始遍历，那么你就应该调用this.sort(8,4,2,6)。虽然这看上去有点匪夷所思，但它十分好用。
     *
     * @param indexRecycleArray 给定的方向索引序列
     * @return  以this方向索引作为开始的方向索引序列。新数组
     */
    public int[] sort(int... indexRecycleArray) {
        int indexInArray = -1;
        for (int i = 0; i < indexRecycleArray.length; i++) {
            if (index == indexRecycleArray[i]) {
                indexInArray = i;
                break;
            }
        }
        if (indexInArray == -1) return null;
        int[] result = new int[indexRecycleArray.length];
        System.arraycopy(indexRecycleArray, indexInArray, result, 0, indexRecycleArray.length - indexInArray);
        System.arraycopy(indexRecycleArray, 0, result, indexRecycleArray.length - indexInArray, indexInArray);
        return result;
    }

}
