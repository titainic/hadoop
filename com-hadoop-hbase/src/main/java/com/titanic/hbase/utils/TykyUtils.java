package com.titanic.hbase.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by titanic on 17-5-24.
 */
public class TykyUtils
{
    public static String getConfigurationFilePath()
    {
        String path = System.getProperty("user.home") + File.separator + "kerberos" + File.separator;
//        String path =TykyUtils.class.getClassLoader().getResource("conf/").getPath();
        File file = new File(path);
        if (file.exists())
        {
            new Exception("缺少kerberos文件");
        }
        return path;
    }

    public static void main(String[] args)
    {
        System.out.println(getConfigurationFilePath());
    }

    public static Properties gethbasePropertiesFimaly()
    {
        String file = getConfigurationFilePath()+"kerberos.properties";
        Properties p = new Properties();
        try
        {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            p.load(in);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
       return p;

    }

}
