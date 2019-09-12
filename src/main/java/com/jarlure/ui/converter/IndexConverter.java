package com.jarlure.ui.converter;

public interface IndexConverter {

    public static final int EXCEPTION_NOT_FOUND = -1;//未找到对应索引值
    public static final int EXCEPTION_LESS_THAN_MIN = -2;//计算得到的索引值小于最小索引值
    public static final int EXCEPTION_GRATER_THAN_MAX = -3;//计算得到的索引值大于最大索引值

    /**
     * 将鼠标坐标转换为行索引值（方向从上到下）。当索引转换器是2D索引转换器时重写此方法
     * @param cursorX   鼠标水平坐标x值
     * @param cursorY   鼠标垂直坐标y值
     * @return  行索引值
     */
    default int getRowIndex(int cursorX,int cursorY) {
        return 0;
    }

    /**
     * 将鼠标坐标转换为索引值。
     * @param cursorX   鼠标水平坐标x值
     * @param cursorY   鼠标垂直坐标y值
     * @return  索引值
     */
    default int getIndex(int cursorX,int cursorY){
        return getColumnIndex(cursorX,cursorY);
    }

    /**
     * 将鼠标坐标转换为列索引值（方向从左到右）。相当于getIndex()。用于兼容2D索引转换器
     * @param cursorX   鼠标水平坐标x值
     * @param cursorY   鼠标垂直坐标y值
     * @return  列索引值
     */
    int getColumnIndex(int cursorX,int cursorY) ;

    /**
     * 将索引值转换为鼠标水平坐标x值
     * @param index 索引值
     * @return  鼠标水平坐标x值
     */
    default int getCursorX(int index){
        return getCursorX(0,index);
    }

    /**
     * 将索引值转换为鼠标水平坐标x值
     * @param rowIndex  行索引值
     * @param columnIndex   列索引值
     * @return  鼠标水平坐标x值
     */
    int getCursorX(int rowIndex,int columnIndex);

    /**
     * 将索引值转换为鼠标垂直坐标y值
     * @param index 索引值
     * @return  鼠标垂直坐标y值
     */
    default int getCursorY(int index){
        return getCursorY(0,index);
    }

    /**
     * 将索引值转换为鼠标垂直坐标y值
     * @param rowIndex  行索引值
     * @param columnIndex   列索引值
     * @return  鼠标垂直坐标y值
     */
    int getCursorY(int rowIndex,int columnIndex);

}