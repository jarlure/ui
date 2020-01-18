package com.jarlure.ui.util;

import com.jarlure.ui.bean.Direction;
import com.jarlure.ui.lambda.Function2Int;
import com.jarlure.ui.lambda.Function2Int1Obj;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import com.jme3.util.IntMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class ImageHandler {

    /**
     * 从本地磁盘加载图片
     *
     * @param path 图片路径。相对路径从src开始，例如：src/main/java/resources/图片.png；绝对路径从磁盘开始，例如：
     *             D:/IdeaProjects/project/src/main/java/resources/图片.png
     * @return 图片数据
     */
    public static Image loadImage(String path) {
        return MethodForPC.loadImage(path);
    }

    /**
     * 保存图片到本地磁盘
     *
     * @param img  要保存的图片
     * @param path 图片路径。相对路径从src开始，例如：src/main/java/resources/图片.png；绝对路径从磁盘开始，例如：
     *             D:/IdeaProjects/project/src/main/java/resources/图片.png
     */
    public static void saveImage(Image img, String path) {
        MethodForPC.saveImage(img, path);
    }

    /**
     * 压缩图片
     *
     * @param img 要压缩的图片
     * @return 压缩后的图片数据。其中byte[0]=Red,byte[1]=Green,byte[2]=Blue,byte[3]=Alpha
     */
    public static byte[][] compressImg(Image img) {
        int[][][] channelImg = toChannelRGBAImg(img, true);
        byte[][] compressedImg = new byte[4][];
        compressedImg[0] = CompressionMethod.compress(channelImg[0]);
        compressedImg[1] = CompressionMethod.compress(channelImg[1]);
        compressedImg[2] = CompressionMethod.compress(channelImg[2]);
        compressedImg[3] = CompressionMethod.compress(channelImg[3]);
        return compressedImg;
    }

    /**
     * 解压缩图片
     *
     * @param compressedImg 压缩的图片数据。其中byte[0]=Red,byte[1]=Green,byte[2]=Blue,byte[3]=Alpha
     * @param width         图片的宽度
     * @param height        图片的高度
     * @return 解压缩后的图片数据
     */
    public static Image decompressImg(byte[][] compressedImg, int width, int height) {
        int[][][] channelImg = new int[4][][];
        channelImg[0] = CompressionMethod.decompress(ByteBuffer.wrap(compressedImg[0]), height, width);
        channelImg[1] = CompressionMethod.decompress(ByteBuffer.wrap(compressedImg[1]), height, width);
        channelImg[2] = CompressionMethod.decompress(ByteBuffer.wrap(compressedImg[2]), height, width);
        channelImg[3] = CompressionMethod.decompress(ByteBuffer.wrap(compressedImg[3]), height, width);
        Image img = toImage(channelImg, true);
        return img;
    }

    public static Image toImage(int[][][] channelRGBAImg, boolean flipY) {
        int height = channelRGBAImg[0].length;
        int width = channelRGBAImg[0][0].length;
        Image img = ImageHandler.createEmptyImage(width, height);
        ImageRaster raster = ImageRaster.create(img);
        ColorRGBA color = new ColorRGBA();
        for (int y = 0; y < height; y++) {
            int imgY = flipY ? height - 1 - y : y;
            for (int x = 0; x < width; x++) {
                float r = channelRGBAImg[0][y][x] / 255f;
                float g = channelRGBAImg[1][y][x] / 255f;
                float b = channelRGBAImg[2][y][x] / 255f;
                float a = channelRGBAImg[3][y][x] / 255f;
                color.set(r, g, b, a);
                raster.setPixel(x, imgY, color);
            }
        }
        return img;
    }

    public static int[][][] toChannelRGBAImg(Image img, boolean flipY) {
        int height = img.getHeight();
        int width = img.getWidth();
        int[][][] channelImg = new int[4][img.getHeight()][img.getWidth()];
        ImageRaster raster = ImageRaster.create(img);
        for (int y = 0; y < height; y++) {
            int channelImgY = flipY ? height - 1 - y : y;
            for (int x = 0; x < width; x++) {
                ColorRGBA color = raster.getPixel(x, y);
                channelImg[0][channelImgY][x] = Math.round(255 * color.getRed());
                channelImg[1][channelImgY][x] = Math.round(255 * color.getGreen());
                channelImg[2][channelImgY][x] = Math.round(255 * color.getBlue());
                channelImg[3][channelImgY][x] = Math.round(255 * color.getAlpha());
            }
        }
        return channelImg;
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
        Image img = new Image(format, width, height, buffer, ColorSpace.Linear);
        return img;
    }

    /**
     * 拷贝图片。与Image.clone()相比，通过该方法得到的图片是全新的，与原数据没有任何关系。
     *
     * @param img 要拷贝的原图片
     * @return 原图片的复制品
     */
    public static Image clone(Image img) {
        ByteBuffer buffer = BufferUtils.clone(img.getData(0));
        Image clone = new Image(img.getFormat(), img.getWidth(), img.getHeight(), buffer, ColorSpace.Linear);
        return clone;
    }

    /**
     * 给图片涂色。该图片将完全被涂成给定的颜色
     *
     * @param img   要上色的图片
     * @param color 要涂的颜色
     */
    public static void drawColor(Image img, ColorRGBA color) {
        int width = img.getWidth();
        int height = img.getHeight();
        ImageRaster imgRaster = ImageRaster.create(img);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imgRaster.setPixel(x, y, color);
            }
        }
    }

    /**
     * 根据给定的函数给图片涂色。
     * @param img   要上色的图片
     * @param function  回调函数
     */
    public static void drawColor(Image img, Function2Int<ColorRGBA> function){
        int width = img.getWidth();
        int height = img.getHeight();
        ImageRaster imgRaster = ImageRaster.create(img);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imgRaster.setPixel(x, y, function.apply(x,y));
            }
        }
    }

    /**
     * 根据给定的函数给图片涂色。每次涂色前会将当前该点像素值作为参数传入函数中
     * @param img   要上色的图片
     * @param function  回调函数
     */
    public static void drawColor(Image img, Function2Int1Obj<ColorRGBA,ColorRGBA> function){
        int width = img.getWidth();
        int height = img.getHeight();
        ImageRaster imgRaster = ImageRaster.create(img);
        ColorRGBA color=new ColorRGBA();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imgRaster.getPixel(x,y,color);
                imgRaster.setPixel(x, y, function.apply(x,y,color));
            }
        }
    }

    /**
     * 通过绘制的方式合并图片。这种方式不会创建新图片。
     *
     * @param toImg    一张图片。合并后的图片将会绘制到这张图片上
     * @param fromImg  另一张图片
     * @param offset_x 另一张图片相对于这张图片的位置水平坐标x值
     * @param offset_y 另一张图片相对于这张图片的位置垂直坐标y值
     */
    public static void drawCombine(Image toImg, Image fromImg, int offset_x, int offset_y) {
        int startX = offset_x < 0 ? 0 : offset_x;
        int startY = offset_y < 0 ? 0 : offset_y;
        int endX = offset_x + fromImg.getWidth();
        if (endX > toImg.getWidth()) endX = toImg.getWidth();
        int endY = offset_y + fromImg.getHeight();
        if (endY > toImg.getHeight()) endY = toImg.getHeight();
        ImageRaster toRaster = ImageRaster.create(toImg);
        ImageRaster fromRaster = ImageRaster.create(fromImg);
        ColorRGBA color = new ColorRGBA();
        ColorRGBA A = new ColorRGBA();
        ColorRGBA B = new ColorRGBA();
        for (int y = startY, fromY = 0; y < endY; y++, fromY++) {
            for (int x = startX, fromX = 0; x < endX; x++, fromX++) {
                fromRaster.getPixel(fromX, fromY, color);
                float a = color.getAlpha();
                if (a == 0) continue;
                else if (a < 1) {
                    A.set(color).multLocal(a);
                    toRaster.getPixel(x, y, color);
                    B.set(color).multLocal(color.getAlpha() * (1 - a));
                    color.set(A.r + B.r, A.g + B.g, A.b + B.b, 1 - (1 - a) * (1 - color.a));
                }
                toRaster.setPixel(x, y, color);
            }
        }
    }

    /**
     * 对另一张图片进行裁剪，然后绘制到这张图片上。这种方式不会创建新图片。
     *
     * @param img         一张图片。将对该图片进行“粘贴”。即裁剪后的图片会绘制到这张图片上
     * @param pasteStartX 粘贴位置水平坐标x值。左下角
     * @param pasteStartY 粘贴位置垂直坐标y值。左下角
     * @param src         另一张图片。将对该图片进行“裁剪”。实际上是选中该图片的一些区域
     * @param cutStartX   相对于src的裁剪起始位置水平坐标x值（包含）
     * @param cutStartY   相对于src的裁剪起始位置垂直坐标y值（包含）
     * @param cutEndX     相对于src的裁剪终止位置水平坐标x值（不包含）
     * @param cutEndY     相对于src的裁剪终止位置垂直坐标y值（不包含）
     */
    public static void drawCut(Image img, int pasteStartX, int pasteStartY, Image src, int cutStartX, int cutStartY, int cutEndX, int cutEndY) {
        if (cutStartX < 0) cutStartX = 0;
        if (cutStartY < 0) cutStartY = 0;
        if (cutEndX > src.getWidth()) cutEndX = src.getWidth();
        if (cutEndY > src.getHeight()) cutEndY = src.getHeight();
        ImageRaster imgRaster = ImageRaster.create(img);
        ImageRaster srcRaster = ImageRaster.create(src);
        ColorRGBA color = new ColorRGBA();
        int startX = pasteStartX + cutStartX < 0 ? -pasteStartX : cutStartX;
        int endX = pasteStartX + cutEndX > img.getWidth() ? img.getWidth() - pasteStartX : cutEndX;
        int startY = pasteStartY + cutStartY < 0 ? -pasteStartY : cutStartY;
        int endY = pasteStartY + cutEndY > img.getHeight() ? img.getHeight() - pasteStartY : cutEndY;
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                srcRaster.getPixel(x, y, color);
                imgRaster.setPixel(pasteStartX + x, pasteStartY + y, color);
            }
        }
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
        String name = font.getName();
        int style = font.getStyle();
        int size = font.getSize();
        ColorRGBA color = font.getColor();
        int outlineWidth = font.getOutlineWidth();
        ColorRGBA outlineColor = font.getOutlineColor();

        return MethodForPC.drawFont(img, new Font(name, style, size), text, startX, startY, endX, endY, align, color, outlineWidth, outlineColor);
    }

    /**
     * 合并图片
     *
     * @param img1     一张图片
     * @param img2     另一张图片
     * @param offset_x img2相对于img1的位置水平坐标x值
     * @param offset_y img2相对于img1的位置垂直坐标y值
     * @return 合并后的图片
     */
    public static Image combine(Image img1, Image img2, int offset_x, int offset_y) {
        int img1px = offset_x < 0 ? -offset_x : 0;
        int img1py = offset_y < 0 ? -offset_y : 0;
        int img2px = offset_x > 0 ? offset_x : 0;
        int img2py = offset_y > 0 ? offset_y : 0;
        int width = Math.max(img1px + img1.getWidth(), img2px + img2.getWidth());
        int height = Math.max(img1py + img1.getHeight(), img2py + img2.getHeight());
        Image img = createEmptyImage(width, height);
        ImageRaster imgRaster = ImageRaster.create(img);
        ImageRaster img1Raster = ImageRaster.create(img1);
        ColorRGBA store = new ColorRGBA();
        for (int y = 0; y < img1.getHeight(); y++) {
            int py = y + img1py;
            for (int x = 0; x < img1.getWidth(); x++) {
                img1Raster.getPixel(x, y, store);
                imgRaster.setPixel(x + img1px, py, store);
            }
        }
        ColorRGBA color = new ColorRGBA();
        ColorRGBA A = new ColorRGBA();
        ColorRGBA B = new ColorRGBA();
        ImageRaster img2Raster = ImageRaster.create(img2);
        for (int y = 0; y < img2.getHeight(); y++) {
            int py = y + img2py;
            for (int x = 0; x < img2.getWidth(); x++) {
                img2Raster.getPixel(x, y, color);
                float a = color.getAlpha();
                if (a == 0) continue;
                if (a != 1) {
                    A.set(color).multLocal(a);
                    imgRaster.getPixel(x + img2px, py, color);
                    B.set(color).multLocal(color.getAlpha() * (1 - a));
                    color.set(A.r + B.r, A.g + B.g, A.b + B.b, 1 - (1 - a) * (1 - color.a));
                }
                imgRaster.setPixel(x + img2px, py, color);
            }
        }
        return img;
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
        Image des = createEmptyImage(width, height);
        int endX = Math.min(startX + width, src.getWidth());
        int endY = Math.min(startY + height, src.getHeight());
        ImageRaster srcRaster = ImageRaster.create(src);
        ImageRaster desRaster = ImageRaster.create(des);
        ColorRGBA store = new ColorRGBA();
        int srcX, srcY, desY, desX;
        for (srcY = Math.max(startY, 0); srcY < endY; srcY++) {
            desY = srcY - startY;
            if (desY < 0) continue;
            for (srcX = Math.max(startX, 0); srcX < endX; srcX++) {
                desX = srcX - startX;
                if (desX < 0) continue;
                ColorRGBA color = srcRaster.getPixel(srcX, srcY, store);
                desRaster.setPixel(desX, desY, color);
            }
        }
        return des;
    }

    /**
     * 统计一张图片里的颜色数量。
     *
     * @param img   给定的图片
     * @param store 用于存储颜色及其在图片当中的数量。可以为null
     * @return 颜色排列。颜色数量最多的放在数组[0]、数量第二的放在数组[1]，依次类推。最多到数组[9]
     */
    public static ColorRGBA[] numberOfColors(Image img, Map<ColorRGBA, Integer> store) {
        IntMap<Integer> indexMap = new IntMap<>();
        List<ColorRGBA> colorList = new ArrayList<>();
        List<AtomicInteger> numberList = new ArrayList<>();
        int width = img.getWidth();
        int height = img.getHeight();
        ImageRaster raster = ImageRaster.create(img);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ColorRGBA color = raster.getPixel(x, y);
                int hashCode = color.hashCode();
                Integer index = indexMap.get(hashCode);
                if (index == null) {
                    indexMap.put(hashCode, colorList.size());
                    colorList.add(color);
                    numberList.add(new AtomicInteger(1));
                } else numberList.get(index).getAndIncrement();
            }
        }

        if (store != null) {
            for (int i = 0, length = colorList.size(); i < length; i++) {
                int number = numberList.get(i).get();
                if (length > 100 && number <= 1) continue;
                ColorRGBA color = colorList.get(i);
                store.put(color, number);
            }
        }

        ColorRGBA[] result = new ColorRGBA[Math.min(colorList.size(), 10)];
        for (int i = 0; i < result.length; i++) {
            int index = i;
            int maxNumber = numberList.get(i).get();
            for (int j = 0; j < result.length; j++) {
                int number = numberList.get(j).get();
                if (number > maxNumber) {
                    maxNumber = number;
                    index = j;
                }
            }
            result[i] = colorList.get(index);
            numberList.get(index).set(0);
        }
        return result;
    }

    /**
     * 将RGB颜色转为灰色
     * @param color RGB颜色
     * @return  灰色
     */
    public static int toGray(ColorRGBA color) {
        return Math.round(76.245f * color.r) + Math.round(149.685f * color.g) + Math.round(29.07f * color.b);
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
        ImageRaster raster = ImageRaster.create(imgDot9);
        ColorRGBA store = new ColorRGBA();
        if (edgeInfoStore == null) edgeInfoStore = new boolean[4][];
        edgeInfoStore[0] = NinePatchHelper.readHorizontalPixel(raster.getHeight() - 1, raster, store);
        edgeInfoStore[1] = NinePatchHelper.readHorizontalPixel(0, raster, store);
        edgeInfoStore[2] = NinePatchHelper.readVerticalPixel(0, raster, store);
        edgeInfoStore[3] = NinePatchHelper.readVerticalPixel(raster.getWidth() - 1, raster, store);
        if (!NinePatchHelper.isNinePatchImage(edgeInfoStore)) return null;

        Image src = NinePatchHelper.cutEdge(raster, edgeInfoStore[0], edgeInfoStore[1], edgeInfoStore[2], edgeInfoStore[3], store);
        edgeInfoStore[0] = NinePatchHelper.resize(edgeInfoStore[0], edgeInfoStore[2], edgeInfoStore[3]);
        edgeInfoStore[1] = NinePatchHelper.resize(edgeInfoStore[1], edgeInfoStore[2], edgeInfoStore[3]);
        edgeInfoStore[2] = NinePatchHelper.resize(edgeInfoStore[2], edgeInfoStore[1], edgeInfoStore[0]);
        edgeInfoStore[3] = NinePatchHelper.resize(edgeInfoStore[3], edgeInfoStore[1], edgeInfoStore[0]);
        return src;
    }

    private static class MethodForPC {

        private static Image loadImage(String path) {
            if (path == null) return null;
            try {
                BufferedImage source = ImageIO.read(new File(path));
                int height = source.getHeight();
                int width = source.getWidth();
                if (source.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
                    //提取图片数据
                    DataBuffer buf = source.getRaster().getDataBuffer();
                    DataBufferByte byteBuf = (DataBufferByte) buf;
                    byte[] dataBuf = byteBuf.getData();
                    //反转Y轴
                    int scSz = width * 4;
                    byte[] sln = new byte[scSz];
                    int y2;
                    for (int y1 = 0; y1 < height / 2; y1++) {
                        y2 = height - y1 - 1;
                        System.arraycopy(dataBuf, y1 * scSz, sln, 0, scSz);
                        System.arraycopy(dataBuf, y2 * scSz, dataBuf, y1 * scSz, scSz);
                        System.arraycopy(sln, 0, dataBuf, y2 * scSz, scSz);
                    }
                    ByteBuffer data = com.jme3.util.BufferUtils.createByteBuffer(width * height * 4);
                    data.put(dataBuf);
                    return new Image(Image.Format.ABGR8, width, height, data, ColorSpace.Linear);
                } else {
                    ByteBuffer data = com.jme3.util.BufferUtils.createByteBuffer(width * height * 4);
                    // no alpha
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
                }
            } catch (IOException e) {
                return null;
            }
        }

        private static void saveImage(Image img, String path) {
            int height = img.getHeight();
            int width = img.getWidth();
            BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            ImageRaster raster = ImageRaster.create(img);
            for (int y = 0; y < height; y++) {
                int negy = height - 1 - y;
                for (int x = 0; x < width; x++) {
                    bimg.setRGB(x, y, raster.getPixel(x, negy).asIntARGB());
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

        private static int[] drawFont(Image img, Font font, String text, int startX, int startY, int endX, int endY, Direction align, ColorRGBA fontColor, int outlineWidth, ColorRGBA outlineColor) {
            //计算绘制文字需要的最小尺寸
            int bimgHeight = Math.min((int) Math.ceil(1.1f * font.getSize()), endY - startY);
            int lineWidth = endX - startX;
            int maxRow = (endY - startY) / bimgHeight;
            int bimgWidth = Math.min((1 + text.length()) * font.getSize(), lineWidth * maxRow + 1);
            BufferedImage bimg = new BufferedImage(bimgWidth, bimgHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) bimg.getGraphics();
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            Shape shape = font.createGlyphVector(fontRenderContext, "—|j").getOutline();
            Rectangle2D bounds = shape.getBounds2D();
            double boundsX = 0 - bounds.getMinX() + outlineWidth;
            double boundsY = 0 - (bounds.getMinY() + bounds.getHeight()) + bimgHeight - outlineWidth;
            GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, text);
            shape = glyphVector.getOutline();
            g.translate(boundsX, boundsY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            if (outlineWidth > 0 && outlineColor != null) {
                //drawOutLine
                g.setColor(new Color(outlineColor.asIntARGB()));
                g.setStroke(new BasicStroke(outlineWidth));
                g.draw(shape);
            }
            //drawText
            g.setColor(new Color(fontColor.asIntARGB()));
            g.fill(shape);
            //endDraw
            g.dispose();

            int vAlign = align.getIndex();
            int hAlign = vAlign % 3;
            float[] position = glyphVector.getGlyphPositions(0, glyphVector.getNumGlyphs() + 1, null);
            //截掉超出的部分
            for (int i = position.length - 2; i >= 0; i -= 2) {
                if (position[i] > bimgWidth) continue;
                bimgWidth = (int) position[i];
                break;
            }
            ImageRaster raster = ImageRaster.create(img);
            if (maxRow == 1 || bimgWidth <= lineWidth) {//单行文本
                if (hAlign == 2) startX = (startX + endX - bimgWidth) / 2;//左右居中
                else if (hAlign == 0) startX = endX - bimgWidth;//右对齐
                if (vAlign > 6) startY = endY - bimgHeight;//顶部对齐
                else if (vAlign > 3) startY = (startY + endY - bimgHeight) / 2;//上下居中
                drawFont(raster, startX, startY, endX, endY, bimg, 0, bimgWidth);
                int[] positionInImg = new int[position.length];
                for (int i = 0; i < position.length; i += 2) {
                    positionInImg[i] = startX + (int) position[i];
                    positionInImg[i + 1] = startY;
                }
                return positionInImg;
            } else {//多行文本
                int row = 1;
                int[] bimgX = new int[text.length()];
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
                    drawFont(raster, lineStartX[i], lineStartY[i], lineEndX[i], lineEndY[i], bimg, bimgX[i], bimgX[i + 1]);
                }
                int[] positionInImg = new int[2 * text.length() + 2 * row];
                for (int i = 0, j = 0, k = 0; i < row; i++) {
                    while (position[j] < bimgX[i + 1]) {
                        positionInImg[k++] = lineStartX[i] + (int) position[j] - bimgX[i];
                        positionInImg[k++] = lineStartY[i];
                        j += 2;
                    }
                    positionInImg[k++] = lineStartX[i] + bimgX[i + 1] - bimgX[i];
                    positionInImg[k++] = lineStartY[i];
                }
                return positionInImg;
            }
        }

        @Deprecated
        private static boolean isVerticalTransparent(BufferedImage bimg, int x) {
            ColorModel colorModel = bimg.getColorModel();
            WritableRaster raster = bimg.getRaster();
            for (int y = bimg.getHeight() - 1; y >= 0; y--) {
                int alpha = colorModel.getAlpha(raster.getDataElements(x, y, null));
                if (alpha != 0) return false;
            }
            return true;
        }

        private static void drawFont(ImageRaster img, int startX, int startY, int imgEndX, int imgEndY, BufferedImage bimg, int bimgStartX, int bimgEndX) {
            ColorRGBA color = new ColorRGBA();
            ColorRGBA A = new ColorRGBA();
            ColorRGBA B = new ColorRGBA();
            int endX = Math.min(imgEndX, startX + bimgEndX - bimgStartX);
            int endY = Math.min(imgEndY, startY + bimg.getHeight());
            for (int y = startY, flipY = bimg.getHeight() - 1; y < endY; y++, flipY--) {
                for (int x = startX, bx = bimgStartX; x < endX; x++, bx++) {
                    int colorValue = bimg.getRGB(bx, flipY);
                    if (colorValue == 0) continue;
                    color.fromIntARGB(colorValue);
                    float a = color.getAlpha();
                    if (a < 1) {
                        A.set(color);
                        img.getPixel(x, y, color);
                        if (color.getAlpha() < 0.05f) {//如果背景透明，则不进行混合
                            color.set(A);
                        } else {
                            A.multLocal(a);
                            B.set(color).multLocal(color.getAlpha() * (1 - a));
                            color.set(A.r + B.r, A.g + B.g, A.b + B.b, 1 - (1 - a) * (1 - color.a));
                        }
                    }
                    img.setPixel(x, y, color);
                }
            }
        }

    }

    private static class MethodForAndroid {

    }

    public static class CompressionMethod {

        public enum Compression {
            NULL(-1),
            RAW(0), //未经过压缩处理
            DLE(1), //RLE 压缩方式
            ZIP(2), //ZIP 压缩方式
            ZIP_WITH_PREDICTION(3);//ZIP 压缩方式（with prediction）

            private int index;

            Compression(int index) {
                this.index = index;
            }

            public static Compression get(int index) {
                switch (index) {
                    case 0:
                        return RAW;
                    case 1:
                        return DLE;
                    case 2:
                        return ZIP;
                    case 3:
                        return ZIP_WITH_PREDICTION;
                    default:
                        return NULL;
                }
            }

            public int getIndex() {
                return index;
            }
        }

        public static int[][] decompress(ByteBuffer buffer, int height, int width) {
            Compression compression = Compression.get(buffer.getShort());
            if (height == 0 || width == 0) return null;
            int[][] channelImg = new int[height][width];
            switch (compression) {
                case RAW:
                    return readDataWithoutDecompression(buffer, channelImg);
                case DLE:
                    return readDataDepressedByRLE(buffer, channelImg);
                case ZIP:
                    return null;
                case ZIP_WITH_PREDICTION:
                    return null;
                default:
                    return channelImg;
            }
        }

        public static byte[] compress(int[][] channelImg) {
            int height = channelImg.length;
            int width = channelImg[0].length;
            ByteBuffer buffer = ByteBuffer.wrap(new byte[2 + height * width]);
            try {
                buffer.putShort((short) Compression.DLE.getIndex());
                writeDataCompressedByRLE(buffer, channelImg);
            } catch (BufferOverflowException e) {
                buffer.position(0);
                buffer.putShort((short) Compression.RAW.getIndex());
                writeDataWithoutCompression(buffer, channelImg);
            }
            int length = buffer.position();
            buffer.position(0);
            byte[] result = new byte[length];
            buffer.get(result);
            return result;
        }

        private static int[][] readDataWithoutDecompression(ByteBuffer buffer, int[][] channelImg) {
            int height = channelImg.length;
            int width = channelImg[0].length;
            for (int y = 0; y < height; y++) {
                int[] imgy = channelImg[y];
                for (int x = 0; x < width; x++) {
                    int value = buffer.get();
                    if (value < 0) value += 256;
                    imgy[x] = value;
                }
            }
            return channelImg;
        }

        private static int[][] readDataDepressedByRLE(ByteBuffer buffer, int[][] store) {
            int height = store.length;
            int width = store[0].length;
            int[] count = new int[height];
            for (int i = 0; i < height; i++) {
                int value = buffer.getShort();
                if (value < 0) value += 65536;
                count[i] = value;
            }
            for (int i = 0; i < height; i++) {
                store[i] = readALineOfChannelColor(buffer, count[i], width);
            }
            return store;
        }

        public static int[] readALineOfChannelColor(ByteBuffer buffer, int numberOfByte, int width) {
            int[] channelColor = new int[width];
            int indexOfChannelColor = 0;
            for (int numberOfByteRead = 0; numberOfByteRead < numberOfByte; numberOfByteRead++) {
                short mark = buffer.get();
                if (mark == -128) continue;
                boolean isSameValue = mark < 0;
                if (isSameValue) {
                    int repeatTime = -mark + 1;
                    int repeatValue = buffer.get();
                    if (repeatValue < 0) repeatValue += 256;
                    numberOfByteRead++;
                    for (int t = 0; t < repeatTime; t++) {
                        channelColor[indexOfChannelColor] = repeatValue;
                        indexOfChannelColor++;
                    }
                } else {
                    int totalTime = mark + 1;
                    for (int t = 0; t < totalTime; t++) {
                        int value = buffer.get();
                        numberOfByteRead++;
                        if (value < 0) value += 256;
                        channelColor[indexOfChannelColor] = value;
                        indexOfChannelColor++;
                    }
                }
            }
            return channelColor;
        }

        private static void writeDataWithoutCompression(ByteBuffer buffer, int[][] channelImg) {
            int height = channelImg.length;
            int width = channelImg[0].length;
            for (int y = 0; y < height; y++) {
                int[] imgy = channelImg[y];
                for (int x = 0; x < width; x++) {
                    byte value = (byte) imgy[x];
                    buffer.put(value);
                }
            }
        }

        private static void writeDataCompressedByRLE(ByteBuffer buffer, int[][] channelImg) {
            int height = channelImg.length;
            int[] length = new int[height];
            byte[][] line = new byte[height][];
            for (int i = 0; i < height; i++) {
                line[i] = compressALineOfChannelColor(channelImg[i]);
                length[i] = line[i].length;
            }
            for (int numberOfByte : length) {
                buffer.putShort((short) numberOfByte);
            }
            for (byte[] aLine : line) {
                buffer.put(aLine);
            }
        }

        private static byte[] compressALineOfChannelColor(int[] channelImg) {
            List<Byte> data = new ArrayList<>(channelImg.length);
            int startIndex = -1;
            int repeatTime = -1;
            for (int i = 1; i < channelImg.length; i++) {
                int lastColor = channelImg[i - 1];
                int color = channelImg[i];

                //初始状态
                if (startIndex == -1) {
                    startIndex = 0;
                    if (color == lastColor) {//颜色相同
                        repeatTime = 1;
                    } else {//颜色不同
                        data.add((byte) lastColor);
                    }
                    continue;
                }

                //连续状态
                if (repeatTime != -1 && lastColor == color) {//颜色相同
                    repeatTime++;
                    continue;
                }
                if (repeatTime == -1 && lastColor != color) {//颜色不同
                    data.add((byte) lastColor);
                    continue;
                }

                //变化状态
                if (repeatTime != -1 && lastColor != color) {//颜色相同->颜色不同
                    repeatTime++;
                    for (int loop = repeatTime / Byte.MAX_VALUE; loop > 0; loop--) {
                        data.add(transRepeatTime(Byte.MAX_VALUE));
                        data.add((byte) lastColor);
                        repeatTime -= Byte.MAX_VALUE;
                    }
                    if (repeatTime > 0) {
                        data.add(transRepeatTime(repeatTime));
                        data.add((byte) lastColor);
                    }

                    repeatTime = -1;
                    startIndex = data.size();
                    continue;
                }
                if (repeatTime == -1 && lastColor == color) {//颜色不同->颜色相同
                    int differentTime = data.size() - startIndex;
                    for (int loop = differentTime / Byte.MAX_VALUE; loop > 0; loop--) {
                        data.add(startIndex, transDifferentTime(Byte.MAX_VALUE));
                        startIndex += Byte.MAX_VALUE + 1;
                        differentTime -= Byte.MAX_VALUE;
                    }
                    if (differentTime > 0) data.add(startIndex, transDifferentTime(differentTime));

                    repeatTime = 1;
                }
            }
            //结束状态
            if (repeatTime != -1) {//颜色相同
                repeatTime++;
                for (int loop = repeatTime / Byte.MAX_VALUE; loop > 0; loop--) {
                    data.add(transRepeatTime(Byte.MAX_VALUE));
                    data.add((byte) channelImg[channelImg.length - 1]);
                    repeatTime -= Byte.MAX_VALUE;
                }
                if (repeatTime > 0) {
                    data.add(transRepeatTime(repeatTime));
                    data.add((byte) channelImg[channelImg.length - 1]);
                }
            } else {//颜色不同
                data.add((byte) channelImg[channelImg.length - 1]);
                int differentTime = data.size() - startIndex;
                for (int loop = differentTime / Byte.MAX_VALUE; loop > 0; loop--) {
                    data.add(startIndex, transDifferentTime(Byte.MAX_VALUE));
                    startIndex += Byte.MAX_VALUE + 1;
                    differentTime -= Byte.MAX_VALUE;
                }
                if (differentTime > 0) data.add(startIndex, transDifferentTime(differentTime));
            }

            byte[] result = new byte[data.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = data.get(i);
            }
            return result;
        }

        private static byte transRepeatTime(int repeatTime) {
            int result = 1 - repeatTime;
            if (result > Byte.MAX_VALUE || result < Byte.MIN_VALUE) throw new IllegalArgumentException();
            return (byte) result;
        }

        private static byte transDifferentTime(int differentTime) {
            int result = differentTime - 1;
            if (result < 0 || result > Byte.MAX_VALUE) throw new IllegalArgumentException();
            return (byte) result;
        }

    }

    private static class NinePatchHelper {

        private static boolean[] readVerticalPixel(int x, ImageRaster raster, ColorRGBA store) {
            boolean[] result = new boolean[raster.getHeight()];
            for (int y = 0; y < result.length; y++) {
                raster.getPixel(x, y, store);
                if (store.a == 0) continue;
                if (store.equals(ColorRGBA.Black)) result[y] = true;
                else return null;
            }
            if (isNinePatchSide(result)) return result;
            return null;
        }

        private static boolean[] readHorizontalPixel(int y, ImageRaster raster, ColorRGBA store) {
            boolean[] result = new boolean[raster.getWidth()];
            for (int x = 0; x < result.length; x++) {
                raster.getPixel(x, y, store);
                if (store.a == 0) continue;
                if (store.equals(ColorRGBA.Black)) result[x] = true;
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

        private static Image cutEdge(ImageRaster fromRaster, boolean[] topSide, boolean[] bottomSide, boolean[] leftSide, boolean[] rightSide, ColorRGBA store) {
            int startX = 0;
            int startY = 0;
            int endX = fromRaster.getWidth();
            int endY = fromRaster.getHeight();
            if (topSide != null) endY--;
            if (bottomSide != null) startY++;
            if (leftSide != null) startX++;
            if (rightSide != null) endX--;
            Image result = ImageHandler.createEmptyImage(endX - startX, endY - startY);
            ImageRaster toRaster = ImageRaster.create(result);
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    fromRaster.getPixel(x + startX, y + startY, store);
                    toRaster.setPixel(x, y, store);
                }
            }
            return result;
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
