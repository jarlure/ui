package com.jarlure.ui.system;

import com.jme3.app.Application;
import com.jme3.asset.AssetKey;
import com.jme3.material.MaterialDef;

public class AssetManager {

    private static com.jme3.asset.AssetManager instance;

    /**
     * 使用前需要调用该方法对其初始化
     * @param app   系统
     */
    public static void initialize(Application app){
        instance=app.getAssetManager();
    }

    public static void cleanup(Application app){
        instance=null;
    }

    public static MaterialDef getGuiMaterialDef() {
//        MaterialDef matDef = instance.loadAsset(new AssetKey<>("Common/MatDefs/Gui/Gui.j3md"));
        MaterialDef matDef = instance.loadAsset(new AssetKey<>("MatDefs/Gui.j3md"));
        return matDef;
    }

    public static MaterialDef getUnshadedMaterialDef(){
        MaterialDef matDef = instance.loadAsset(new AssetKey<>("Common/MatDefs/Misc/Unshaded.j3md"));
        return matDef;
    }

    public static Object loadAsset(String url){
        Object data = instance.loadAsset(url);
        return data;
    }

}
