package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.IndexConverter;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.TextEditEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputListener;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.FocusProperty;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.property.common.Property;

public abstract class TextEditMouseInputListener implements MouseInputListener {

    private UIComponent text;

    /**
     * 单行文本编辑器的鼠标输入监听器。目前该类有未处理的BUG：文本编辑输入监听器无法得知文本长度限制，因此鼠标位置索引可
     *                                                        能会大于文本最大长度
     *
     * @param text 单行文本。该组件需要有TextEditEffect
     */
    public TextEditMouseInputListener(UIComponent text){
        this.text=text;
    }

    public abstract SelectConverter getSelectConverter();

    public abstract Property<Integer> getCursorPositionIndex() ;

    public abstract Property<Integer> getSelectFromIndex() ;

    public FocusProperty getFocusProperty(){
        return text.get(FocusProperty.class);
    }

    public TextProperty getTextProperty(){
        return text.get(TextProperty.class);
    }

    public TextEditEffect getTextEditEffect(){
        return text.get(TextEditEffect.class);
    }

    public IndexConverter getIndexConverter(){
        if (!text.exist(IndexConverter.class)){
            text.set(IndexConverter.class, new IndexConverter() {
                @Override
                public int getColumnIndex(int cursorX, int cursorY) {
                    AABB box = text.get(AABB.class);
                    cursorX-=box.getXLeft();
                    if (cursorX<0)return IndexConverter.EXCEPTION_NOT_FOUND;
                    cursorY-=box.getYBottom();
                    if (cursorY<0)return IndexConverter.EXCEPTION_NOT_FOUND;
                    int[] textPosInImg = text.get(TextProperty.class).getTextPosInImg();
                    if (textPosInImg==null)return IndexConverter.EXCEPTION_NOT_FOUND;
                    if (textPosInImg[textPosInImg.length-2]<cursorX) return IndexConverter.EXCEPTION_GRATER_THAN_MAX;
                    if (textPosInImg[0]>cursorX) return IndexConverter.EXCEPTION_LESS_THAN_MIN;
                    for (int i=textPosInImg.length-2;i>0;i-=2){
                        if (textPosInImg[i-2]>cursorX)continue;
                        if (textPosInImg[i]<cursorX)continue;
                        if (Math.abs(textPosInImg[i]-cursorX)<=Math.abs(textPosInImg[i-2]-cursorX))return i/2;
                        else return i/2-1;
                    }
                    return IndexConverter.EXCEPTION_NOT_FOUND;
                }

                @Override
                public int getCursorX(int rowIndex, int columnIndex) {
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
    public void onLeftButtonPress(MouseEvent mouse) {
        FocusProperty focusProperty=getFocusProperty();
        if (!isTextSelected(mouse)) {
            if (focusProperty.isFocus()) {
                getTextEditEffect().finishImmediately();
                focusProperty.setFocus(false);
            }
            return;
        }

        if (focusProperty.isFocus()){
            setCursorPosition(mouse.x,mouse.y);
        }else{
            selectAll();
            focusProperty.setFocus(true);
        }
    }

    @Override
    public void onLeftButtonDragging(MouseEvent mouse) {
        if (!getFocusProperty().isFocus())return;
        setSelectToIndex(mouse.x,mouse.y);
    }

    protected boolean isTextSelected(MouseEvent mouse){
        if (!getSelectConverter().isSelect(text,mouse))return false;
        TextProperty textProperty = text.get(TextProperty.class);
        if (textProperty.getSrc()==null)return false;
        AABB box = text.get(AABB.class);
        int x = Math.round(mouse.x-box.getXLeft());
        if (x<textProperty.getStartX())return false;
        if (x>textProperty.getEndX())return false;
        int y = Math.round(mouse.y-box.getYBottom());
        if (y<textProperty.getStartY())return false;
        if (y>textProperty.getEndY())return false;

        return true;
    }

    @Override
    public void onLeftButtonDoubleClick(MouseEvent mouse) {
        if (getFocusProperty().isFocus()){
            selectAll();
        }
    }

    protected void setCursorPosition(int x,int y){
        int index = getIndexConverter().getIndex(x,y);
        if (index==IndexConverter.EXCEPTION_NOT_FOUND)return;
        if (index==IndexConverter.EXCEPTION_LESS_THAN_MIN) index=0;
        int length = getTextProperty().getText().length();
        if (index==IndexConverter.EXCEPTION_GRATER_THAN_MAX) index=length;
        getCursorPositionIndex().setValue(index);
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

    protected void setSelectToIndex(int x,int y){
        int index = getIndexConverter().getIndex(x,y);
        if (index<0) return;
        int length = getTextProperty().getText().length();
        if (index>length) return;
        int currentIndex = getCursorPositionIndex().getValue();
        if (index==currentIndex)return;
        getCursorPositionIndex().setValue(index);
        int fromIndex = getSelectFromIndex().getValue();
        getTextEditEffect().select(0,fromIndex,0,index);
    }

    protected void selectAll(){
        String text=getTextProperty().getText();
        int toIndex = text==null? 0:text.length();
        getTextEditEffect().select(0,0,0,toIndex);
        getCursorPositionIndex().setValue(toIndex);
        getSelectFromIndex().setValue(0);
    }

}
