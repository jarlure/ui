package effect.testdrageffect;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.effect.DragEffect;
import com.jarlure.ui.property.common.ListProperty;

public class HorizontalDragEffect implements DragEffect {

    protected float t1 = 0.25f;//动画播放时长
    protected float div_t1 = 4;//数据缓存，div_t1=1/t1

    protected ListProperty<UIComponent> componentListProperty;
    protected float startX;
    protected float endX;
    protected float t;
    protected boolean finished;

    /**
     * 水平拖拽效果。
     *
     * @param componentListProperty 施加移动效果的组件元素列表。一般是ChildrenProperty或ElementProperty
     */
    public HorizontalDragEffect(ListProperty<UIComponent> componentListProperty) {
        this.componentListProperty = componentListProperty;
    }

    @Override
    public void start(float x, float y) {
        finishImmediately();
        endX = startX = x;
    }

    @Override
    public void update(float x, float y) {
        float dx = x - endX;
        move(dx);
        endX = x;
    }

    @Override
    public void end() {
        t = 0;
        finished = false;
    }

    @Override
    public void update(float tpf) {
        if (finished) return;
        if (t1 - t <= tpf) {
            finishImmediately();
            return;
        }
        float dx = tpf * div_t1 * (startX - endX);
        move(dx);
        t += tpf;
    }

    @Override
    public void finishImmediately() {
        float dt = t1 - t;
        float dx = dt * div_t1 * (startX - endX);
        move(dx);
        t = t1;
        finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * 移动每个组件元素
     *
     * @param dx 移动距离
     */
    protected void move(float dx) {
        for (UIComponent component : componentListProperty.value) {
            if (component == null) continue;
            component.move(dx, 0);
        }
    }

}
