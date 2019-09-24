package com.jarlure.ui.input.extend;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.ScrollConverter;
import com.jarlure.ui.converter.SelectConverter;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.input.MouseInputAdapter;
import com.jarlure.ui.property.AABB;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class VerticalScrollInputListener extends MouseInputAdapter {

    private static final Logger LOG = Logger.getLogger(VerticalScrollInputListener.class.getName());
    protected static final int NULL = Integer.MAX_VALUE;
    private UIComponent scrollBar;
    private UIComponent scene;
    private UIComponent object;
    protected float wheelRollingSpeed;
    protected float startYTop = NULL;

    public VerticalScrollInputListener(UIComponent scrollBar, UIComponent scene, UIComponent object) {
        this(5, scrollBar, scene, object);
    }

    /**
     * 垂直滚动条鼠标输入监听器。该监听器会根据鼠标位置和滚动条当前位置更新观察对象的位置。注意，该监听器不会更新滚动条
     * 的位置。滚动条的位置更新应该交由观察对象的位置监听器执行，这样才能确保不会发生死循环。
     *
     * @param rollSpeed 滑轮滚动速度。单位是像素每次。默认值为每次触发滚轮事件相当于滚动条移动5像素。
     * @param scrollBar 滚动条。该组件需要有ScrollConverter
     * @param window    视窗窗口的包围盒
     * @param object    观察对象
     */
    public VerticalScrollInputListener(float rollSpeed, UIComponent scrollBar, UIComponent window, UIComponent object) {
        this.wheelRollingSpeed = rollSpeed;
        this.scrollBar = scrollBar;
        this.scene = window;
        this.object = object;
    }

    protected abstract SelectConverter getSelectConverter();

    @Override
    public void onWheelRolling(MouseEvent mouse) {
        SelectConverter selectConverter = getSelectConverter();
        if (selectConverter.isSelect(scene, mouse) || selectConverter.isSelect(scrollBar, mouse)) {
            setObjectYTop(scrollBar.get(ScrollConverter.class).getYTop() + wheelRollingSpeed * Math.signum(mouse.dw));
        }
    }

    @Override
    public void onLeftButtonPress(MouseEvent mouse) {
        if (!getSelectConverter().isSelect(scrollBar, mouse)) return;
        ScrollConverter scrollConverter = scrollBar.get(ScrollConverter.class);
        float yTop = scrollConverter.getYTop();
        if (mouse.y > yTop) return;
        float yBottom = yTop - scrollConverter.getHeight();
        if (mouse.y < yBottom) return;
        startYTop = yTop;
    }

    @Override
    public void onLeftButtonDragging(MouseEvent mouse) {
        if (startYTop != NULL) {
            setObjectYTop(startYTop + mouse.y - mouse.getPressY());
        }
    }

    @Override
    public void onLeftButtonRelease(MouseEvent mouse) {
        startYTop = NULL;
    }

    protected void setObjectYTop(float yTop) {
        LOG.log(Level.INFO, "yTop:{0}", yTop);
        yTop = scrollBar.get(ScrollConverter.class).getObjectYTop(yTop);
        object.move(0, yTop - object.get(AABB.class).getYTop());
    }

}
