package com.jarlure.ui.system;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.component.UINode;
import com.jarlure.ui.property.ChildrenProperty;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class UIRenderState extends AbstractAppState {

    private Application app;
    private UINode node;
    private UICamera camera;
    private ViewPort viewPort;

    /**
     * 该AppState用于替代SimpleApplication的guiNode。在以前的测试版本中，该AppState的作用是不使用画家算法、恢复深度测试
     * 从而解决多个3D图形堆叠在GUI上不正确显示问题。但是这种方案需要用户对所有的平面组件也要设置不同的深度值以保证不会出
     * 现z-fighting问题。当前版本该AppState已经与guiNode一模一样，不再使用深度测试。不过如果你需要3D图像正确显示在GUI上，
     * 可以使用UIRenderState(new UINode("UI Node"))重新恢复到以前的测试版本。
     */
    public UIRenderState() {
        node = new UINode("UI Node");

        Node view = (Node) node.get(UIComponent.VIEW);
        view.setQueueBucket(RenderQueue.Bucket.Gui);
        view.setCullHint(Spatial.CullHint.Never);
    }

    public UIRenderState(UINode node) {
        this.node = node;
    }

    public UINode getNode() {
        return node;
    }

    public void setNode(UINode node) {
        if (this.node == node) return;
        if (initialized) {
            if (this.node != null) viewPort.detachScene((Spatial) this.node.get(UIComponent.VIEW));
            if (node != null) viewPort.attachScene((Spatial) node.get(UIComponent.VIEW));
        }
        this.node = node;
    }

    public void attachChildToNode(UIComponent... children) {
        node.get(ChildrenProperty.class).attachChild(children);
    }

    public void detachChildFromNode(UIComponent... children) {
        node.get(ChildrenProperty.class).detachChild(children);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
        AppSettings settings = app.getContext().getSettings();
        camera = new UICamera(settings.getWidth(), settings.getHeight());
        viewPort = app.getRenderManager().createPostView("UIPostView", camera);
        if (node != null) viewPort.attachScene((Spatial) node.get(UIComponent.VIEW));
        initialized = true;
    }

    @Override
    public void cleanup() {
        initialized = false;
        if (this.node != null) viewPort.detachScene((Spatial) this.node.get(UIComponent.VIEW));
        app.getRenderManager().removePostView(viewPort);
    }

    @Override
    public void update(float tpf) {
        if (node != null) ((Spatial) node.get(UIComponent.VIEW)).updateLogicalState(tpf);
    }

    private static class UICamera extends Camera {

        private UICamera(int width, int height) {
            super(width, height);
            setFrustum(-1000, 1000, 0, width, height, 0);
            setLocation(Vector3f.UNIT_Z);
            lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        }

        @Override
        public void resize(int width, int height, boolean fixAspect) {
            super.resize(width, height, false);
            frustumRight = frustumTop * ((float) width / height);
            frustumLeft = -frustumRight;
            setFrustum(-1000, 1000, 0, width, height, 0);
        }

        @Override
        public FrustumIntersect contains(BoundingVolume bound) {
            if (containsGui(bound)) return FrustumIntersect.Inside;
            return FrustumIntersect.Outside;
        }

    }

}
