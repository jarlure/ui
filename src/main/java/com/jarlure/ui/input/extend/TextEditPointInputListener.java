package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.FocusProperty;
import com.jarlure.ui.converter.IndexConverter;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.TextEditEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.PointTouchEvent;
import com.jarlure.ui.input.TouchMouseAdapter;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.property.common.Property;
import com.jme3.input.event.InputEvent;

public abstract class TextEditPointInputListener extends TouchMouseAdapter {

    private UIComponent text;

    /**
     * 单行文本编辑器的鼠标输入监听器。目前该类有未处理的BUG：文本编辑输入监听器无法得知文本长度限制，因此鼠标位置索引可
     * 能会大于文本最大长度
     *
     * @param text 单行文本。该组件需要有TextEditEffect
     */
    public TextEditPointInputListener(UIComponent text) {
        this.text = text;
    }

    public abstract SelectConverter getSelectConverter();

    public abstract FocusProperty getFocusProperty();

    public abstract Property<Integer> getCursorPositionIndex();

    public abstract Property<Integer> getSelectFromIndex();

    public TextProperty getTextProperty() {
        return text.get(TextProperty.class);
    }

    public TextEditEffect getTextEditEffect() {
        return text.get(TextEditEffect.class);
    }

    public IndexConverter getIndexConverter() {
        if (!text.exist(IndexConverter.class)) {
            text.set(IndexConverter.class, new IndexConverter() {
                @Override
                public int getRowIndex(int cursorX, int cursorY) {
                    return 0;
                }

                @Override
                public int getIndex(int cursorX, int cursorY) {
                    return getColumnIndex(cursorX, cursorY);
                }

                @Override
                public int getColumnIndex(int cursorX, int cursorY) {
                    AABB box = text.get(AABB.class);
                    if (!box.contains(cursorX, cursorY)) return IndexConverter.EXCEPTION_NOT_FOUND;
                    int cursorLocalX = (int) (cursorX-box.getXLeft());//相对于图片左下角的位置坐标
                    int[] textPosInImg = text.get(TextProperty.class).getTextPosInImg();
                    if (textPosInImg == null || textPosInImg.length < 2) return IndexConverter.EXCEPTION_NOT_FOUND;
                    if (cursorLocalX < textPosInImg[0]) return IndexConverter.EXCEPTION_LESS_THAN_MIN;
                    if (textPosInImg[textPosInImg.length - 2] < cursorLocalX)
                        return IndexConverter.EXCEPTION_GRATER_THAN_MAX;
                    for (int i = textPosInImg.length - 2; i >= 2; i -= 2) {
                        if (cursorLocalX < textPosInImg[i - 2]) continue;
                        if (Math.abs(textPosInImg[i] - cursorLocalX) <= Math.abs(textPosInImg[i - 2] - cursorLocalX))
                            return i / 2;
                        else return i / 2 - 1;
                    }
                    return IndexConverter.EXCEPTION_NOT_FOUND;
                }

                @Override
                public int getCursorX(int index) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getCursorX(int rowIndex, int columnIndex) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getCursorY(int index) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getCursorY(int rowIndex, int columnIndex) {
                    throw new UnsupportedOperationException();
                }
            });
        }
        return text.get(IndexConverter.class);
    }

    @Override
    public void onPointPress(InputEvent point) {
        FocusProperty focusProperty = getFocusProperty();
        if (isTextSelected(point)){
            if (focusProperty.isFocus(text)){
                setCursorPosition(point);
            }else {
                focusProperty.setFocus(text);
                selectAll();
            }
        }else {
            if (focusProperty.isFocus(text)) {
                focusProperty.setFocus(null);
                getTextEditEffect().finishImmediately();
            }
        }
    }

    @Override
    public void onPointDragging(InputEvent point) {
        if (getFocusProperty().isFocus(text)){
            setSelectToIndex(point);
        }
    }

    protected boolean isTextSelected(InputEvent point) {
        if (!getSelectConverter().isSelect(text, point)) return false;
        TextProperty textProperty = text.get(TextProperty.class);
        if (textProperty.getSrc() == null) return false;
        AABB box = text.get(AABB.class);
        int x = Math.round(getX(point) - box.getXLeft());
        if (x < textProperty.getStartX()) return false;
        if (x > textProperty.getEndX()) return false;
        int y = Math.round(getY(point) - box.getYBottom());
        if (y < textProperty.getStartY()) return false;
        if (y > textProperty.getEndY()) return false;

        return true;
    }

    @Override
    public void onLeftButtonDoubleClick(MouseEvent mouse) {
        if (getFocusProperty().isFocus(text)) {
            selectAll();
        }
    }

    protected void setCursorPosition(InputEvent point){
        if (point instanceof MouseEvent) setCursorPosition(((MouseEvent) point).x,((MouseEvent) point).y);
        else setCursorPosition((int)((PointTouchEvent) point).x,(int)((PointTouchEvent) point).y);
    }

    protected void setCursorPosition(int x, int y) {
        int index = getIndexConverter().getIndex(x, y);
        if (index == IndexConverter.EXCEPTION_NOT_FOUND) return;
        if (index == IndexConverter.EXCEPTION_LESS_THAN_MIN) index = 0;
        int length = getTextProperty().getText().length();
        if (index == IndexConverter.EXCEPTION_GRATER_THAN_MAX) index = length;
        getCursorPositionIndex().setValue(index);
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

    protected void setSelectToIndex(InputEvent point){
        if (point instanceof MouseEvent) setSelectToIndex(((MouseEvent) point).x,((MouseEvent) point).y);
        else setSelectToIndex((int)((PointTouchEvent) point).x,(int)((PointTouchEvent) point).y);
    }

    protected void setSelectToIndex(int x, int y) {
        int index = getIndexConverter().getIndex(x, y);
        if (index < 0) return;
        int length = getTextProperty().getText().length();
        if (index > length) return;
        int currentIndex = getCursorPositionIndex().getValue();
        if (index == currentIndex) return;
        getCursorPositionIndex().setValue(index);
        int fromIndex = getSelectFromIndex().getValue();
        getTextEditEffect().select(0, fromIndex, 0, index);
    }

    protected void selectAll() {
        String text = getTextProperty().getText();
        int toIndex = text == null ? 0 : text.length();
        getTextEditEffect().select(0, 0, 0, toIndex);
        getCursorPositionIndex().setValue(toIndex);
        getSelectFromIndex().setValue(0);
    }

}
