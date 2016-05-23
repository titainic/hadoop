package com.calcite.memory;

import java.util.HashMap;
import java.util.Map;

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
    public static Map<String, Database> map = new HashMap<String, Database>();
    public static Table classTable = null;
    public static Table studentTable = null;

    public static void main(String args[])
    {
        initData();
    }

    /**
     * 初始化数据
     */
    private static void initData()
    {
        initClassTable();
        initStudentTable();
    }

    /**
     * 初始化class表结构
     */
    private static void initClassTable()
    {
        classTable = new Table();
        classTable.setTablesName("Class");

        Column name = new Column();
        name.setName("id");
        name.setType("varchar");

        Column id = new Column();
        id.setName("id");
        id.setType("integer");

        Column teacher = new Column();
        teacher.setName("teacher");
        teacher.setType("varchar");

        classTable.columns.add(name);
        classTable.columns.add(id);
        classTable.columns.add(teacher);
    }


    /**
     * 初始化Student表结构
     */
    private static void initStudentTable()
    {
        studentTable = new Table();
        studentTable.setTablesName("Student");

        Column name = new Column();
        name.name = "name";
        name.type = "varchar";
        studentTable.columns.add(name);

        Column id = new Column();
        id.name = "id";
        id.type = "varchar";
        studentTable.columns.add(id);

        Column classId = new Column();
        classId.name = "classId";
        classId.type = "integer";
        studentTable.columns.add(classId);

        Column birth = new Column();
        birth.name = "birthday";
        birth.type = "date";
        studentTable.columns.add(birth);

        Column home = new Column();
        home.name = "home";
        home.type = "varchar";
        studentTable.columns.add(home);
    }



}
