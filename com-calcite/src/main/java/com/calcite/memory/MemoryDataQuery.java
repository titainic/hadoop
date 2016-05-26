package com.calcite.memory;

import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.*;
import java.util.Properties;

/**
 * 内存数据查询
 * <p/>
 * 这个MAP中存储了数据库名到我们内存中Database结构的映射，
 * 每一个Database中存储了多个Table对象，每一个Table对象
 * 有一些Column和一个二维的data数组，Column定义了字段名
 * 和类型，然后为了测试创建了一个Database对象，名为school，
 * 它包含两个Table，分别为Class和Student，Class对象的初始化如下：
 */
public class MemoryDataQuery
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        Properties info = new Properties();

        try
        {
            Class.forName("org.apache.calcite.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:calcite:model=/home/titanic/soft/intellij_work/hadoop/com-calcite/src/main/resources/School.json", info);


            //--------------------------------------------------------------------------
//            CalciteConnection calciteConn = conn.unwrap(CalciteConnection.class);
            ResultSet result = conn.getMetaData().getTables(null, null, null,null );
            while (result.next())
            {
                System.out.println("Catalog : " +result.getString(1) + ",Database : " + result.getString(2) + ",Table : " + result.getString(3));
            }
            result.close();
            result = conn.getMetaData().getColumns(null, null, "Student", null);
            while (result.next())
            {
                System.out.println("name : " + result.getString(4) + ", type : " + result.getString(5) + ", typename : " + result.getString(6));
            }
            result.close();
            //--------------------------------------------------------------------------


            Statement st = conn.createStatement();
            String sql = "select \"home\", 1 , count(1) from \"Student\" as S INNER JOIN \"Class\" as C on S.\"classId\" = C.\"id\" group by \"home\"";
            System.out.println(sql);
            result = st.executeQuery(sql);
            while (result.next())
            {
                System.out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3));
            }
            result.close();
            conn.close();

        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


}
