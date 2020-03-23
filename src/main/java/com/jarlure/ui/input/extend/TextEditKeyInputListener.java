package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.FocusProperty;
import com.jarlure.ui.effect.TextEditEffect;
import com.jarlure.ui.input.KeyEvent;
import com.jarlure.ui.input.KeyInputAdapter;
import com.jarlure.ui.property.TextProperty;
import com.jarlure.ui.property.common.Property;
import com.jarlure.ui.util.ClipboardEditor;
import com.jme3.input.KeyInput;

public abstract class TextEditKeyInputListener extends KeyInputAdapter {

    private UIComponent text;

    /**
     * 单行文本编辑器的键盘输入监听器。
     *
     * @param text 单行文本。该组件需要有TextEditEffect
     */
    public TextEditKeyInputListener(UIComponent text) {
        this.text = text;
    }

    protected abstract FocusProperty getFocusConverter();

    protected abstract Property<Integer> getCursorPositionIndex();

    protected abstract Property<Integer> getSelectFromIndex();

    protected TextProperty getTextProperty() {
        return text.get(TextProperty.class);
    }

    protected TextEditEffect getTextEditEffect() {
        return text.get(TextEditEffect.class);
    }

    @Override
    public void onKeyPressed(KeyEvent key) {
        if (!getFocusConverter().isFocus(text)) return;
        switch (key.getCode()) {
            case KeyInput.KEY_LEFT:
                if (key.isCtrlPressedOnly()) leftMoveSelect();
                else leftMoveCursor();
                return;
            case KeyInput.KEY_RIGHT:
                if (key.isCtrlPressedOnly()) rightMoveSelect();
                else rightMoveCursor();
                return;
            case KeyInput.KEY_BACK:
                deleteSelectOrCursorLeft();
                return;
            case KeyInput.KEY_DELETE:
                deleteSelect();
                return;
            case KeyInput.KEY_RETURN:
                getTextEditEffect().finishImmediately();
                getFocusConverter().setFocus(null);
                return;
            case KeyInput.KEY_A:
                if (key.isCtrlPressedOnly()){
                    selectAll();
                    return;
                }
                break;
            case KeyInput.KEY_C:
                if (key.isCtrlPressedOnly()) {
                    copy();
                    return;
                }
                break;
            case KeyInput.KEY_V:
                if (key.isCtrlPressedOnly()) {
                    paste();
                    return;
                }
                break;
        }
        if (key.getValue() == 0) return;
        insert(key.getValue());
    }

    @Override
    public void onKeyPressing(KeyEvent key) {
        onKeyPressed(key);
    }

    @Override
    public void onKeyReleased(KeyEvent key) {
        if (!getFocusConverter().isFocus(text)) return;
        //输入法输入值
        if (key.getCode() == 0 && key.getValue() != 0) {
            insert(key.getValue());
        }
    }

    /**
     * 光标左移并选中。你可以在Windows操作系统自带的文本编辑器中按下按键Ctrl+←重现这种操作
     */
    protected void leftMoveSelect() {
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        int index = cursorPositionIndex.getValue() - 1;
        cursorPositionIndex.setValue(index);
        if (index != cursorPositionIndex.getValue()) return;
        int fromIndex = getSelectFromIndex().getValue();
        getTextEditEffect().select(0, fromIndex, 0, index);
    }

