package com.titanic.ml.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class DataInitUtils
{
    public static void loadCSVinitList(List<Double> xList ,List<Double> yList ,String dataPath)
    {
        DataInputStream in = null;
        try
        {
            in = new DataInputStream(new FileInputStream(new File(dataPath)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String csvRow;
            while ((csvRow = bufferedReader.readLine()) != null)
            {
                String[] strArr = csvRow.split(",");
                xList.add(Double.valueOf(strArr[0]));
                yList.add(Double.valueOf(strArr[1]));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
