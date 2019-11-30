package com.titanic.opencv.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 * 开启摄像头，识别人脸
 */
public class OpencvCameraForFace
{


    public static void detectHumenFrontFace(Mat rgb, Mat gray)
    {
        CascadeClassifier cascade = new CascadeClassifier("/home/titanic/soft/opencv/opencv4.1.2/data/haarcascades/haarcascade_frontalface_alt.xml");
        if (cascade.empty())
        {
            System.out.println("文件读取失败");
            return;
        }
        MatOfRect rect = new MatOfRect();
        cascade.detectMultiScale(gray, rect);
        for (Rect re : rect.toArray())
        {
            Imgproc.rectangle(rgb, new Point(re.x, re.y), new Point(re.x
                    + re.width, re.y + re.height), new Scalar(0, 255, 0));
        }
        HighGui.imshow("ImageShow", rgb);
    }

    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture videoCapture = new VideoCapture();
        if (!videoCapture.open(0))
        {
            System.out.println("相机打开失败");
            return;
        }
        while (true)
        {
            Mat img = new Mat();
            if (!videoCapture.read(img))
            {
                return;
            }
            Mat rgb = new Mat();
            Imgproc.cvtColor(img, rgb, Imgproc.COLOR_BGR2RGB);
            Mat gray = new Mat();
            Imgproc.cvtColor(rgb, gray, Imgproc.COLOR_RGB2GRAY);
            detectHumenFrontFace(img, gray);
            HighGui.waitKey(10);
        }
    }
}
