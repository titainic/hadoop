package com.titanic.opencv.example;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

//-Djava.library.path=/home/titanic/soft/opencv/openCV4.1.2/opencv-opencv-59f0319/build/lib
public class HelloOpenCV
{
    static
    {
//        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws NoSuchFieldException
    {

        highlighted2();
    }

    public static void createMat()
    {
        Mat hello = Mat.eye(5, 5, CvType.CV_8UC1);
        System.out.println(hello.dump());
    }

    /**
     * 改变部分区域颜色
     */
    public static void setScalar()
    {
        Mat hello = new Mat(150, 150, CvType.CV_8UC3);
        hello.setTo(new Scalar(180, 80, 250));
        Mat sub = hello.submat(0, 50, 0, 50);
        sub.setTo(new Scalar(0, 0, 100));
        //保存图像
//        Imgcodecs.imwrite("/home/titanic/hello.png", hello);

        //展示图像0
        HighGui.imshow("改变部分区域颜色", hello);
        HighGui.waitKey(0);
    }

    /**
     * 加载图像
     */
    public static void loadImage()
    {
        //灰度图像通道为1
        Mat matCat1 = Imgcodecs.imread("/home/titanic/图片/006.png");
        System.out.println("mat=" + matCat1.width() + "x" + matCat1.height() + "," + matCat1.type());

        //调整图像大小
        Mat matCat2 = Imgcodecs.imread("/home/titanic/图片/006.png", Imgcodecs.IMREAD_REDUCED_COLOR_8);
        HighGui.imshow("改变部分区域颜色", matCat2);
        HighGui.waitKey(0);
    }

    /**
     * 截取图像
     */
    public static void subImage()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/图片/007.png");
        System.out.println(mat);
        Mat sub = mat.submat(250, 650, 600, 900);
        System.out.println(sub);
        HighGui.imshow("截取图像", sub);
        HighGui.waitKey(0);
    }

    /**
     * 当对子矩阵操作，原始矩阵也会收到影响
     * 模糊图像
     */
    public static void blur()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/图片/007.png");
        Mat sub = mat.submat(250, 650, 600, 900);

        Imgproc.blur(sub, sub, new Size(25.0, 25.0));
        System.out.println(sub);
        HighGui.imshow("截取图像", mat);
        HighGui.waitKey(0);
    }

    /**
     * 范围截取1
     */
    public static void range1()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/图片/007.png");
        Mat sub = mat.submat(new Range(250, 650), new Range(600, 900));

        Imgproc.blur(sub, sub, new Size(25.0, 25.0));
        System.out.println(sub);
        HighGui.imshow("截取图像", mat);
        HighGui.waitKey(0);
    }

    /**
     * 范围截取2
     */
    public static void range2()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/图片/007.png");
        Mat sub = mat.submat(new Rect(250, 600, 400, 400));

        Imgproc.blur(sub, sub, new Size(25.0, 25.0));
        System.out.println(sub);
        HighGui.imshow("截取图像", mat);
        HighGui.waitKey(0);
    }

    /**
     * 高亮显示图像中的物体-黑色
     */
    public static void highlighted()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/soft/intellij_workspace/github-hadoop/com-opencv/src/main/resources/image/007.png");

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(mat,mat,150.0,300.0,3,true);
        HighGui.imshow("", mat);
        HighGui.waitKey(0);
    }

    /**
     * 高亮显示图像中的物体-白色
     */
    public static void highlighted2()
    {
        Mat mat = Imgcodecs.imread("/home/titanic/soft/intellij_workspace/github-hadoop/com-opencv/src/main/resources/image/007.png");

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(mat,mat,150.0,300.0,3,true);

        Core.bitwise_not(mat,mat);
        HighGui.imshow("", mat);
        HighGui.waitKey(0);
    }



}
