package effect.testdrageffect;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.DynamicCoord;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.common.ListProperty;
import com.jme3.math.FastMath;

import java.util.List;

public class HorizontalScrollDragEffect extends HorizontalDragEffect {

    private DynamicCoord primaryReference;
    private DynamicCoord secondaryReference;
    private float[] minAndMaxValueStore = new float[2];

    public HorizontalScrollDragEffect(ListProperty<UIComponent> componentListProperty, DynamicCoord primaryReference) {
        this(componentListProperty, primaryReference, null);
    }

    /**
     * 水平滚动拖拽效果。
     *
     * @param componentListProperty 施加移动效果的组件元素列表。一般是ChildrenProperty或ElementProperty
     * @param primaryReference      临界位置点1。通常是组件元素列表中第一个组件的位置点
     * @param secondaryReference    临界位置点2。通常是组件元素列表中最后一个组件刚好完整显示时的位置点
     */
    public HorizontalScrollDragEffect(ListProperty<UIComponent> componentListProperty, DynamicCoord primaryReference, DynamicCoord secondaryReference) {
        super(componentListProperty);
        this.primaryReference = primaryReference;
        this.secondaryReference = secondaryReference;
    }

    @Override
    public void update(int x, int y) {
        if (getMinAndMaxXCenter(minAndMaxValueStore) == null) return;
        float minXCenter = minAndMaxValueStore[0];
        float maxXCenter = minAndMaxValueStore[1];
        getMinAndMaxReferenceX(minAndMaxValueStore);
        float minReferenceX = minAndMaxValueStore[0];
        float maxReferenceX = minAndMaxValueStore[1];
        float dx = x - endX;
        if (0 < dx && minReferenceX < minXCenter) {//最左边的组件中心点已经超过参考位置并且继续往右移动
            dx = 0.5f;
        } else if (dx < 0 && maxXCenter < maxReferenceX) {//最右边的组件中心点已经超过参考位置并且继续往左移动
            dx = -0.5f;
        }
        endX = dx + endX;
        move(dx);
    }

    @Override
    public void end() {
        if (null == getMinAndMaxXCenter(minAndMaxValueStore)) return;
        float dx = 0;
        float minXCenter = minAndMaxValueStore[0];
        float maxXCenter = minAndMaxValueStore[1];
        float primaryReferenceX = primaryReference.getWorldX();
        if (primaryReferenceX < minXCenter) {
            dx = primaryReferenceX - minXCenter;
        } else if (maxXCenter < primaryReferenceX) {
            dx = primaryReferenceX - maxXCenter;
        } else if (secondaryReference != null) {
            float secondaryReferenceX = secondaryReference.getWorldX();
            if (FastMath.abs(minXCenter - maxXCenter) < FastMath.abs(primaryReferenceX - secondaryReferenceX)) {
                if (primaryReferenceX < secondaryReferenceX) {
                    dx = primaryReferenceX - minXCenter;
                }else {
                    dx = primaryReferenceX - maxXCenter;
                }
            } else if (secondaryReferenceX < minXCenter) {
                dx = secondaryReferenceX - minXCenter;
            } else if (maxXCenter < secondaryReferenceX) {
                dx = secondaryReferenceX - maxXCenter;
            }
        }
        endX = startX - dx;
        t = 0;
        finished = false;
    }

    private float[] getMinAndMaxXCenter(float[] store) {
        List<UIComponent> componentList = componentListProperty.value;
        float minXCenter = Integer.MAX_VALUE;
        float maxXCenter = Integer.MIN_VALUE;
        for (UIComponent component : componentList) {
            AABB box = component.get(AABB.class);
            float xCenter = box.getXCenter();
            if (xCenter < minXCenter) {
                minXCenter = xCenter;
            }
            if (xCenter > maxXCenter) {
                maxXCenter = xCenter;
            }
        }
        if (minXCenter == Integer.MAX_VALUE || maxXCenter == Integer.MIN_VALUE) return null;
        store[0] = minXCenter;
        store[1] = maxXCenter;
        return store;
    }

    private void getMinAndMaxReferenceX(float[] store) {
        float minReferenceX = primaryReference.getWorldX();
        float maxReferenceX = minReferenceX;
        if (secondaryReference != null) {
            float secondaryReferenceX = secondaryReference.getWorldX();
            if (secondaryReferenceX < minReferenceX) minReferenceX = secondaryReferenceX;
            else if (maxReferenceX < secondaryReferenceX) maxReferenceX = secondaryReferenceX;
        }
        store[0] = minReferenceX;
        store[1] = maxReferenceX;
    }

}
