package com.titanic.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 异步创建链接
 */
public class CreateNodeAsync implements Watcher
{
    public static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateNodeAsync());
        System.out.println(zooKeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event)
    {
        System.out.println("收到事件："+event);
        if (event.getState() == Event.KeeperState.SyncConnected)
        {
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }
        }
    }

    private void doSomething()
    {
        //节点路径，节点内容，节点权限，节点模式（零时还是永久），异步调用接口callback，异步调用上下文 “创建”
        zooKeeper.create("/node_5", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallBack(), "创建");
    }

    static class IStringCallBack implements AsyncCallback.StringCallback
    {
        //状态码（0是异步调用成功 ），需要创建节点的完整路劲，  异步调用上下文，已经创建节点的真实路劲
        public void processResult(int rc, String path, Object ctx, String name)
        {
            System.out.println("rc:"+rc);
            System.out.println("path:"+path);
            System.out.println("ctx:"+ctx);
            System.out.println("name:"+name);
        }
    }
}
