package com.jarlure.ui.component;

import com.jarlure.ui.system.AssetManager;
import com.jarlure.ui.util.ImageHandler;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

public class Picture extends AbstractComponent {

    protected static final Quad MESH_DEFAULT = new Quad(1f, 1f);

    protected Geometry view;

    /**
     * 用于子类继承或序列化。不要调用
     */
    public Picture() {
    }

    /**
     * 创建一个透明图片组件。该图片的图片数据只有1x1像素，因此不能直接在上面绘制文字或多种色彩。
     *
     * @param name   组件名
     * @param width  图片的宽度
     * @param height 图片的高度
     */
    public Picture(String name, int width, int height) {
        this(name, MESH_DEFAULT, ImageHandler.createEmptyImage(1, 1), width, height);
    }

    /**
     * 创建一个给定图片数据的图片组件。
     *
     * @param name 组件名
     * @param img  图片数据。该数据包含有图片的颜色数据和尺寸数据
     */
    public Picture(String name, Image img) {
        this(name, MESH_DEFAULT, img, img.getWidth(), img.getHeight());
    }

    /**
     * 图片。图片是最常用、功能最强大的最基础组件。一切可视组件都是由图片组件组成的。你可以把图片组件当作图片、标签、
     * 按钮、文本。将三个图片组件和结点组件组合在一起，你可以得到进度条、滚动条或是滑块。图片组件无法添加子组件。
     *
     * @param name   组件名
     * @param mesh   组件视图的网格。通常是1x1的Quad
     * @param img    图片的图片数据
     * @param width  图片的宽度
     * @param height 图片的高度
     */
    public Picture(String name, Mesh mesh, Image img, int width, int height) {
        view = new Geometry(name, mesh);
        Material material = new Material(AssetManager.getGuiMaterialDef());
        material.setColor("Color", ColorRGBA.White);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Texture2D texture = new Texture2D(img);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        material.setTexture("Texture", texture);
        view.setMaterial(material);
        view.setLocalScale(width, height, 1);
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
