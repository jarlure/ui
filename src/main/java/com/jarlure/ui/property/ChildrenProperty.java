package com.jarlure.ui.property;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.property.common.ListProperty;

import java.util.List;

public class ChildrenProperty extends ListProperty<UIComponent> implements WithUIComponent {

    @Override
    public void set(UIComponent component) {
        ChildrenPropertyListener listener = new ChildrenPropertyListener();
        listener.set(component);
        this.addPropertyListener(listener);
    }

    /**
     * 通过子组件名称获得子组件
     *
     * @param name 子组件的名称
     * @return 名称与给定名称相同的子组件。如果子组件列表中含有多个与给定名称相同的子组件，则返回最先添加的那一个。如果
     * 没有找到，则返回null
     */
    public UIComponent getChildByName(String name) {
        if (super.value == null) return null;
        for (UIComponent child : super.value) {
            if (name.equals(child.get(UIComponent.NAME))) return child;
            if (child.exist(ChildrenProperty.class)) {
                UIComponent grandChild = child.get(ChildrenProperty.class).getChildByName(name);
                if (grandChild != null) return grandChild;
            }
        }
        return null;
    }

    /**
     * 添加子组件
     * @param child 子组件
     */
    public void attachChild(UIComponent child) {
        super.remove(child);
        super.add(child);
    }

    /**
     * 添加子组件
     * @param children 子组件
     */
    public void attachChild(UIComponent... children) {
        super.remove(children);
        super.add(children);
    }

    /**
     * 添加子组件
     * @param children 子组件
     */
    public void attachChild(List<UIComponent> children) {
        attachChild(children.toArray(new UIComponent[0]));
    }

    /**
     * 添加子组件。已添加的索引值大于等于index的子组件将会重新排到该子组件之后
     * @param index 要插入的位置索引值
     * @param child 子组件
     */
    public void attachChildAt(int index, UIComponent child) {
        index = check(child, index);
        super.add(index, child);
    }

    /**
     * 添加子组件。已添加的索引值大于等于index的子组件将会重新排到这些子组件之后
     * @param index 要插入的位置索引值
     * @param children 子组件
     */
    public void attachChildAt(int index, UIComponent... children) {
        for (UIComponent child : children) {
            index = check(child, index);
        }
        super.add(index, children);
    }

    /**
     * 移除子组件
     * @param child 要移除的子组件
     * @return  移除成功返回true；否则返回false
     */
    public boolean detachChild(UIComponent child) {
        return super.remove(child);
    }

    /**
     * 移除子组件
     * @param children  要移除的子组件
     */
    public void detachChild(UIComponent... children) {
        super.remove(children);
    }

    /**
     * 检查child是否已经存在，若存在则移除它并返回移除child后改变的插入索引值。
     *
     * @param child
     * @param insertIndex 插入索引值
     * @return 新的插入索引值
     */
    private int check(UIComponent child, int insertIndex) {
        int index = indexOf(child);
        if (index == -1) return insertIndex;
        super.remove(index);
        if (index < insertIndex) insertIndex--;
        return insertIndex;
    }

    @Override
    @Deprecated
    public void add(UIComponent... values) {
        attachChild(values);
    }

    @Override
    @Deprecated
    public void add(UIComponent value) {
        attachChild(value);
    }

    @Override
    @Deprecated
    public boolean add(int index, UIComponent... values) {
        attachChildAt(index, values);
        return true;
    }

    @Override
    @Deprecated
    public void add(int index, UIComponent value) {
        attachChildAt(index, value);
    }

    @Override
    @Deprecated
    public void exchange(int indexA, int indexB) {
    }

    @Override
    @Deprecated
    public UIComponent set(int index, UIComponent newValue) {
        return null;
    }

}
