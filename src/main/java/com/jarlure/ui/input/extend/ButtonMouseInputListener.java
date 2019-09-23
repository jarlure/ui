package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.SwitchEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputListener;

public abstract class ButtonMouseInputListener implements MouseInputListener {

    public static final int NULL = -1;
    public static final int NOTHING = 0;
    public static final int MOVE_ON = 1;
    public static final int PRESSED = 2;

    private UIComponent button;
    protected int state = 0;

    /**
     * 按钮鼠标输入监听器。它会根据按钮是否被选中对其状态图片进行切换。
     *
     * @param button 按钮。该组件需要有SwitchEffect
     */
    public ButtonMouseInputListener(UIComponent button) {
        this.button = button;
    }

    protected abstract SelectConverter getSelectConverter();

    @Override
    public void onMove(MouseEvent mouse) {
        if (getSelectConverter().isSelect(button, mouse)) {
            if (state != NOTHING) return;
            state = MOVE_ON;
            button.get(SwitchEffect.class).switchTo(MOVE_ON);
        } else if (state != NOTHING) {
            state = NOTHING;
            button.get(SwitchEffect.class).switchTo(NOTHING);
        }
    }

    @Override
    public void onLeftButtonPress(MouseEvent mouse) {
        if (getSelectConverter().isSelect(button, mouse)) {
            if (state == PRESSED) return;
            state = PRESSED;
            button.get(SwitchEffect.class).switchTo(PRESSED);
        }
    }

    @Override
    public void onLeftButtonRelease(MouseEvent mouse) {
        if (getSelectConverter().isSelect(button, mouse)) {
            state = MOVE_ON;
            button.get(SwitchEffect.class).switchTo(MOVE_ON);
        } else if (state != NOTHING) {
            state = NOTHING;
            button.get(SwitchEffect.class).switchTo(NOTHING);
        }
    }

}