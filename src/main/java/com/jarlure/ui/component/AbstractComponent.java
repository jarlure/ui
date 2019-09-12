package com.jarlure.ui.component;

import com.jarlure.ui.property.SpatialProperty;
import com.jarlure.ui.property.WithUIComponent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractComponent implements UIComponent{

    private static final Logger LOG = Logger.getLogger(AbstractComponent.class.getSimpleName());

    protected Map<Class, Object> data;

    @Override
    public float getDepth() {
        return get(SpatialProperty.class).getWorldTranslation().getZ();
    }

    @Override
    public void setDepth(float depth) {
        get(SpatialProperty.class).move(0,0,depth-getDepth());
    }

    @Override
    public void scale(float x_percent, float y_percent) {
        if(Float.isNaN(x_percent)) throw new IllegalArgumentException("参数x_percent为无穷大！");
        if(Float.isNaN(y_percent)) throw new IllegalArgumentException("参数y_percent为无穷大！");
        get(SpatialProperty.class).scale(x_percent,y_percent,1);
    }

    @Override
    public void move(float dx, float dy) {
        SpatialProperty spatialProperty = get(SpatialProperty.class);
        Vector3f localScale = spatialProperty.getLocalScale();
        Vector3f worldScale = spatialProperty.getWorldScale();
        dx = dx * localScale.getX() / worldScale.getX();
        dy = dy * localScale.getY() / worldScale.getY();
        spatialProperty.move(dx,dy,0);
    }

    @Override
    public void rotate(float angle) {
        float halfAngle = 0.5f * angle;
        float w = FastMath.cos(halfAngle);
        float z = FastMath.sin(halfAngle);
        Quaternion q = new Quaternion(0,0,z,w);
        get(SpatialProperty.class).rotate(q);
    }

    @Override
    public boolean isVisible() {
        Spatial.CullHint hint = get(SpatialProperty.class).getCullHint();
        return hint !=null && hint != Spatial.CullHint.Always;
    }

    @Override
    public void setVisible(boolean visible) {
        Spatial.CullHint cullHint=visible ? Spatial.CullHint.Inherit : Spatial.CullHint.Always;
        get(SpatialProperty.class).setCullHint(cullHint);
    }

    @Override
    public boolean toggleVisible() {
        SpatialProperty spatialProperty = get(SpatialProperty.class);
        boolean visible = spatialProperty.getCullHint()==Spatial.CullHint.Always;
        Spatial.CullHint cullHint=visible ? Spatial.CullHint.Inherit : Spatial.CullHint.Always;
        spatialProperty.setCullHint(cullHint);
        return visible;
    }

    @Override
    public <T> boolean exist(Class<T> type) {
        return data != null && data.containsKey(type);
    }

    @Override
    public <T> T get(Class<T> type) {
        T value = data == null ? null : (T) data.get(type);
        if (value == null) {
            LOG.log(Level.WARNING, "在{0}中未找到类型{1}对应的值", new Object[]{this, type});
            try {
                value = type.newInstance();
                if (value instanceof WithUIComponent){
                    ((WithUIComponent)value).set(this);
                }
                set(type, value);
                LOG.log(Level.INFO,"已为{0}自动创建了{1}的类实例",new Object[]{this,type});
            } catch (IllegalAccessException | InstantiationException e) {
                LOG.log(Level.WARNING, "无法创建{0}的类实例，请检查该类是否存在空构造器", type);
                return null;
            }
        }
        return value;
    }

    @Override
    public <K, V extends K> void set(Class<K> type, V value) {
        if (data == null) data = new HashMap<>();
        data.put(type, value);
    }

    @Override
    public String toString() {
        String name = (String) get(NAME);
        if (name != null && !name.isEmpty()) return name;
        return super.toString();
    }
}