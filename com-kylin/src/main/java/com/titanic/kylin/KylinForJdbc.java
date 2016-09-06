package com.titanic.kylin;

import java.sql.*;
import java.util.Properties;

/**
 * JDBC链接kylin
 */
public class KylinForJdbc
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        ResultSet resultSet = null;
        Statement state = null;
        Connection conn = null;
        try
        {
            Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();
            Properties info = new Properties();
            info.put("user", "ADMIN");
            info.put("password", "KYLIN");

            //                    "jdbc:kylin://localhost:7070/kylin_project_name"
            conn = driver.connect("jdbc:kylin://192.9.7.191:7070/zeppelin_kylin", info);
            state = conn.createStatement();
            resultSet = state.executeQuery("select * from kylin_sales");

            while (resultSet.next())
            {
                System.out.println(resultSet.getString(1));
            }
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {

            try
            {

                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (state != null)
                {
                    state.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }


        }


    }
}
