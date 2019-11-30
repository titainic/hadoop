package com.titanic.opencv.camera;

import org.opencv.core.Mat;

import javax.swing.JPanel;
import java.awt.Graphics;

public class MatPanel extends JPanel
{
    public Mat mat;

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(OpencvMat2BufferedImage.matToBufferedImage(mat),0,0,this);
    }
}
