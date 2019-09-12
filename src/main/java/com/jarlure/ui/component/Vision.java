package com.jarlure.ui.component;

import com.jme3.scene.Spatial;

public class Vision extends AbstractComponent {

    private Spatial view;

    /**
     * 用于子类继承或序列化。不要调用
     */
    public Vision() {
    }

    /**
     * 镜像。该组件用于封装Spatial或作为另一个组件的替身存在的情况。例如拖拽组件移动时如果你不希望原来的组件实际上发生
     * 移动，那么你可以选择将原组件的视图作为参数创建一个镜像，然后对镜像进行操作达到移动效果的目的。
     *
     * @param spatial   镜像会浅拷贝（除材质共用外其他数据相互独立）一份传入的spatial并作为自己的视图。如果spatial是一个
     *                  Node，那么它的所有子Spatial都会被深拷贝（所有数据均拷贝一份，包括拷贝材质）
     */
    public Vision(Spatial spatial) {
        view = spatial.clone(false);
        view.setLocalTranslation(spatial.getWorldTranslation());
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

}
