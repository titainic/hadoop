package com.titanic.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 同步创建链接
 * 创建模式:
 * CreateMode.PERSISTENT 持久节点
 * CreateMode.PERSISTENT_SEQUENTIAL 持久节点且是顺序节点
 * CreateMode.EPHEMERAL 零时节点
 * CreateMode.EPHEMERAL_SEQUENTIAL 零时节点，且顺序节点
 */
public class CreateNodeSync implements Watcher
{

    public static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        zk = new ZooKeeper("127.0.0.1", 5000, new CreateNodeSync());
        Thread.sleep(10000);
    }

    public void process(WatchedEvent event)
    {
        if (event.getState() == Event.KeeperState.SyncConnected)
        {
            doSomething();
        }
    }

    private void doSomething()
    {
        try
        {
            //节点路径，节点内容，节点权限，节点模式（零时还是永久）
            String path = zk.create("/node_4", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("return path : " + path);
        } catch (KeeperException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
