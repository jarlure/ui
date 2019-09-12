package com.jarlure.ui.converter;

import com.jarlure.ui.component.UIComponent;
import com.jarlure.ui.input.MouseEvent;
import com.jarlure.ui.property.*;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class SelectConverter implements WithUIComponent {

    protected UIComponent rootNode;
    protected int validMark;
    protected UIComponent selected;
    protected List<UIComponent> parentList=new ArrayList<>();

    /**
     * 用于反射机制。不要使用
     */
    public SelectConverter(){
    }

    @Override
    public void set(UIComponent component) {
        rootNode=component;
    }

    /**
     * 判断给定组件是否被鼠标选中。
     * @param component 给定的组件
     * @param mouse 鼠标
     * @return  未被选中、这个组件不可见或是被其他组件覆盖时返回false；否则返回true
     */
    public boolean isSelect(UIComponent component, MouseEvent mouse){
        checkAndUpdateSelected(mouse);
        if (component==null)return false;
        if (component.equals(selected))return true;
        if (parentList.isEmpty()) return false;
        for (UIComponent parent:parentList){
            if (component.equals(parent))return true;
        }
        return false;
    }

    /**
     * 判断被鼠标选中的组件当中是否有名为给定组件名的组件。
     * @param name  给定组件名
     * @param mouse 鼠标
     * @return  如果被选中的组件当中有叫作给定组件名的，返回true；否则返回false
     */
    public boolean isSelect(String name, MouseEvent mouse){
        checkAndUpdateSelected(mouse);
        if (selected==null)return false;
        if (name.equals(selected.get(UIComponent.NAME))) return true;
        if (parentList.isEmpty()) return false;
        for (UIComponent parent:parentList){
            if (name.equals(parent.get(UIComponent.NAME))) return true;
        }
        return false;
    }

    /**
     * 检查给定的鼠标事件是否是新的鼠标事件。是则重新计算鼠标选中，并记录到缓存中。
     * @param mouse 给定的鼠标事件
     */
    protected void checkAndUpdateSelected(MouseEvent mouse) {
        if (validMark == mouse.hashCode()) return;
        validMark = mouse.hashCode();
        UIComponent lastSelected = selected;
        if (lastSelected == rootNode) lastSelected = null;
        lastSelected = checkRange(lastSelected, mouse.x, mouse.y);
        selected = findSelected(rootNode, mouse.x, mouse.y, lastSelected);
        if (lastSelected == null || !lastSelected.equals(selected)) updateParentSelectedList();
    }

    /**
     * 检查上次被选中的组件是否仍然被选中。
     * @param selected  上次被选中的组件
     * @param x 当前鼠标的水平坐标x值
     * @param y 当前鼠标的垂直坐标y值
     * @return  null如果鼠标位置不在被选中的组件范围内；否则返回被选中的组件
     */
    private UIComponent checkRange(UIComponent selected, int x, int y) {
        if (selected == null) return null;
        if (!selected.isVisible())return null;
        if (!selected.get(AABB.class).contains(x, y)) return null;
        Spatial view = (Spatial) selected.get(UIComponent.VIEW);
        if (view instanceof Geometry) {
            Material mat = ((Geometry) view).getMaterial();
            MatParam rangeParam = mat.getParam("Range");
            if (rangeParam != null) {
                Vector4f range = (Vector4f) rangeParam.getValue();
                if (range != null) {
                    if (x < range.x) return null;
                    if (y < range.y) return null;
                    if (x > range.z) return null;
                    if (y > range.w) return null;
                }
            }
        }
        return selected;
    }

    /**
     * 寻找被选中的组件。被选中的组件的特点是可见、鼠标处于包围盒范围内、鼠标处于视图可见范围内、深度值更小（或同深度但
     * 更晚连接到父组件）
     *
     * @param component 被检测组件
     * @param x 鼠标的水平坐标x值
     * @param y 鼠标的垂直坐标y值
     * @param selected  候选组件
     * @return  如果被检测组件比候选组件更符合条件则返回被检测组件；否则返回候选组件
     */
    protected UIComponent findSelected(UIComponent component,int x,int y,UIComponent selected){
        if (component==null)return selected;
        if (component.equals(selected))return selected;
        if (!component.isVisible())return selected;
        if (!component.get(AABB.class).contains(x,y))return selected;
        if (component.exist(ChildrenProperty.class)) {
            if (component.exist(RangeProperty.class)){
                RangeProperty rangeProperty = component.get(RangeProperty.class);
                if (!rangeProperty.contains(x,y))return selected;
            }
            ChildrenProperty children = component.get(ChildrenProperty.class);
            for (int i = children.size() - 1; i >= 0; i--) {
                UIComponent child = children.get(i);
                selected = findSelected(child, x, y, selected);
            }
            return selected;
        }
        if (selected!=null){
            float depthOfCurrentSelected=selected.getDepth();
            if (component.getDepth() < depthOfCurrentSelected) return selected;
            if (component.getDepth() == depthOfCurrentSelected) {//深度值相同时，后添加的组件层级高
                return getLaterAttached(component,selected);
            }
        }
        return component;
    }

    /**
     * 当被检测组件和候选组件的深度值相当时，判断两个组件连接到父组件的先后顺序。后连接到父组件的会遮挡先连接到父组件的，
     * 因此更符合作为被选中的组件
     * @param component 当前被检测的组件
     * @param selected  候选组件
     * @return  后连接到父组件的那一个组件
     */
    private UIComponent getLaterAttached(UIComponent component,UIComponent selected){
        if (!selected.exist(ParentProperty.class))return selected;
        if (!component.exist(ParentProperty.class))return selected;
        List<UIComponent> selectedAncestors = getListOfComponentFromLeafToRoot(selected);
        List<UIComponent> componentAncestors = getListOfComponentFromLeafToRoot(component);
        if (selectedAncestors.isEmpty())return selected;
        if (componentAncestors.isEmpty())return selected;
        for (UIComponent ancestor:selectedAncestors){
            if (ancestor.equals(component))return selected;
        }
        for (UIComponent ancestor:componentAncestors){
            if (ancestor.equals(selected))return component;
        }
        UIComponent commonAncestor = null;
        UIComponent selectedAncestor=selected;
        UIComponent componentAncestor=component;
        for (int i=selectedAncestors.size()-1,j=componentAncestors.size()-1;i>=0&&j>=0;i--,j--){
            selectedAncestor=selectedAncestors.get(i);
            componentAncestor=componentAncestors.get(j);
            if (selectedAncestor==componentAncestor){
                commonAncestor=selectedAncestors.get(i);
            }else break;
        }
        if (commonAncestor == null) return selected;
        ChildrenProperty childrenProperty = commonAncestor.get(ChildrenProperty.class);
        int indexOfComponent = childrenProperty.indexOf(componentAncestor);
        int indexOfSelected = childrenProperty.indexOf(selectedAncestor);
        if (indexOfComponent<indexOfSelected)return selected;
        else return component;
    }

    /**
     * 获得该组件、该组件的父组件、祖父组件、曾祖父组件、曾曾祖父组件、……、鼻祖组件。
     * @param component 给定的组件
     * @return  包括给定组件在内的所有它的父组件
     */
    private List<UIComponent> getListOfComponentFromLeafToRoot(UIComponent component){
        List<UIComponent> list = new ArrayList<>();
        while (component!=null){
            list.add(component);
            if (component.exist(ParentProperty.class)){
                component=component.get(ParentProperty.class).getParent();
            }else break;
        }
        return list;
    }

    /**
     * 更新被选中组件列表。当一个组件被选中时，意味着它的父组件、祖父组件、曾祖父组件、曾曾祖父组件、……、鼻祖组件也被
     * 选中了。因此更新一下作为缓存的被选中组件列表。
     */
    protected void updateParentSelectedList(){
        parentList.clear();
        if (selected==null)return;
        UIComponent component=selected;
        while (component.exist(ParentProperty.class)){
            UIComponent parent = component.get(ParentProperty.class).getParent();
            if (parent==null)break;
            parentList.add(parent);
            component=parent;
        }
    }

}
