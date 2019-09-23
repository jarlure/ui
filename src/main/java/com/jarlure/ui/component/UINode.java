package com.jarlure.ui.component;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class UINode extends AbstractComponent {

    protected Node view;

    /**
     * 用于子类继承或序列化。不要调用
     */
    protected UINode() {
    }

    /**
     * 结点。用于连接多个组件。与面板不同，结点的尺寸取决于连接组件的尺寸。因此结点不适合动态增删子组件。
     *
     * @param name 组件名
     */
    public UINode(String name) {
        view = new AutoUpdatedNode(name);
    }

    @Override
    public Object get(String param) {
        switch (param) {
            case VIEW:
                return view;
            case NAME:
                return view.getName();
        }
        return null;
    }

    @Override
    public <T> T get(Class<T> type) {
        return super.get(type);
    }

    /**
     * 用于解决改变Spatial状态后如果未手动调用updateGeometricState()方法会抛异常的问题。实际上它的功能与
     * com.jme3.scene.Node完全一样。
     */
    private static class AutoUpdatedNode extends Node {

        public AutoUpdatedNode(String name) {
            super(name);
        }

        @Override
        public boolean checkCulling(Camera cam) {
            if (refreshFlags != 0) {
                updateGeometricState();
            }
            return super.checkCulling(cam);
        }

    }

}
