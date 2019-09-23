package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.effect.SwitchEffect;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputListener;

import static com.jarlure.ui.input.extend.ButtonMouseInputListener.*;

public abstract class SpringButtonMouseInputListener implements MouseInputListener {

    private UIComponent button;
    protected int state = 0;
    protected int stateWhenPressed = NULL;

    /**
     * 弹簧式按钮鼠标输入监听器。它会根据按钮是否被选中对其状态图片进行切换，并保持切换后的状态。
     *
     * @param button 弹簧式按钮。弹簧式按钮的特点是按下后不会弹起，需要再按一下才会弹起。实际生活中例如圆珠笔；图形用
     *               户界面中例如勾选框。该组件需要有SwitchEffect
     */
    public SpringButtonMouseInputListener(UIComponent button) {
        this.button = button;
    }

    protected abstract SelectConverter getSelectConverter();

    @Override
    public void onMove(MouseEvent mouse) {
        if (state == PRESSED) return;
        if (getSelectConverter().isSelect(button, mouse)) {
            if (state == MOVE_ON) return;
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
            stateWhenPressed = state;
            if (state != PRESSED) {
                state = PRESSED;
                button.get(SwitchEffect.class).switchTo(PRESSED);
            }
        }
    }

    @Override
    public void onLeftButtonRelease(MouseEvent mouse) {
        if (stateWhenPressed != NULL) {
            if (getSelectConverter().isSelect(button, mouse)) {
                if (stateWhenPressed == PRESSED) {
                    state = MOVE_ON;
                    button.get(SwitchEffect.class).switchTo(MOVE_ON);
                }
            } else {
                if (stateWhenPressed == PRESSED) {
                    state = PRESSED;
                    button.get(SwitchEffect.class).switchTo(PRESSED);
                } else {
                    state = NOTHING;
                    button.get(SwitchEffect.class).switchTo(NOTHING);
                }
            }
            stateWhenPressed = NULL;
        }
    }
}