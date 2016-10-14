package com.titanic.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * 异步获取路径
 */
public class GetChildrenASync implements Watcher
{
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new GetChildrenASync());
        Thread.sleep(10000);
    }

    public void process(WatchedEvent event)
    {
        if (event.getState() == Event.KeeperState.SyncConnected)
        {
            if (event.getType() == Event.EventType.None && null == event.getPath())
            {
                doSomething();
            }
            else
            {
                if (event.getType() == Event.EventType.NodeChildrenChanged)
                {
                    try
                    {
                        zk.getChildren(event.getPath(), true);
                    } catch (KeeperException e)
                    {
                        e.printStackTrace();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void doSomething()
    {
         zk.getChildren("/", true, new IChildren2Callback(),null);
    }


    private class IChildren2Callback implements AsyncCallback.Children2Callback
    {
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("rc="+rc).append("\n");
            sb.append("path="+path).append("\n");
            sb.append("ctx="+ctx).append("\n");
            sb.append("children="+children).append("\n");
            sb.append("stat="+stat).append("\n");
            System.out.println(sb.toString());
        }
    }
}
