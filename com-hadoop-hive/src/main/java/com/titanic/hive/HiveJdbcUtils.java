package com.titanic.hive;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * hive通过JDBC查询hive数据库
 */
public class HiveJdbcUtils
{
    private String jdbcDBhost = "192.9.11.90";
    private String jdbcUrl = "jdbc:hive2://" + jdbcDBhost + ":10000/default";
    private String jdbcDriver = "org.apache.hive.jdbc.HiveDriver";
    private String jdbcUser = "hive";
    private String jdbcPasswd = "12345678";

    private ResultSet resultSet = null;
    private Connection conn = null;
    private PreparedStatement pstmt = null;


    /**
     * 测试查询
     * @param args
     */
    public static void main(String[] args)
    {
        String hql = "select * from test_bin";
        HiveJdbcUtils hiveJdbcUtils = new HiveJdbcUtils();
        List<Map<String, Object>> maps = hiveJdbcUtils.executeQueryHql(hql);
        hiveJdbcUtils.releaseConn();
        for (Map<String, Object> mm : maps)
        {
            System.out.println(mm);
        }
    }


    public HiveJdbcUtils()
    {
        try
        {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        try
        {
            conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPasswd);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Map<String, Object>> executeQueryHql(String hql)
    {
        List<Map<String, Object>> list = Lists.newArrayList();
        try
        {
            conn = getConnection();
            pstmt = conn.prepareStatement(hql);
            resultSet = pstmt.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next())
            {
                Map<String, Object> map = Maps.newHashMap();
                for (int i = 0; i < cols_len; i++)
                {
                    String cols_name = metaData.getColumnName(i + 1);
                    java.lang.Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null)
                    {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public void releaseConn()
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
