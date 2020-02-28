package component.imghandler;

import com.jarlure.ui.util.ImageHandler;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

public class DrawCombine {

    public static void main(String[] args) {
        Image img1 = ImageHandler.loadImage("C:\\Users\\Administrator\\Desktop\\半透明图.png");
        Image img2 = ImageHandler.clone(img1);
        ImageHandler.drawColor(img2, ColorRGBA.White);
        ImageHandler.drawCombine(img2,0,0,img1);
        ImageHandler.saveImage(img2,"C:\\Users\\Administrator\\Desktop\\output.png");
    }

}
