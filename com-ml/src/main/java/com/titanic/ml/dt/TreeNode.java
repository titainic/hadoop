package com.titanic.ml.dt;

import java.util.ArrayList;

public class TreeNode
{
    private String sname;//节点名

    public TreeNode(String str)
    {
        sname = str;
    }

    public String getsname()
    {
        return sname;
    }

    ArrayList<String> label = new ArrayList<String>();//和子节点间的边标签
    ArrayList<TreeNode> node = new ArrayList<TreeNode>();//对应子节点
}
