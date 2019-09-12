package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.ScrollConverter;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputListener;
import com.jarlure.ui.property.AABB;

public abstract class HorizontalScrollInputListener implements MouseInputListener {

    protected static final int NULL = Integer.MAX_VALUE;
    private UIComponent scrollBar;
    private UIComponent object;
    protected float startXLeft = NULL;

    /**
     * 水平滚动条鼠标输入监听器。该监听器会根据鼠标位置和滚动条当前位置更新观察对象的位置。注意，该监听器不会更新滚动条
     * 的位置。滚动条的位置更新应该交由观察对象的位置监听器执行，这样才能确保不会发生死循环。
     *
     * @param scrollBar 滚动条。该组件需要有ScrollConverter
     * @param object    观察对象
     */
    public HorizontalScrollInputListener(UIComponent scrollBar, UIComponent object) {
        this.scrollBar = scrollBar;
        this.object = object;
    }

    protected abstract SelectConverter getSelectConverter();

    @Override
    public void onLeftButtonPress(MouseEvent mouse) {
        if (!getSelectConverter().isSelect(scrollBar, mouse)) return;
        ScrollConverter scrollConverter = scrollBar.get(ScrollConverter.class);
        float xLeft = scrollConverter.getXLeft();
        if (mouse.x < xLeft) return;
        float xRight = xLeft - scrollConverter.getWidth();
        if (xRight < mouse.y) return;
        startXLeft = xLeft;
    }

    @Override
    public void onLeftButtonDragging(MouseEvent mouse) {
        if (startXLeft != NULL) {
            setObjectXLeft(startXLeft + mouse.x - mouse.getPressX());
        }
    }

    @Override
    public void onLeftButtonRelease(MouseEvent mouse) {
        startXLeft = NULL;
    }

    protected void setObjectXLeft(float xLeft) {
        xLeft = scrollBar.get(ScrollConverter.class).getObjectXLeft(xLeft);
        object.move(xLeft - object.get(AABB.class).getXLeft(), 0);
    }

}