    /**
     * 光标左移。你可以在Windows操作系统自带的文本编辑器中按下按键←重现这种操作
     */
    protected void leftMoveCursor() {
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        int index = cursorPositionIndex.getValue() - 1;
        cursorPositionIndex.setValue(index);
        if (index != cursorPositionIndex.getValue()) return;
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

    /**
     * 光标右移并选中。你可以在Windows操作系统自带的文本编辑器中按下按键Ctrl+→重现这种操作
     */
    protected void rightMoveSelect() {
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        int index = cursorPositionIndex.getValue() + 1;
        cursorPositionIndex.setValue(index);
        if (index != cursorPositionIndex.getValue()) return;
        int fromIndex = getSelectFromIndex().getValue();
        getTextEditEffect().select(0, fromIndex, 0, index);
    }

    /**
     * 光标右移。你可以在Windows操作系统自带的文本编辑器中按下按键→重现这种操作
     */
    protected void rightMoveCursor() {
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        int index = cursorPositionIndex.getValue() + 1;
        cursorPositionIndex.setValue(index);
        if (index != cursorPositionIndex.getValue()) return;
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

    /**
     * 删除选中的文本内容或删除光标左边一个字符。你可以在Windows操作系统自带的文本编辑器中按下Back键（它在回车键的上方，
     * 一般画着←符号）重现这种操作
     */
    protected void deleteSelectOrCursorLeft() {
        int fromIndex = getSelectFromIndex().getValue();
        int toIndex = getCursorPositionIndex().getValue();
        if (fromIndex == toIndex) {
            deleteCursorLeft();
        } else {
            deleteSelect();
        }
    }

    /**
     * 删除鼠标左边一个字符。你可以在Windows操作系统自带的文本编辑器中按下Back键（它在回车键的上方，一般画着←符号）重现
     * 这种操作
     */
    protected void deleteCursorLeft() {
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        int index = cursorPositionIndex.getValue() - 1;
        cursorPositionIndex.setValue(index);
        if (index != cursorPositionIndex.getValue()) return;
        getSelectFromIndex().setValue(index);
        String text = getTextProperty().getText();
        StringBuilder builder = new StringBuilder(text);
        builder.delete(index, index + 1);
        getTextProperty().setText(builder.toString());
        getTextEditEffect().setCursorPosition(0, index);
    }

    /**
     * 删除选中的文本内容。你可以在Windows操作系统自带的文本编辑器中选中部分文本内容后，按下Back键（它在回车键的上方，
     * 一般画着←符号）重现这种操作
     */
    protected void deleteSelect() {
        int fromIndex = getSelectFromIndex().getValue();
        int toIndex = getCursorPositionIndex().getValue();
        if (fromIndex == toIndex) return;
        int minIndex = Math.min(fromIndex, toIndex);
        int maxIndex = Math.max(fromIndex, toIndex);
        String text = getTextProperty().getText();
        StringBuilder builder = new StringBuilder(text);
        builder.delete(minIndex, maxIndex + 1);
        getTextProperty().setText(builder.toString());
        builder.reverse();
        getSelectFromIndex().setValue(minIndex);
        getCursorPositionIndex().setValue(minIndex);
        getTextEditEffect().setCursorPosition(0, minIndex);
    }

    /**
     * 插入一个字符。
     *
     * @param c 字符
     */
    protected void insert(char c) {
        //插入文本
        TextProperty textProperty = getTextProperty();
        String text = textProperty.getText();
        StringBuilder builder = new StringBuilder(text);
        int fromIndex = getSelectFromIndex().getValue();
        int index = getCursorPositionIndex().getValue();
        if (fromIndex != index) {
            int minIndex = Math.min(fromIndex, index);
            int maxIndex = Math.max(fromIndex, index);
            builder.delete(minIndex, maxIndex);
            builder.insert(minIndex, c);
            index = minIndex;
        } else {
            builder.insert(index, c);
        }
        textProperty.setText(builder.toString());

        //更新光标
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        index++;
        cursorPositionIndex.setValue(index);
        index = cursorPositionIndex.getValue();
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

    /**
     * 全选
     */
    protected void selectAll() {
        String text = getTextProperty().getText();
        int toIndex = text == null ? 0 : text.length();
        getTextEditEffect().select(0, 0, 0, toIndex);
        getCursorPositionIndex().setValue(toIndex);
        getSelectFromIndex().setValue(0);
    }

    /**
     * 复制选中的文本内容到系统剪贴板。你可以在Windows操作系统自带的文本编辑器中选中部分文本内容后，按下Ctrl+C重现这种操
     * 作
     */
    protected void copy() {
        int fromIndex = getSelectFromIndex().getValue();
        int toIndex = getCursorPositionIndex().getValue();
        if (fromIndex == toIndex) return;
        String text = getTextProperty().getText();
        if (text == null) return;
        int minIndex = Math.min(fromIndex, toIndex);
        int maxIndex = Math.max(fromIndex, toIndex);
        text = text.substring(minIndex, maxIndex);
        ClipboardEditor.setTextToClipboard(text);
    }

    /**
     * 从系统剪贴板获得文本内容并粘贴到光标所在位置。你可以在Windows操作系统自带的文本编辑器中，按下Ctrl+V重现这种操作
     */
    protected void paste() {
        //插入文本
        String textFromClipboard = ClipboardEditor.getTextFromClipboard();
        if (textFromClipboard == null || textFromClipboard.isEmpty()) return;
        TextProperty textProperty = getTextProperty();
        String text = textProperty.getText();
        StringBuilder builder = new StringBuilder(text);
        int fromIndex = getSelectFromIndex().getValue();
        int index = getCursorPositionIndex().getValue();
        if (fromIndex != index) {
            int minIndex = Math.min(fromIndex, index);
            int maxIndex = Math.max(fromIndex, index);
            builder.delete(minIndex, maxIndex);
            builder.insert(minIndex, textFromClipboard);
            index = minIndex;
        } else {
            builder.insert(index, textFromClipboard);
        }
        textProperty.setText(builder.toString());

        //更新光标
        Property<Integer> cursorPositionIndex = getCursorPositionIndex();
        index += textFromClipboard.length();
        cursorPositionIndex.setValue(index);
        index = cursorPositionIndex.getValue();
        getTextEditEffect().setCursorPosition(0, index);
        getSelectFromIndex().setValue(index);
    }

}
