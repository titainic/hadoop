package com.titanic.opencv.camera;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class OpencvMat2BufferedImage
{


    public static BufferedImage matToBufferedImage(Mat frame)
    {
        int type = 0;
        if (frame == null)
        {
            return null;
        }

        if (frame.channels() == 1)
        {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }else
        {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0 , data);
        return image;

    }
}
