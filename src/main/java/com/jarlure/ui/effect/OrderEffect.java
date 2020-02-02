package com.jarlure.ui.effect;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.DynamicCoord;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.common.ListProperty;
import com.jarlure.ui.property.common.ListPropertyAdapter;
import com.jarlure.ui.property.common.ListPropertyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderEffect {

    private static final Logger LOG = Logger.getLogger(OrderEffect.class.getName());

    protected int startIndex;
    protected DynamicCoord fixSize;
    protected DynamicCoord fixPoint0, fixPoint1;
    protected ListProperty<UIComponent> componentListProperty;
    protected ListPropertyListener<UIComponent> componentListListener = new ListPropertyAdapter<UIComponent>() {
        @Override
        public void propertyAdded(int index, UIComponent component) {
            AABB box = component.get(AABB.class);
            if (fixSize != null) {
                float scale=Math.min(getOrderWidth()/box.getWidth(),getOrderHeight()/box.getHeight());
                component.scale(scale);
            }
            if (fixPoint0 != null && fixPoint1 != null) {
                float firstLocationX, firstLocationY;
                if (index < startIndex) {
                    LOG.log(Level.WARNING, "组件{0}的索引值index:{1}小于初始索引startIndex:{2}，无法确定该组件的位置", new Object[]{component, index, startIndex});
                    return;
                } else if (index == startIndex) {
                    firstLocationX = fixPoint0.getWorldX();
                    firstLocationY = fixPoint0.getWorldY();
                } else {
                    UIComponent firstComponent = componentListProperty.get(startIndex);
                    AABB firstBox = firstComponent.get(AABB.class);
                    firstLocationX = firstBox.getXCenter();
                    firstLocationY = firstBox.getYCenter();
                }
                float dx = getOrderPositionX(index, firstLocationX) - box.getXCenter();
                float dy = getOrderPositionY(index, firstLocationY) - box.getYCenter();
                component.move(dx, dy);
            }
        }

        @Override
        protected void foldAnonymousInnerClassCode(ListPropertyAdapter instance) {
        }
    };

    public OrderEffect(ListProperty<UIComponent> componentListProperty, DynamicCoord fixPoint0, DynamicCoord fixPoint1) {
        this(componentListProperty, null, fixPoint0, fixPoint1);
    }

    /**
     * 整齐效果。该效果会使组件元素拥有统一的尺寸和间距，并设置以索引值作为参数代入直线方程中得到的位置。
     *
     * @param componentListProperty 组件元素
     * @param fixSize               组件等比例缩放后的尺寸
     * @param fixPoint0             组件0的中心点位置坐标
     * @param fixPoint1             组件1的中心点位置坐标
     */
    public OrderEffect(ListProperty<UIComponent> componentListProperty, DynamicCoord fixSize, DynamicCoord fixPoint0, DynamicCoord fixPoint1) {
        this.componentListProperty = componentListProperty;
        startIndex = componentListProperty.size();
        this.fixSize = fixSize;
        this.fixPoint0 = fixPoint0;
        this.fixPoint1 = fixPoint1;
        componentListProperty.addPropertyListener(componentListListener);
    }

    /**
     * 获取组件的统一尺寸
     *
     * @return 组件的统一尺寸
     */
    public DynamicCoord getFixSize() {
        return fixSize;
    }

    /**
     * 获取组件0的中心点位置坐标
     *
     * @return 组件0的中心点位置坐标
     */
    public DynamicCoord getFixPoint0() {
        return fixPoint0;
    }

    /**
     * 获取组件1的中心点位置坐标
     *
     * @return 组件1的中心点位置坐标
     */
    public DynamicCoord getFixPoint1() {
        return fixPoint1;
    }

    /**
     * 获得组件的统一宽度
     *
     * @return 组件的统一宽度
     */
    protected float getOrderWidth() {
        return fixSize.getLocalX();
    }

    /**
     * 获得组件的统一高度
     *
     * @return 组件的统一高度
     */
    protected float getOrderHeight() {
        return fixSize.getLocalY();
    }

    /**
     * 计算给定组件N的位置水平坐标x值
     *
     * @param index            给定组件N的索引值
     * @param currentFixPoint0 组件0的中心点位置水平坐标x值
     * @return 位置水平坐标x值
     */
    protected float getOrderPositionX(int index, float currentFixPoint0) {
        float fix0 = fixPoint0.getWorldX();
        float fix1 = fixPoint1.getWorldX();
        float fixN = currentFixPoint0 + (index - startIndex) * (fix1 - fix0);
        return fixN;
    }

    /**
     * 计算给定组件N的位置垂直坐标y值
     *
     * @param index            给定组件N的索引值
     * @param currentFixPoint0 组件0的中心点位置垂直坐标y值
     * @return 位置垂直坐标y值
     */
    protected float getOrderPositionY(int index, float currentFixPoint0) {
        float fix0 = fixPoint0.getWorldY();
        float fix1 = fixPoint1.getWorldY();
        float fixN = currentFixPoint0 + (index - startIndex) * (fix1 - fix0);
        return fixN;
    }

}
