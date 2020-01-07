package com.titanic.opencv.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.util.HashMap;
import java.util.Map;

/**
 * Mat相互转换BufferedImage
 */
public class OpenCvUtils
{

    static
    {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    /**
     * @param srcMat 源水印图像
     * @return 去除水印后的图像
     */
    private static Mat clearWatermark(Mat srcMat)
    {
        Mat destmat = new Mat(srcMat.height(), srcMat.width(), CvType.CV_8UC1);
        for (int y = 0; y < srcMat.height(); y++)
        {
            for (int x = 0; x < srcMat.width(); x++)
            {
                double[] data = srcMat.get(y, x);
                for (int i = 0; i < data.length; i++)
                {
                    if (data[i] == 229)
                    {//水印色
                        data[i] = 255;//白色
                    }
                }
                destmat.put(y, x, data);
            }
        }
        return destmat;
    }

    /**
     * @param convertImgMap 待转换的图片
     * @return 转换好的图片bufferimages
     */
    public static Map<Integer, BufferedImage> fileToclearWatermark(Map<Integer, BufferedImage> convertImgMap)
    {
        Map<Integer, BufferedImage> temp = convertImgMap;
        Map<Integer, BufferedImage> yesconvertImgMap = new HashMap<Integer, BufferedImage>();
        for (int i = 1; i <= temp.size(); i++)
        {
            BufferedImage img = temp.get(i);
            Mat srcmat = BufImg2Mat(img, BufferedImage.TYPE_INT_RGB, CvType.CV_8UC3);
            Mat dstmat = clearWatermark(srcmat);
            BufferedImage destBufferImg = Mat2BufImg(dstmat, "bmp");
            yesconvertImgMap.put(i, destBufferImg);
        }
        return yesconvertImgMap;
    }

    public static void fileToclearWatermark(String srcPath, String dstPath)
    {
        File root = new File(srcPath);
        if (root.isFile())
        {
            try
            {
                throw new FileSystemException("必须为一个目录");
            } catch (FileSystemException e)
            {
                e.printStackTrace();
            }
        } else
        {
            File[] files = root.listFiles();//"Z:\\code\\";
            for (File file : files)
            {
                Mat mat = Imgcodecs.imread(file.getPath());
                Mat mat1 = clearWatermark(mat);
                Imgcodecs.imwrite(dstPath + file.getName(), mat1);
                System.out.println(dstPath + file.getName());
            }
            //文件目录不能带有中文
           /* for (int i = 0; i < files.length; i++) {//Z:\TEMP\
                System.out.println(dstPath+i+".bmp");
                Mat mat = Imgcodecs.imread(dstPath+i+".bmp");
                Mat mat1 = clearWatermark(mat);
                //System.out.println(dstPath+i+".bmp");
               // Imgcodecs.imwrite(dstPath+i+".bmp",mat1);
                System.out.println(Imgcodecs.imwrite(dstPath+i+".bmp",mat1));
            }*/
        }


    }

    /**
     * Mat转换成BufferedImage
     *
     * @param matrix        要转换的Mat
     * @param fileExtension 格式为 ".jpg", ".png", etc
     * @return
     */
    public static BufferedImage Mat2BufImg(Mat matrix, String fileExtension)
    {
        // convert the matrix into a matrix of bytes appropriate for
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try
        {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bufImage;
    }

    /**
     * BufferedImage转换成Mat
     *
     * @param original 要转换的BufferedImage
     * @param imgType  bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType  转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat BufImg2Mat(BufferedImage original, int imgType, int matType)
    {
        if (original == null)
        {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType)
        {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try
            {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally
            {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }
}
