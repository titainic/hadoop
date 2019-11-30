package com.titanic.opencv.camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;

// -Djava.library.path=/home/titanic/soft/opencv/opencv4.1.2/build/lib
public class OpenCVCamera
{

    static
    {
//        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

//    public static void main(String[] args)
//    {
//        MatPanel t = new MatPanel();
//        JFrame frame0 = new JFrame();
//        frame0.getContentPane().add(t);
//        frame0.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame0.setSize(320, 240);
//        frame0.setVisible(true);
//        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        VideoCapture camera = new VideoCapture();
//        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
//        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240);
//
//        Mat frame = new Mat();
//        while (true)
//        {
//            if (camera.read(frame))
//            {
//                t.mat = frame;
//                t.repaint();
//            }
//        }
//    }ss

    public static void main(String[] args)
    {
        Mat frame = new Mat();
        //0; default video device id
        VideoCapture camera = new VideoCapture(0);
        JFrame jframe = new JFrame("Title");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setVisible(true);

        while (true) {
            if (camera.read(frame)) {

                ImageIcon image = new ImageIcon(OpencvMat2BufferedImage.matToBufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();

            }
        }
    }
}
