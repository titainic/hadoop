package com.titanic.hbase.mysql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tyky.utils.api.TykyUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by titanic on 16-11-30.
 */
public class MySqlJdbc
{
    Connection conn;

    Properties prop = null;
    String sql = null;

    public MySqlJdbc()
    {
        try
        {
            prop = TykyUtils.gethbasePropertiesFimaly();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        String url = prop.getProperty("dburl");
        String user = prop.getProperty("dbuser");
        String pwd = prop.getProperty("dbpwd");
        sql = prop.getProperty("sql");
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public List<Map<String, String>> queryForList()
    {

        List<Map<String, String>> list = Lists.newArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                Map<String, String> map = Maps.newHashMap();
                ResultSetMetaData rsmd = rs.getMetaData();
                int counts = rsmd.getColumnCount();
                for (int col = 0; col < counts; col++)
                {
                    String colname = rsmd.getColumnName(col + 1).trim();
                    map.put(colname, rs.getString(colname)+"");
                }
                list.add(map);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (rs != null)
            {
                rs.close();
            }
            if (ps != null)
            {
                ps.close();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public void closeConn()
    {

        try
        {
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
