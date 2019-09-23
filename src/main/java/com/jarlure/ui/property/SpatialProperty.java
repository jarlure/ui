package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.CustomProperty;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SpatialProperty extends CustomProperty implements WithUIComponent {

    public enum Property {
        SPATIAL, NAME, CULL_HINT, WORLD_BOUND, PARENT,
        LOCAL_SCALE, LOCAL_ROTATION, LOCAL_TRANSLATION,
        WORLD_SCALE, WORLD_ROTATION, WORLD_TRANSLATION
    }

    protected Spatial spatial;
    private Vector3f vec;
    private Quaternion q;

    /**
     * 用于反射自动创建。不要使用
     */
    public SpatialProperty() {
    }

    /**
     * 组件的Spatial属性
     *
     * @param spatial spatial参数
     */
    public SpatialProperty(Spatial spatial) {
        this.spatial = spatial;
    }

    @Override
    public void set(UIComponent component) {
        this.spatial = (Spatial) component.get(UIComponent.VIEW);
        SpatialPropertyListener listener = new SpatialPropertyListener();
        listener.set(component);
        this.addPropertyListener(listener);
    }

    public Spatial getSpatial() {
        return (Spatial) filterOutputProperty(Property.SPATIAL, spatial);
    }

    public void setSpatial(Spatial spatial) {
        Enum property = Property.SPATIAL;
        Object oldValue = this.spatial;
        Object newValue = spatial;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial = spatial;
        propertyChanged(property, oldValue, newValue);
    }

    public String getName() {
        return (String) filterOutputProperty(Property.NAME, spatial.getName());
    }

    public void setName(String name) {
        Enum property = Property.NAME;
        Object oldValue = this.spatial.getName();
        Object newValue = name;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.setName(name);
        propertyChanged(property, oldValue, newValue);
    }

    public Spatial.CullHint getCullHint() {
        return (Spatial.CullHint) filterOutputProperty(Property.SPATIAL, spatial.getCullHint());
    }

    public void setCullHint(Spatial.CullHint hint) {
        Enum property = Property.CULL_HINT;
        Object oldValue = this.spatial.getCullHint();
        Object newValue = hint;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.setCullHint(hint);
        propertyChanged(property, oldValue, newValue);
    }

    public BoundingVolume getWorldBound() {
        return (BoundingVolume) filterOutputProperty(Property.SPATIAL, spatial.getWorldBound());
    }

    public Vector3f getWorldScale() {
        return (Vector3f) filterOutputProperty(Property.SPATIAL, spatial.getWorldScale());
    }

    public void worldScaleChanged() {
        Vector3f worldScale = getWorldScale();
        propertyChanged(Property.WORLD_SCALE, worldScale, worldScale);
    }

    public void scale(float scale) {
        Vector3f vec = spatial.getLocalScale().clone();
        vec.multLocal(scale);
        setLocalScale(vec);
    }

    public void scale(float x, float y, float z) {
        Vector3f vec = spatial.getLocalScale().clone();
        vec.multLocal(x, y, z);
        setLocalScale(vec);
    }

    public Vector3f getLocalScale() {
        return (Vector3f) filterOutputProperty(Property.SPATIAL, spatial.getLocalScale());
    }

    public void setLocalScale(float localScale) {
        setLocalScale(localScale, localScale, localScale);
    }

    public void setLocalScale(float x, float y, float z) {
        setLocalScale(new Vector3f(x, y, z));
    }

    public void setLocalScale(Vector3f localScale) {
        Enum property = Property.LOCAL_SCALE;
        if (vec == null) vec = new Vector3f();
        Object oldValue = vec.set(this.spatial.getLocalScale());
        Object newValue = localScale;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.setLocalScale(localScale);
        propertyChanged(property, oldValue, newValue);
    }

    public Quaternion getWorldRotation() {
        return (Quaternion) filterOutputProperty(Property.SPATIAL, spatial.getWorldRotation());
    }

    public void worldRotationChanged() {
        Quaternion worldRotation = getWorldRotation();
        propertyChanged(Property.WORLD_ROTATION, worldRotation, worldRotation);
    }

    public void rotate(Quaternion rot) {
        setLocalRotation(spatial.getLocalRotation().mult(rot));
    }

    public Quaternion getLocalRotation() {
        return (Quaternion) filterOutputProperty(Property.SPATIAL, spatial.getLocalRotation());
    }

    public void setLocalRotation(Quaternion rotation) {
        Enum property = Property.LOCAL_ROTATION;
        if (q == null) q = new Quaternion();
        Object oldValue = q.set(this.spatial.getLocalRotation());
        Object newValue = rotation;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.setLocalRotation(rotation);
        propertyChanged(property, oldValue, newValue);
    }

    public Vector3f getWorldTranslation() {
        return (Vector3f) filterOutputProperty(Property.SPATIAL, spatial.getWorldTranslation());
    }

    public void worldTranslationChanged() {
        Vector3f worldTranslation = getWorldTranslation();
        propertyChanged(Property.WORLD_TRANSLATION, worldTranslation, worldTranslation);
    }

    public void move(float x, float y, float z) {
        Vector3f location = spatial.getLocalTranslation().add(x, y, z);
        setLocalTranslation(location);
    }

    public Vector3f getLocalTranslation() {
        return (Vector3f) filterOutputProperty(Property.SPATIAL, spatial.getLocalTranslation());
    }

    public void setLocalTranslation(float x, float y, float z) {
        setLocalTranslation(new Vector3f(x, y, z));
    }

    public void setLocalTranslation(Vector3f localTranslation) {
        Enum property = Property.LOCAL_TRANSLATION;
        if (vec == null) vec = new Vector3f();
        Object oldValue = vec.set(this.spatial.getLocalTranslation());
        Object newValue = localTranslation;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.setLocalTranslation(localTranslation);
        propertyChanged(property, oldValue, newValue);
    }

    public Node getParent() {
        return (Node) filterOutputProperty(Property.SPATIAL, spatial.getParent());
    }

    public void setParent(Node parent) {
        Enum property = Property.PARENT;
        Object oldValue = this.spatial.getParent();
        Object newValue = parent;

        newValue = filterInputProperty(property, newValue);
        boolean pass = interceptProperty(property, newValue);
        if (!pass) return;
        this.spatial.removeFromParent();
        if (parent != null) parent.attachChild(this.spatial);
        propertyChanged(property, oldValue, newValue);
    }

}