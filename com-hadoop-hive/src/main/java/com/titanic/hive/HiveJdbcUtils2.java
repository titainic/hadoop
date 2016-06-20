package com.titanic.hive;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;

import java.sql.*;

/**
 * 通过配置实现JDBC,有问题，带测试
 */
public class HiveJdbcUtils2
{
    public static void main(String[] args)
    {
        Connection conn = null;
        ResultSet rs = null;
        Statement st = null;

        HiveConf conf = new HiveConf();
        conf.addResource(new Path("file:///home/titanic/soft/hive-site.xml"));
        String hql = "select * from test_bin";

        try
        {
            Class.forName(conf.getVar(HiveConf.ConfVars.METASTORE_CONNECTION_DRIVER));
            conn = DriverManager.getConnection(conf.getVar(HiveConf.ConfVars.METASTORECONNECTURLKEY),
                    conf.getVar(HiveConf.ConfVars.METASTORE_CONNECTION_USER_NAME),
                    conf.getVar(HiveConf.ConfVars.METASTOREPWD));
            st = conn.createStatement();
            rs = st.executeQuery(hql);
            while (rs.next())
            {
                System.out.println(rs.getString(1) + " : " + rs.getString(2));
            }
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
}
