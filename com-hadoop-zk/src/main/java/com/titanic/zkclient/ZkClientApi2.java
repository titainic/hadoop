package com.titanic.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

/**
 * Created by titanic on 16-10-18.
 */
public class ZkClientApi2
{
    public static ZkClient zc;
    public static void main(String[] args) throws InterruptedException
    {
        //初始化参数，ip端口，回话过期的时间，链接超时的时间,序列化器（序列化对象）
//        zc = new ZkClient("127.0.0.1:2181",10000,10000, new SerializableSerializer());

        //监控控制台修改所用的序列化器
        zc = new ZkClient("127.0.0.1:2181",10000,10000, new BytesPushThroughSerializer());

        System.out.println("connection ok");
        subscribeDataChanges("/jike20");

        Thread.sleep(10000000);
    }

    /**
     * 修改节点数据
     */
    public static void updateNodeData()
    {
        User u = new User();
        u.setId(22);
        u.setNamr("test2");
        // 节点路劲，节点新内容,数据版本号
        zc.writeData("/jike5",u,1);
    }

    /**
     * 订阅子节点新增删除发生变化
     */
    public static void subscribeChildChanges(String path)
    {
        //监听子节点的变化,例如子节点的添加和删除
        zc.subscribeChildChanges(path,new ZkChildListener());
    }

    /**
     * 订阅节点内容发生变化
     * @param path
     */
    public static void subscribeDataChanges(String path)
    {
        zc.subscribeDataChanges("/jike20",new ZkDataListener());
    }

    public static class ZkDataListener implements IZkDataListener
    {
        //节点内容发生变化的时候触发此方法
        public void handleDataChange(String s, Object o) throws Exception
        {
            System.out.println(s +" ： "+ o);
        }

        //当节点被删除的时候此方法被触发
        public void handleDataDeleted(String s) throws Exception
        {
            System.out.println(s);
        }
    }



    public static class ZkChildListener implements IZkChildListener
    {
        //节点路劲和子节点列表
        public void handleChildChange(String path, List<String> list) throws Exception
        {
            //节点路劲
            System.out.println(path);
            System.out.println(list.toString());
        }
    }
}
