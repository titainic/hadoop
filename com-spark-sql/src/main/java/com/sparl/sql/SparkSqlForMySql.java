package com.sparl.sql;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * spark链接mysql数据库
 */
public class SparkSqlForMySql
{


    public static void main(String[] rgs)
    {
        init();
    }

    private static void init()
    {
        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/javademo?"
                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
