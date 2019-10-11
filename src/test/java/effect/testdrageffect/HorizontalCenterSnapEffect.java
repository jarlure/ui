package effect.testdrageffect;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.converter.DynamicCoord;
import com.jarlure.ui.effect.SelectEffect;
import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.common.ListProperty;
import com.jme3.math.FastMath;

public class HorizontalCenterSnapEffect extends HorizontalScrollDragEffect implements SelectEffect {

    private DynamicCoord reference;

    public HorizontalCenterSnapEffect(ListProperty<UIComponent> componentListProperty, DynamicCoord reference) {
        super(componentListProperty,reference);
        this.reference=reference;
    }

    @Override
    public void end() {
        float dx = Integer.MAX_VALUE;
        float referenceX = reference.getWorldX();
        for (UIComponent component : componentListProperty.value) {
            AABB box = component.get(AABB.class);
            float xCenter = box.getXCenter();
            float dist = xCenter - referenceX;
            if (FastMath.abs(dist) < FastMath.abs(dx)) dx = dist;
        }
        if (dx == Integer.MAX_VALUE) dx = 0;
        endX = startX + dx;
        t = 0;
        finished = false;
    }

    @Override
    public void select(UIComponent component) {
        finishImmediately();
        AABB box = component.get(AABB.class);
        endX = box.getXCenter();
        startX = reference.getWorldX();
        t = 0;
        finished = false;
    }

}
