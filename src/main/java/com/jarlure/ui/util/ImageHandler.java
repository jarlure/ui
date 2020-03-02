package com.jarlure.ui.util;

import android.graphics.*;
import android.graphics.Canvas;
import android.text.TextPaint;
import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.bean.Font;
import com.jarlure.ui.lambda.IntFunction2Int;
import com.jme3.math.ColorRGBA;
import com.jme3.system.JmeSystem;
import com.jme3.system.android.JmeAndroidSystem;
import com.jme3.texture.Image;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ImageHandler {

    private static final Logger LOG = Logger.getLogger(ImageHandler.class.getName());
    public static final float INV_255 = 1/255f;

    /**
     * 从本地磁盘加载图片
     *
     * @param path 图片路径。相对路径从src开始，例如：src/main/java/resources/图片.png；绝对路径从磁盘开始，例如：
     *             D:/IdeaProjects/项目名/src/main/java/resources/图片.png
     * @return 图片数据
     */
    public static Image loadImage(String path) {
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                return WindowsHelper.loadImage(path);
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                return AndroidHelper.loadImage(path);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 保存图片到本地磁盘
     *
     * @param img  要保存的图片
     * @param path 图片路径。相对路径从src开始，例如：src/main/java/resources/图片.png；绝对路径从磁盘开始，例如：
     *             D:/IdeaProjects/项目名/src/main/java/resources/图片.png
     */
    public static void saveImage(Image img, String path) {
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                WindowsHelper.saveImage(img, path);
                break;
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                AndroidHelper.saveImage(img, path);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 创建一张图片，该图片的颜色值为透明色（=ColorRGBA.BlackNoAlpha）
     *
     * @param width  图片的宽度
     * @param height 图片的高度
     * @return 透明图片
     */
    public static Image createEmptyImage(int width, int height) {
        Image.Format format = Image.Format.RGBA8;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * format.getBitsPerPixel() / 8);
        return new Image(format, width, height, buffer, ColorSpace.Linear);
    }

    /**
     * 深拷贝图片。通过该方法得到的图片不会引用原图片的数据。若需要浅拷贝，请直接使用Image.clone()
     *
     * @param img 要拷贝的原图片
     * @return 原图片的复制品
     */
    public static Image clone(Image img) {
        ByteBuffer buffer = BufferUtils.clone(img.getData(0));
        return new Image(img.getFormat(), img.getWidth(), img.getHeight(), buffer, ColorSpace.Linear);
    }

    /**
     * 使用游程长度编码算法对图片进行压缩。底层是纯Java代码，Android上使用可能耗时较高（Android5.0耗时1s）
     *
     * @param img 要压缩的图片
     * @param compressionRateThreshold 最高压缩比。若压缩比超过该值则不进行压缩。范围为[0,1]。0表示不进行压缩；1表示无此限制
     * @return 压缩后的图片数据
     */
    public static byte[] compress(Image img,float compressionRateThreshold){
        return CompressHelper.compress(img.getData(0),img.getWidth(),img.getHeight(),compressionRateThreshold);
    }

    /**
     * 对使用了游程长度编码算法进行压缩的图片进行解压缩。底层是纯Java代码，Android上使用可能耗时较高（Android5.0耗时0.5s）
     *
     * @param data 压缩的图片数据
     * @param width         图片的宽度
     * @param height        图片的高度
     * @return 解压缩后的图片数据
     */
    public static Image decompress(byte[] data,int width,int height){
        ByteBuffer data1 = CompressHelper.decompress(data,width,height);
        return new Image(Image.Format.RGBA8,width,height,data1,ColorSpace.Linear);
    }

    /**
     * 判断图片是否存在透明值。该算法采用点阵式抽样检查（只检查索引值为偶数的行和列）
     * @param img   图片
     * @return  true如果图片具有透明值；false如果图片不存在透明值
     */
    public static boolean existAlpha(Image img){
        ByteBuffer data = img.getData(0);
        byte[] line = new byte[4*img.getWidth()];
        data.position(0);
        int i,j;
        for (i=0;i<img.getHeight();i+=2){
            data.get(line);
            for (j=3;j<line.length;j+=8){
                if (line[j]!=-1)return true;
            }
        }
        return false;
    }

    /**
     * 给图片涂色。该图片将完全被涂成给定的颜色
     *
     * @param img   要上色的图片
     * @param color 要涂的颜色
     */
    public static void drawColor(Image img, ColorRGBA color) {
        byte r = toByte(color.r);
        byte g = toByte(color.g);
        byte b = toByte(color.b);
        byte a = toByte(color.a);
        byte[] array = new byte[4*img.getWidth()];
        for (int i=0;i<array.length;){
            array[i++]=r;
            array[i++]=g;
            array[i++]=b;
            array[i++]=a;
        }
        ByteBuffer data =img.getData(0);
        data.position(0);
        for (int i=0;i<img.getHeight();i++){
            data.put(array);
        }
        img.setUpdateNeeded();
    }

    /**
     * 给图片上指定区域涂色。该图片上指定区域将被涂成给定的颜色
     *
     * @param img    要上色的图片
     * @param color  要涂的颜色
     * @param startX 涂色起始位置水平坐标x值（包含）
     * @param startY 涂色起始位置水平坐标y值（包含）
     * @param endX   涂色终止位置水平坐标x值（不包含）
     * @param endY   涂色终止位置水平坐标y值（不包含）
     */
    private static void drawColor(Image img,ColorRGBA color,int startX,int startY,int endX,int endY){
        byte r = toByte(color.r);
        byte g = toByte(color.g);
        byte b = toByte(color.b);
        byte a = toByte(color.a);
        byte[] array = new byte[4*(endX-startX)];
        for (int i=0;i<array.length;){
            array[i++]=r;
            array[i++]=g;
            array[i++]=b;
            array[i++]=a;
        }
        int line = 4*img.getWidth();
        ByteBuffer data =img.getData(0);
        int pos = startY*line+4*startX;
        for (int i=startY;i<endY;i++){
            data.position(pos);
            data.put(array);
            pos+=line;
        }
        img.setUpdateNeeded();
    }

    public static void drawCombine(Image des, int pasteStartX, int pasteStartY, Image src) {
        drawCombine(des,pasteStartX,pasteStartY,src,0,0,src.getWidth(),src.getHeight());
    }

    /**
     * 对另一张图片进行裁剪，然后绘制到这张图片上。这种方式会处理半透明叠加效果，Android上使用可能耗时较高（Android5.0耗时1s）
     * @param des         粘贴目标。将对该图片进行“粘贴”。即裁剪后的图片会绘制到这张图片上
     * @param pasteStartX 相对于des的粘贴起始位置水平坐标x值
     * @param pasteStartY 相对于des的粘贴起始位置垂直坐标y值
     * @param src         剪切源。将对该图片进行“裁剪”。实际上是选中该图片的部分区域
     * @param cutStartX   相对于src的裁剪起始位置水平坐标x值（包含）
     * @param cutStartY   相对于src的裁剪起始位置垂直坐标y值（包含）
     * @param cutEndX     相对于src的裁剪终止位置水平坐标x值（不包含）
     * @param cutEndY     相对于src的裁剪终止位置垂直坐标y值（不包含）
     */
    public static void drawCombine(Image des, int pasteStartX, int pasteStartY, Image src, int cutStartX, int cutStartY, int cutEndX, int cutEndY) {
        if (cutStartX<0) cutStartX=0;
        if (cutStartY<0) cutStartY=0;
        if (cutEndX>src.getWidth()) cutEndX=src.getWidth();
        if (cutEndY>src.getHeight()) cutEndY=src.getHeight();
        if (cutStartX>=cutEndX)return;
        if (cutStartY>=cutEndY)return;
        int srcLine=4*src.getWidth();
        int srcStartPos=cutStartY*srcLine;
        int srcOffset = 4*cutStartX;
        cutEndX = Math.min(pasteStartX+cutEndX-cutStartX,des.getWidth());
        cutEndY = Math.min(pasteStartY+cutEndY-cutStartY,des.getHeight());
        cutStartX = Math.max(pasteStartX,0);
        cutStartY = Math.max(pasteStartY,0);
        int cutWidth = cutEndX - cutStartX;
        if (cutWidth<0)return;
        int cutHeight = cutEndY - cutStartY;
        if (cutHeight<0)return;
        byte[] store = new byte[4*cutWidth];
        int desLine=4*des.getWidth();
        int desStartPos = cutStartY*desLine;
        int desOffset = 4*cutStartX;
        ByteBuffer desData = des.getData(0);
        ByteBuffer srcData = src.getData(0);
        byte[] desStore=new byte[store.length];
        int i,j;
        byte a;
        float a1,a2;
        for (i=0;i<cutHeight;i++){
            srcData.position(srcStartPos+srcOffset);
            srcData.get(store);
            desData.position(desStartPos+desOffset);
            desData.get(desStore);
            for (j=3;j<store.length;j+=4){
                a = store[j];
                if (a==-1)continue;//不透明
                else if (a==0){//透明
                    j-=3;
                    store[j]=desStore[j];
                    j++;
                    store[j]=desStore[j];
                    j++;
                    store[j]=desStore[j];
                    j++;
                    store[j]=desStore[j];
                }else{//半透明
                    a1=(a & 0xff)*INV_255;
                    a2=(1-a1)*(desStore[j] & 0xff)*INV_255;
                    j-=3;
                    store[j] = (byte) ((store[j] & 0xff) * a1 + (desStore[j] & 0xff) * a2);
                    j++;
                    store[j] = (byte) ((store[j] & 0xff) * a1 + (desStore[j] & 0xff) * a2);
                    j++;
                    store[j] = (byte) ((store[j] & 0xff) * a1 + (desStore[j] & 0xff) * a2);
                    j++;
                    store[j]= (byte) (255 * (a1+a2));
                }
            }
            desData.position(desStartPos+desOffset);
            desData.put(store);
            srcStartPos+=srcLine;
            desStartPos+=desLine;
        }
        des.setUpdateNeeded();
    }

    public static void drawCut(Image des,Image src){
        if (des.getWidth()==src.getWidth()&&des.getHeight()==src.getHeight()){
            byte[] store=new byte[4*des.getWidth()];
            ByteBuffer desData = des.getData(0);
            ByteBuffer srcData = src.getData(0);
            desData.position(0);
            srcData.position(0);
            for (int y=0;y<des.getHeight();y++){
                srcData.get(store);
                desData.put(store);
            }
            des.setUpdateNeeded();
        }else{
            drawCut(des,0,0,src,0,0,src.getWidth(),src.getHeight());
        }
    }

    /**
     * 对另一张图片进行裁剪，然后绘制到这张图片上。
     *
     * @param des         粘贴目标。将对该图片进行“粘贴”。即裁剪后的图片会绘制到这张图片上
     * @param pasteStartX 相对于des的粘贴起始位置水平坐标x值
     * @param pasteStartY 相对于des的粘贴起始位置垂直坐标y值
     * @param src         剪切源。将对该图片进行“裁剪”。实际上是选中该图片的部分区域
     * @param cutStartX   相对于src的裁剪起始位置水平坐标x值（包含）
     * @param cutStartY   相对于src的裁剪起始位置垂直坐标y值（包含）
     * @param cutEndX     相对于src的裁剪终止位置水平坐标x值（不包含）
     * @param cutEndY     相对于src的裁剪终止位置垂直坐标y值（不包含）
     */
    public static void drawCut(Image des, int pasteStartX, int pasteStartY, Image src, int cutStartX, int cutStartY, int cutEndX, int cutEndY) {
        if (cutStartX<0) cutStartX=0;
        if (cutStartY<0) cutStartY=0;
        if (cutEndX>src.getWidth()) cutEndX=src.getWidth();
        if (cutEndY>src.getHeight()) cutEndY=src.getHeight();
        if (cutStartX>=cutEndX)return;
        if (cutStartY>=cutEndY)return;
        int srcLine=4*src.getWidth();
        int srcStartPos=cutStartY*srcLine;
        int srcOffset = 4*cutStartX;
        cutEndX = Math.min(pasteStartX+cutEndX-cutStartX,des.getWidth());
        cutEndY = Math.min(pasteStartY+cutEndY-cutStartY,des.getHeight());
        cutStartX = Math.max(pasteStartX,0);
        cutStartY = Math.max(pasteStartY,0);
        int cutWidth = cutEndX - cutStartX;
        if (cutWidth<0)return;
        int cutHeight = cutEndY - cutStartY;
        if (cutHeight<0)return;
        byte[] store = new byte[4*cutWidth];
        int desLine=4*des.getWidth();
        int desStartPos = cutStartY*desLine;
        int desOffset = 4*cutStartX;
        ByteBuffer desData = des.getData(0);
        ByteBuffer srcData = src.getData(0);
        for (int i=0;i<cutHeight;i++){
            srcData.position(srcStartPos+srcOffset);
            srcData.get(store);
            desData.position(desStartPos+desOffset);
            desData.put(store);
            srcStartPos+=srcLine;
            desStartPos+=desLine;
        }
        des.setUpdateNeeded();
    }

    /**
     * 给图片绘制文本
     *
     * @param img    文本将会往这张图片上绘制
     * @param font   字体数据
     * @param text   文本数据
     * @param startX 文本左边界
     * @param startY 文本底边界
     * @param endX   文本右边界
     * @param endY   文本顶边界
     * @param align  文本对齐方式
     * @return 文本中每个字符对应的起始位置坐标
     */
    public static int[] drawFont(Image img, com.jarlure.ui.bean.Font font, String text, int startX, int startY, int endX, int endY, Direction align) {
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                return WindowsHelper.drawFont(img,font,text,startX,startY,endX,endY,align);
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                return AndroidHelper.drawFont(img,font,text,startX,startY,endX,endY,align);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 将两张图片连接到一起形成一张新图片。若有重叠部分，img2一定会遮挡img1。若有空白部分，
     * 则空白部分的颜色值为透明色（=ColorRGBA.BlackNoAlpha）
     *
     * @param img1     一张图片
     * @param startX img2相对于img1的位置水平坐标x值
     * @param startY img2相对于img1的位置垂直坐标y值
     * @param img2     另一张图片
     * @return 合并后的图片
     */
    public static Image combine(Image img1, int startX, int startY,Image img2) {
        int left = Math.min(startX,0);
        int right = Math.max(img2.getWidth()+startX,img1.getWidth());
        int bottom = Math.min(startY,0);
        int top = Math.max(img2.getHeight()+startY,img1.getHeight());
        int width = right-left;
        int height = top-bottom;
        if (width==img1.getWidth() && height==img1.getHeight()){
            ByteBuffer data = BufferUtils.clone(img1.getData(0));
            Image result = new Image(Image.Format.RGBA8,width,height,data,ColorSpace.Linear);
            drawCombine(result,startX,startY,img2);
            return result;
        }
        if (width==img2.getWidth() && height==img2.getHeight()){
            ByteBuffer data = BufferUtils.clone(img2.getData(0));
            Image result = new Image(Image.Format.RGBA8,width,height,data,ColorSpace.Linear);
            drawCombine(result,-startX,-startY,img1);
            return result;
        }
        Image result = createEmptyImage(width,height);
        drawCombine(result,-left,-bottom,img1);
        drawCombine(result,startX-left,startY-bottom,img2);
        return result;
    }

    /**
     * 裁剪图片
     *
     * @param src    要裁剪的图片
     * @param startX 裁剪起始位置水平坐标x值
     * @param startY 裁剪起始位置垂直坐标y值
     * @param width  裁剪下来的图片宽度
     * @param height 裁剪下来的图片高度
     * @return 裁剪下来的图片
     */
    public static Image cut(Image src, int startX, int startY, int width, int height) {
        Image result = createEmptyImage(width,height);
        drawCut(result,0,0,src,startX,startY,width,height);
        return result;
    }

    /**
     * 读取并裁剪点九图特有数据边缘，也可用来判断一张图片是否为点九图。该方法实际是通过检查图片边缘像素是否存在至少一条边
     * 有且仅有黑色(0,0,0,1)和透明色(?,?,?,0)做出判断的，因此会存在误判。
     *
     * @param imgDot9       点九图。也可以是普通图片
     * @param edgeInfoStore 长度为4，分别存储水平拉伸、水平文本填充范围、垂直拉伸、垂直文本填充范围信息。可以为null
     * @return null如果img不是点九图；否则返回去除点九图特有边缘的图片
     */
    public static Image cutNinePatchImage(Image imgDot9, boolean[][] edgeInfoStore) {
        if (edgeInfoStore == null) edgeInfoStore = new boolean[4][];
        edgeInfoStore[0] = NinePatchHelper.readHorizontalPixel(imgDot9.getHeight() - 1, imgDot9);
        edgeInfoStore[1] = NinePatchHelper.readHorizontalPixel(0, imgDot9);
        edgeInfoStore[2] = NinePatchHelper.readVerticalPixel(0, imgDot9);
        edgeInfoStore[3] = NinePatchHelper.readVerticalPixel(imgDot9.getWidth() - 1, imgDot9);
        if (!NinePatchHelper.isNinePatchImage(edgeInfoStore)) return null;

        Image src = NinePatchHelper.cutEdge(imgDot9, edgeInfoStore[0], edgeInfoStore[1], edgeInfoStore[2], edgeInfoStore[3]);
        edgeInfoStore[0] = NinePatchHelper.resize(edgeInfoStore[0], edgeInfoStore[2], edgeInfoStore[3]);
        edgeInfoStore[1] = NinePatchHelper.resize(edgeInfoStore[1], edgeInfoStore[2], edgeInfoStore[3]);
        edgeInfoStore[2] = NinePatchHelper.resize(edgeInfoStore[2], edgeInfoStore[1], edgeInfoStore[0]);
        edgeInfoStore[3] = NinePatchHelper.resize(edgeInfoStore[3], edgeInfoStore[1], edgeInfoStore[0]);
        return src;
    }

    /**
     * 将用于ColorRGBA中的float值转换为用于ByteBuffer中的byte值
     *
     * @param value 用于ColorRGBA中的float值
     * @return 用于ByteBuffer中的byte值
     */
    public static byte toByte(float value) {
        if (value <= 0) return 0;
        if (value >= 1) return (byte) 255;
        return (byte) (value * 255 + 0.5f);
    }

    /**
     * 将用于ByteBuffer中的byte值转换为用于ColorRGBA中的float值
     *
     * @param value 用于ByteBuffer中的byte值
     * @return 用于ColorRGBA中的float值
     */
    public static float toFloat(byte value) {
        return (value & 0xff) * INV_255;
    }

    private static class WindowsHelper {

        private static Image loadImage(String path){
            try {
                BufferedImage source = ImageIO.read(new File(path));
                int height = source.getHeight();
                int width = source.getWidth();
                ByteBuffer data = BufferUtils.createByteBuffer(width * height * 4);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int ny = height - y - 1;
                        int rgb = source.getRGB(x, ny);
                        byte a = (byte) ((rgb & 0xFF000000) >> 24);
                        byte r = (byte) ((rgb & 0x00FF0000) >> 16);
                        byte g = (byte) ((rgb & 0x0000FF00) >> 8);
                        byte b = (byte) ((rgb & 0x000000FF));
                        data.put(r).put(g).put(b).put(a);
                    }
                }
                data.flip();
                return new Image(Image.Format.RGBA8, width, height, data, ColorSpace.Linear);
            }catch (IOException e) {
                return null;
            }
        }

        private static void saveImage(Image img,String path){
            int height = img.getHeight();
            int width = img.getWidth();
            BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            ByteBuffer data = img.getData(0);
            data.position(0);
            int rgba;
            for (int flipY=height-1;flipY>=0;flipY--){
                for (int x=0;x<width;x++){
                    rgba=((data.get() & 0xFF) << 16)
                            | ((data.get() & 0xFF) << 8)
                            | ((data.get() & 0xFF))
                            | ((data.get() & 0xFF) << 24);
                    bimg.setRGB(x,flipY,rgba);
                }
            }
            //输出图片
            try {
                File file = new File(path);
                ImageIO.write(bimg, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static int[] drawFont(Image img, Font font, String text, int startX, int startY, int endX, int endY, Direction align) {
            java.awt.Font awtFont = new java.awt.Font(font.getName(), font.getStyle(), font.getSize());
            //计算绘制文字需要的最小尺寸
            int bimgHeight = Math.min((int) Math.ceil(1.1f * font.getSize()), endY - startY);
            int lineWidth = endX - startX;
            int maxRow = (endY - startY) / bimgHeight;
            int bimgWidth = Math.min((1 + text.length()) * font.getSize(), lineWidth * maxRow + 1);
            BufferedImage bimg = new BufferedImage(bimgWidth, bimgHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) bimg.getGraphics();
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            Shape shape = awtFont.createGlyphVector(fontRenderContext, "—|j").getOutline();
            Rectangle2D bounds = shape.getBounds2D();
            double boundsX = 0 - bounds.getMinX() + font.getOutlineWidth();
            double boundsY = 0 - (bounds.getMinY() + bounds.getHeight()) + bimgHeight - font.getOutlineWidth();
            GlyphVector glyphVector = awtFont.createGlyphVector(fontRenderContext, text);
            shape = glyphVector.getOutline();
            g.translate(boundsX, boundsY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            if (font.getOutlineWidth() > 0 && font.getOutlineColor() != null) {
                //drawOutLine
                g.setColor(new java.awt.Color(font.getOutlineColor().asIntARGB()));
                g.setStroke(new BasicStroke(font.getOutlineWidth()));
                g.draw(shape);
            }
            //drawText
            g.setColor(new java.awt.Color(font.getColor().asIntARGB()));
            g.fill(shape);
            //endDraw
            g.dispose();

            float[] position = glyphVector.getGlyphPositions(0, glyphVector.getNumGlyphs() + 1, null);

            return FontHelper.drawFont(img,text.length(),startX,startY,endX,endY,align,maxRow,bimgWidth,bimgHeight,position, bimg::getRGB);
        }

    }

    private static class AndroidHelper {

        private static Image loadImage(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(1, -1); // 翻转
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            int bufferRows = width*4;
            int bufferSize = bufferRows*height;
            ByteBuffer data = BufferUtils.createByteBuffer(bufferSize);
            bitmap.copyPixelsToBuffer(data);
            data.flip();
            return new Image(Image.Format.RGBA8, width, height, data, ColorSpace.Linear);
        }

        private static void saveImage(Image img, String path) {
            ByteBuffer data = img.getData(0);
            data.rewind();
            Bitmap bitmap = Bitmap.createBitmap(img.getWidth(),img.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(data);
            Matrix matrix = new Matrix();
            matrix.postScale(1, -1); // 翻转
            bitmap = Bitmap.createBitmap(bitmap,0, 0, img.getWidth(), img.getHeight(), matrix, true);
            FileOutputStream outStream = null;
            try{
                outStream = new FileOutputStream(path);
                if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
                } else if (path.endsWith(".png")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outStream);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (outStream != null) {
                        outStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static int[] drawFont(Image img, Font font, String text, int startX, int startY, int endX, int endY, Direction align) {
            int bimgHeight = Math.min((int) Math.ceil(1.1f * font.getSize()), endY - startY);
            int lineWidth = endX - startX;
            int maxRow = (endY - startY) / bimgHeight;
            int bimgWidth = Math.min((1 + text.length()) * font.getSize(), lineWidth * maxRow + 1);
            Bitmap bimg = Bitmap.createBitmap(bimgWidth, bimgHeight, Bitmap.Config.ARGB_8888);
            TextPaint textPaint = new TextPaint();
            //设置抗锯齿
            textPaint.setAntiAlias(true);
            //设置字体
            Typeface typeface;
            if (null==font.getName() || font.getName().isEmpty()){
                typeface=Typeface.DEFAULT;
            }else {
                typeface = Typeface.createFromAsset(JmeAndroidSystem.getView().getContext().getAssets(), font.getName());
            }
            textPaint.setTypeface(typeface);
            //设置字体尺寸
            textPaint.setTextSize(font.getSize());
            //设置字体样式
            if (font.getStyle()==Typeface.BOLD){
                textPaint.setFakeBoldText(true);
            }
            //设置字体颜色
            textPaint.setColor(font.getColor().asIntARGB());
            //计算字体包围盒
            Rect bounds = new Rect();
            textPaint.getTextBounds("—|j",0,3,bounds);
            Canvas canvas = new Canvas(bimg);
            canvas.drawText(text,- bounds.left,bimg.getHeight()- bounds.bottom,textPaint);

            float[] position;{
                position=new float[2*text.length()+2];
                for (int i=1,j=2;i<=text.length();i++){
                    position[j++] = textPaint.measureText(text,0,i);
                    position[j++] = 0;
                }
            }

            return FontHelper.drawFont(img,text.length(),startX,startY,endX,endY,align,maxRow,bimgWidth,bimgHeight,position, bimg::getPixel);
        }

    }

    private static class CompressHelper {

        private static final byte RAW = 0;
        private static final byte RLE = 1;

        private static byte[] compress(ByteBuffer data, int width, int height,float compressionRateThreshold){
            byte[] result = new byte[1+4*width*height];
            if (compressionRateThreshold>0){
                try{
                    int pos = 1;
                    result[0]=RLE;
                    data.position(0);
                    byte[] line = new byte[4*width];
                    for (int row=0;row<height;row++){
                        data.get(line);
                        pos = writeALineCompressedByRLE(line,0,result,pos);
                        pos = writeALineCompressedByRLE(line,1,result,pos);
                        pos = writeALineCompressedByRLE(line,2,result,pos);
                        pos = writeALineCompressedByRLE(line,3,result,pos);
                    }
                    if (pos>data.limit()*compressionRateThreshold){
                        LOG.log(Level.WARNING,"RLE压缩率超过给定的最大压缩率，正在进行无压缩存储数据...");
                    }else{
                        byte[] array = new byte[pos];
                        System.arraycopy(result,0,array,0,pos);
                        return array;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    LOG.log(Level.WARNING,"RLE压缩出现异常，正在进行无压缩存储数据...");
                }
            }
            result[0]=RAW;
            data.position(0);
            data.get(result,1,result.length-1);
            return result;
        }

        private static ByteBuffer decompress(byte[] data,int width,int height){
            ByteBuffer result = BufferUtils.createByteBuffer(4*width*height);
            int pos=1;
            if (RLE==data[0]){
                byte[] line = new byte[4*width];
                for (int row=0;row<height;row++){
                    pos = readALineCompressedByRLE(line,0,data,pos);
                    pos = readALineCompressedByRLE(line,1,data,pos);
                    pos = readALineCompressedByRLE(line,2,data,pos);
                    pos = readALineCompressedByRLE(line,3,data,pos);
                    result.put(line);
                }
                result.flip();
            }else{
                result.put(data,1,data.length-1);
            }
            return result;
        }

        private static int writeALineCompressedByRLE(byte[] line,int index,byte[] result,int pos){
            byte lastColor;
            byte color = line[index];
            index+=4;
            boolean equalsLastColor;
            boolean repeat=false;
            byte count=1;
            int numPos=0;
            for (;index<line.length;index+=4){
                lastColor=color;
                color=line[index];
                equalsLastColor=lastColor==color;
                if (repeat){
                    if (equalsLastColor) {
                        if (count==Byte.MAX_VALUE){
                            repeat=false;
                            count=1;
                            result[pos++]=(byte) -Byte.MAX_VALUE;
                            result[pos++]=lastColor;
                        }else count++;
                    } else {
                        repeat=false;
                        result[pos++]=(byte) -count;
                        result[pos++]=lastColor;
                        count=1;
                    }
                }else{
                    if (equalsLastColor) {
                        if (count>1){
                            result[numPos]=(byte) (count-1);
                        }
                        repeat=true;
                        count=2;
                    } else {
                        if (count==Byte.MAX_VALUE){
                            result[numPos]=Byte.MAX_VALUE;
                            result[pos++]=lastColor;
                            count=1;
                        }else {
                            if (count==1){
                                numPos=pos;
                                pos++;
                            }
                            result[pos++]=lastColor;
                            count++;
                        }
                    }
                }
            }
            //最后一次写入
            if (repeat){
                result[pos++]=(byte) -count;
                result[pos++]=color;
            }else{
                if (count==1){
                    result[pos++]=count;
                    result[pos++]=color;
                }else{
                    result[numPos]=count;
                    result[pos++]=color;
                }
            }
            return pos;
        }

        private static int readALineCompressedByRLE(byte[] line,int index,byte[] data,int pos){
            int count;
            byte value;
            while (index<line.length){
                count = data[pos++];
                if (count<0){
                    count=-count;
                    value=data[pos++];
                    for (int i=0;i<count;i++){
                        line[index]=value;
                        index+=4;
                    }
                }else{
                    for (int i=0;i<count;i++){
                        line[index]=data[pos++];
                        index+=4;
                    }
                }
            }
            return pos;
        }

    }

    private static class FontHelper {

        private static int[] drawFont(Image img, int textLength, int startX, int startY, int endX, int endY, Direction align, int maxRow, int bimgWidth, int bimgHeight, float[] position, IntFunction2Int bimgRaster) {
            int lineWidth = endX - startX;
            int vAlign = align.getIndex();
            int hAlign = vAlign % 3;
            //截掉超出的部分
            for (int i = position.length - 2; i >= 0; i -= 2) {
                if (position[i] > bimgWidth) continue;
                bimgWidth = (int) position[i];
                break;
            }
            if (maxRow == 1 || bimgWidth <= lineWidth) {//单行文本
                if (hAlign == 2) startX = (startX + endX - bimgWidth) / 2;//左右居中
                else if (hAlign == 0) startX = endX - bimgWidth;//右对齐
                if (vAlign > 6) startY = endY - bimgHeight;//顶部对齐
                else if (vAlign > 3) startY = (startY + endY - bimgHeight) / 2;//上下居中
                drawFont(img, startX, startY, endX, endY, bimgHeight, bimgRaster, 0, bimgWidth);
                int[] positionInImg = new int[position.length];
                for (int i = 0; i < position.length; i += 2) {
                    positionInImg[i] = startX + (int) position[i];
                    positionInImg[i + 1] = startY;
                }
                img.setUpdateNeeded();
                return positionInImg;
            } else {//多行文本
                int row = 1;
                int[] bimgX = new int[textLength];
                for (int i = 2, x = 0; i < position.length; i += 2) {
                    if (position[i] - x <= lineWidth) bimgX[row] = (int) position[i];
                    else {
                        x = bimgX[row];
                        row++;
                    }
                }
                if (row > maxRow) row = maxRow;
                int[] lineStartX = new int[row];
                int[] lineStartY = new int[row];
                int[] lineEndX = new int[row];
                int[] lineEndY = new int[row];
                if (hAlign == 2) {//左右居中
                    for (int i = 0; i < row; i++) {
                        lineStartX[i] = (startX + endX - (bimgX[i + 1] - bimgX[i])) / 2;
                        lineEndX[i] = endX;
                    }
                } else if (hAlign == 0) {//右对齐
                    for (int i = 0; i < row; i++) {
                        lineStartX[i] = endX - (bimgX[i + 1] - bimgX[i]);
                        lineEndX[i] = endX;
                    }
                } else {//左对齐
                    for (int i = 0; i < row; i++) {
                        lineStartX[i] = startX;
                        lineEndX[i] = endX;
                    }
                }
                if (vAlign > 6) {//顶部对齐
                    for (int i = 0; i < row; i++) {
                        lineEndY[i] = endY - i * bimgHeight;
                        lineStartY[i] = lineEndY[i] - bimgHeight;
                    }
                } else {
                    int y0 = startY;//底部对齐
                    if (vAlign > 3) y0 = (startY + endY - row * bimgHeight) / 2;//上下居中
                    for (int i = 0; i < row; i++) {
                        lineStartY[i] = y0 + (row - 1 - i) * bimgHeight;
                        lineEndY[i] = lineStartY[i] + bimgHeight;
                    }
                }
                for (int i = 0; i < row; i++) {
                    drawFont(img, lineStartX[i], lineStartY[i], lineEndX[i], lineEndY[i], bimgHeight, bimgRaster, bimgX[i], bimgX[i + 1]);
                }
                int[] positionInImg = new int[2 * textLength + 2 * row];
                for (int i = 0, j = 0, k = 0; i < row; i++) {
                    while (position[j] < bimgX[i + 1]) {
                        positionInImg[k++] = lineStartX[i] + (int) position[j] - bimgX[i];
                        positionInImg[k++] = lineStartY[i];
                        j += 2;
                    }
                    positionInImg[k++] = lineStartX[i] + bimgX[i + 1] - bimgX[i];
                    positionInImg[k++] = lineStartY[i];
                }
                img.setUpdateNeeded();
                return positionInImg;
            }
        }

        private static void drawFont(Image img, int startX, int startY, int imgEndX, int imgEndY, int bimgHeight, IntFunction2Int bimgRaster, int bimgStartX, int bimgEndX) {
            int flipY = bimgHeight - 1;
            int bimgEndY = flipY + startY - Math.min(imgEndY, startY + bimgHeight);
            bimgEndX = bimgStartX+Math.min(imgEndX, startX + bimgEndX - bimgStartX)-startX;
            int line = 4 * img.getWidth();
            int pos = startY * line;
            int offset = 4 * startX;
            int r, g, b, a;
            byte[] rgba=new byte[4];
            float a1, a2;
            ByteBuffer data = img.getData(0);
            for (; flipY > bimgEndY; flipY--) {
                data.position(pos + offset);
                for (int x = bimgStartX; x < bimgEndX; x++) {
                    int color = bimgRaster.apply(x, flipY);
                    if (color==0){
                        data.position(data.position()+4);
                        continue;
                    }
                    a = (color >> 24) & 0xFF;
                    r = (color >> 16) & 0xFF;
                    g = (color >> 8) & 0xFF;
                    b = (color) & 0xFF;
                    if (a==255){
                        rgba[0]= (byte) r;
                        rgba[1]= (byte) g;
                        rgba[2]= (byte) b;
                        rgba[3]= (byte) a;
                        data.put(rgba);
                    }else{
                        data.get(rgba);
                        data.position(data.position() - 4);
                        if ((rgba[3]& 0xFF)<13){
                            rgba[0]= (byte) r;
                            rgba[1]= (byte) g;
                            rgba[2]= (byte) b;
                            rgba[3]= (byte) a;
                            data.put(rgba);
                        }else{
                            a1 = a * INV_255;
                            a2 = (1 - a1)*(rgba[3] & 0xff) * INV_255;
                            rgba[0]=(byte) (r * a1 + (rgba[0] & 0xff) * a2);
                            rgba[1]=(byte) (g * a1 + (rgba[1] & 0xff) * a2);
                            rgba[2]=(byte) (b * a1 + (rgba[2] & 0xff) * a2);
                            rgba[3]=(byte) (255 * (a1+a2));
                            data.put(rgba);
                        }
                    }
                }
                pos+=line;
            }
        }

    }

    private static class NinePatchHelper {

        private static boolean[] readVerticalPixel(int x, Image dot9Img) {
            boolean[] result = new boolean[dot9Img.getHeight()];
            ByteBuffer data = dot9Img.getData(0);
            int line=4*dot9Img.getWidth();
            int offset=4*x;
            int pos=0;
            byte r,g,b,a;
            for (int y = 0; y < result.length; y++,pos+=line) {
                data.position(pos+offset);
                r=data.get();
                g=data.get();
                b=data.get();
                a=data.get();
                if (a == 0) continue;
                if (r==0 && g==0 && b==0 && a==-1) result[y] = true;//ColorRGBA.Black
                else return null;
            }
            if (isNinePatchSide(result)) return result;
            return null;
        }

        private static boolean[] readHorizontalPixel(int y, Image dot9Img) {
            boolean[] result = new boolean[dot9Img.getWidth()];
            ByteBuffer data = dot9Img.getData(0);
            data.position(y*4*dot9Img.getWidth());
            byte r,g,b,a;
            for (int x = 0; x < result.length; x++) {
                r=data.get();
                g=data.get();
                b=data.get();
                a=data.get();
                if (a == 0) continue;
                if (r==0 && g==0 && b==0 && a==-1) result[x] = true;//ColorRGBA.Black
                else return null;
            }
            if (isNinePatchSide(result)) return result;
            return null;
        }

        private static boolean isNinePatchSide(boolean[] side) {
            if (side == null) return false;
            boolean color = side[0];
            for (int i = side.length - 1; i > 0; i--) {
                if (color != side[i]) return true;
            }
            return false;
        }

        private static boolean isNinePatchImage(boolean[][] edge) {
            for (boolean[] side : edge) {
                if (side != null) return true;
            }
            return false;
        }

        private static Image cutEdge(Image img, boolean[] topSide, boolean[] bottomSide, boolean[] leftSide, boolean[] rightSide) {
            int startX = 0;
            int startY = 0;
            int endX = img.getWidth();
            int endY = img.getHeight();
            if (topSide != null) endY--;
            if (bottomSide != null) startY++;
            if (leftSide != null) startX++;
            if (rightSide != null) endX--;
            return cut(img,startX,startY,endX-startX,endY-startY);
        }

        private static boolean[] resize(boolean[] side, boolean[] minNeighborSide, boolean[] maxNeighborSide) {
            if (side == null) return null;
            if (minNeighborSide == null && maxNeighborSide == null) return side;
            int length = side.length;
            if (minNeighborSide != null) length--;
            if (maxNeighborSide != null) length--;
            boolean[] result = new boolean[length];
            if (minNeighborSide == null) System.arraycopy(side, 0, result, 0, length);
            else System.arraycopy(side, 1, result, 0, length);
            return result;
        }

    }

}
