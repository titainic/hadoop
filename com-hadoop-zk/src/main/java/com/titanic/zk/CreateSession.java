package com.titanic.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 客户端与zk建立session链接
 */
public class CreateSession implements Watcher
{

    public static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        //客户端与zk建立链接
        zk = new ZooKeeper("127.0.0.1", 5000, new CreateSession());
        System.out.println(zk.getState());

        Thread.sleep(10000);

    }

    //do something 业务代码
    public void process(WatchedEvent watchedEvent)
    {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
        {
            doSomething();
        }
    }

    //业务代码
    private void doSomething()
    {
        System.out.println("do samething");
    }
}
