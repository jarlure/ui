package com.jarlure.ui.component;

import com.jarlure.ui.property.AABB;
import com.jarlure.ui.property.ChildrenProperty;
import com.jme3.scene.Node;
import com.jme3.texture.Image;

public class Panel extends AbstractComponent {

    public static final String PANEL = "panel";

    protected Node view;
    protected UIComponent panel;

    /**
     * 用于子类继承或序列化。不要调用
     */
    public Panel() {
    }

    public Panel(String name, int width, int height) {
        this(name, new Picture(PANEL, width, height));
    }

    public Panel(String name, Image img) {
        this(name, new Picture(PANEL, img));
    }

    /**
     * 面板。面板是一个复合组件，它的尺寸只跟构造时传递的子组件panel有关。面板常被用于动态添加相同尺寸的子组件元素。这些
     * 组件元素应该放进ElementProperty中。而你使用ChildrenProperty添加一些固定不动态移除的子组件。例如面板背景、装饰等。
     *
     * @param name  组件名
     * @param panel 面板。用于决定面板的尺寸、背景色。
     */
    protected Panel(String name, UIComponent panel) {
        view = new Node(name);
        this.panel = panel;
        get(ChildrenProperty.class).attachChild(panel);
    }

    @Override
    public Object get(String param) {
        switch (param) {
            case VIEW:
                return view;
            case NAME:
                return view.getName();
            case PANEL:
                return panel;
        }
        return null;
    }

    @Override
    public <T> T get(Class<T> type) {
        if (AABB.class.isAssignableFrom(type)) {
            return panel.get(type);
        }
        return super.get(type);
    }

}
