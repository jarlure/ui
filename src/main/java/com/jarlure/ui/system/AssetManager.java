package com.jarlure.ui.system;

import com.jme3.app.Application;
import com.jme3.asset.AssetKey;
import com.jme3.audio.AudioData;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

public final class AssetManager {

    private static com.jme3.asset.AssetManager instance;

    /**
     * 使用前需要调用该方法对其初始化
     *
     * @param app 系统
     */
    public static void initialize(Application app) {
        instance = app.getAssetManager();
    }

    public static void cleanup(Application app) {
        instance = null;
    }

    public static MaterialDef getGuiMaterialDef() {
        return instance.loadAsset(new AssetKey<>("MatDefs/Gui.j3md"));
    }

    public static MaterialDef getUnshadedMaterialDef() {
        return instance.loadAsset(new AssetKey<>("Common/MatDefs/Misc/Unshaded.j3md"));
    }

    public static Object loadAsset(String name) {
        return instance.loadAsset(name);
    }

    public static Texture loadTexture(String name){
        return instance.loadTexture(name);
    }

    public Spatial loadModel(String name){
        return instance.loadModel(name);
    }

    public Material loadMaterial(String name){
        return instance.loadMaterial(name);
    }

    public AudioData loadAudio(String name){
        return instance.loadAudio(name);
    }

}
