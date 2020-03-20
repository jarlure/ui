package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.CustomProperty;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

public class OBB extends CustomProperty implements WithUIComponent {

    protected SpatialProperty spatialProperty;

    /**
     * 用于反射自动创建。不要使用
     */
    public OBB(){
    }

    /**
     * 有向包围盒。有向包围盒将组件看作是一个可以绕z轴旋转的矩形
     *
     * @param spatialProperty Spatial属性
     */
    public OBB(SpatialProperty spatialProperty){
        this.spatialProperty = spatialProperty;
    }

    @Override
    public void set(UIComponent component) {
        this.spatialProperty = component.get(SpatialProperty.class);
    }

    /**
     * 获得有向包围盒的几何中心坐标位置。目前仅支持视图类型为Geometry的组件
     *
     * @return 有向包围盒的几何中心坐标位置
     */
    public Vector3f getCenter(Vector3f store){
        if (store==null) store=new Vector3f();
        Spatial view = spatialProperty.getSpatial();
        if (view instanceof Geometry){
            Vector3f center= ((Geometry)view).getModelBound().getCenter();
            return view.getWorldTransform().transformVector(center,store);
        }
        throw new UnsupportedOperationException();
    }

    /**
     * 获得有向包围盒的半个宽度。目前仅支持视图类型为Geometry的组件
     *
     * @return 有向包围盒的半个宽度
     */
    public float getXExtent() {
        Spatial view = spatialProperty.getSpatial();
        if (view instanceof Geometry){
            return ((BoundingBox)((Geometry) view).getModelBound()).getXExtent();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * 获得有向包围盒的半个高度。目前仅支持视图类型为Geometry的组件
     *
     * @return 有向包围盒的半个高度
     */
    public float getYExtent() {
        Spatial view = spatialProperty.getSpatial();
        if (view instanceof Geometry){
            return ((BoundingBox)((Geometry) view).getModelBound()).getYExtent();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * 获得有向包围盒的半个厚度。目前仅支持视图类型为Geometry的组件
     *
     * @return 有向包围盒的半个厚度
     */
    public float getZExtent() {
        Spatial view = spatialProperty.getSpatial();
        if (view instanceof Geometry){
            return ((BoundingBox)((Geometry) view).getModelBound()).getZExtent();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * 判断坐标点是否在有向包围盒内部或边上。目前仅支持视图类型为Geometry的组件
     *
     * @param x 水平坐标位置x值
     * @param y 垂直坐标位置y值
     * @return 如果坐标点落入有向包围盒内部或边上则返回true；否则返回false
     */
    public boolean contains(float x, float y) {
        Spatial view = spatialProperty.getSpatial();
        BoundingBox AABB = (BoundingBox) view.getWorldBound();
        if (Math.abs(x-AABB.getCenter().x)>AABB.getXExtent())return false;
        if (Math.abs(y-AABB.getCenter().y)>AABB.getYExtent())return false;
        if (view instanceof Geometry) {
            TempVars vars = TempVars.get();
            Vector3f point = vars.vect1.set(x, y, 0);
            Quaternion invRot = vars.quat1.set(view.getWorldRotation()).inverseLocal();
            point.subtractLocal(view.getWorldTranslation());
            invRot.multLocal(point);
            point.divideLocal(view.getWorldScale());
            BoundingBox box = (BoundingBox) ((Geometry) view).getModelBound();
            boolean result = box.contains(point);
            vars.release();
            return result;
        }
        throw new UnsupportedOperationException();
    }

}
