package com.calcite.memory;

import java.util.LinkedList;
import java.util.List;

/**
 * 表映射
 */
public class Table
{
    public String tablesName;
    public List<Column> columns = new LinkedList<Column>();
    public List<List<String>> data = new LinkedList<List<String>>();

    public String getTablesName()
    {
        return tablesName;
    }

    public void setTablesName(String tablesName)
    {
        this.tablesName = tablesName;
    }

    public List<Column> getColumns()
    {
        return columns;
    }

    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }

    public List<List<String>> getData()
    {
        return data;
    }

    public void setData(List<List<String>> data)
    {
        this.data = data;
    }
}
