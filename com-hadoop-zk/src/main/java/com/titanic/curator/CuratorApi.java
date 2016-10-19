package com.titanic.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Curator api操作
 */
public class CuratorApi
{
    public static void main(String[] args)
    {

    }


    /**
     * 客户端构建方式 1
     */
    public static CuratorFramework createSession()
    {
        /**
         * 重试不能超过3次，第一次是1000毫秒，第二次会大于1000毫秒重试
         */
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        /**
         * 执行最大重试次数策略，最大重试5次，重试之间的间隔
         */
        //RetryPolicy retryPolicy = new RetryNTimes(5, 1000);

        /**
         * 会一直重试，直到达到规定的时间，5000毫秒之内重试，每次间隔1000毫秒
         */
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000, 5000, retryPolicy);
        client.start();
        return client;

    }

    /**
     * 客户端构建方式 2
     */
    public static CuratorFramework createSessionBulider()
    {
        /**
         * 重试不能超过3次，第一次是1000毫秒，第二次会大于1000毫秒重试
         */
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        /**
         * 执行最大重试次数策略，最大重试5次，重试之间的间隔
         */
        //RetryPolicy retryPolicy = new RetryNTimes(5, 1000);

        /**
         * 会一直重试，直到达到规定的时间，5000毫秒之内重试，每次间隔1000毫秒
         */
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.1.105:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)   //执行策略
                .build();
        client.start();

        return client;

    }

    /**
     * 创建节点
     *
     * @param client
     * @param path
     * @param nodeData
     */
    public static void createNodePath(CuratorFramework client, String path, String nodeData)
    {
        try
        {
            client.create()
                    .creatingParentsIfNeeded() //创建带有父节点
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, nodeData.getBytes());//节点路径，节点数据
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除节点
     *
     * @param client
     * @param path
     */
    public static void delNode(CuratorFramework client, String path)
    {
        try
        {
            client.delete()
                    .guaranteed() //只要保证客户端链接有效，就会一直执行删除操作，直到执行成功
                    .deletingChildrenIfNeeded() //删除所有节点下面带有子节点，最后删除节点本身
                    .withVersion(-1)
                    .forPath(path);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取子节点
     *
     * @param client
     * @param path
     */
    public static List<String> getChildernNodeList(CuratorFramework client, String path)
    {
        List<String> pathList = null;
        try
        {
            pathList = client.getChildren().forPath(path);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(path.toString());
        return pathList;
    }

    /**
     * 获取节点内容
     *
     * @param client
     * @param path
     * @return
     */
    public static String getNodeData(CuratorFramework client, String path)
    {
        String data = null;
        Stat stat = new Stat();
        try
        {
            byte[] byteArr = client.getData().storingStatIn(stat).forPath(path);
            data = new String(byteArr);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(stat);

        return data;
    }

    /**
     * 修改节点内容
     *
     * @param client
     * @param updateData
     */
    public static void updateNodeData(CuratorFramework client, String path, String updateData)
    {
        try
        {
            client.setData().forPath(path, updateData.getBytes());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 判断节点是否存在
     *
     * @param client
     * @param path
     */
    public static boolean checkNodeExists(CuratorFramework client, String path)
    {
        Stat stat = null;
        try
        {
            stat = client.checkExists().forPath(path);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (stat == null)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 异步调用
     * @param client
     * @param path
     */
    public static void checkNodeExistsAsy(CuratorFramework client, String path) throws InterruptedException
    {

        //优化同一时刻，异步现场过多，影响系统性能
        ExecutorService es = Executors.newFixedThreadPool(5);

        try
        {
            client.checkExists().inBackground(new BackgroundCallback()
            {
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception
                {
                    //异步操作类型
                    CuratorEventType type = curatorEvent.getType();

                    //执行返回码 0是成功，非0是失败
                    int code =curatorEvent.getResultCode();

                    //上下文 "123"
                    Object o = curatorEvent.getData();

                    //出发这个事件的节点路径
                    String path = curatorEvent.getPath();

                    //子节点列表
                    List<String> pathList = curatorEvent.getChildren();

                    //数据内容
                    byte[] bytesArr = curatorEvent.getData();
                }
            },"123",es).forPath(path);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //便于观察，线程等待几秒
        Thread.sleep(10000);
    }


    /**
     * 节点监听
     * @param client
     * @param path
     */
    public static void nodeListener(CuratorFramework client, String path) throws InterruptedException
    {
        //知识点
        final NodeCache nodeCache = new NodeCache(client, path);
        try
        {
            nodeCache.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        nodeCache.getListenable().addListener(new NodeCacheListener()
        {
            public void nodeChanged() throws Exception
            {
                System.out.println(new String(nodeCache.getCurrentData().getData()));
            }
        });

        //便于观察，线程等待几秒
        Thread.sleep(10000);

    }

    /**
     * 监听子节点
     * @param client
     * @param path
     */
    public static void nodeChildrenListener(CuratorFramework client, String path) throws InterruptedException
    {
        final PathChildrenCache cache = new PathChildrenCache(client, path,true);//为true时，当子节点内容发生变化，获取子节点的内容

        cache.getListenable().addListener(new PathChildrenCacheListener()
        {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception
            {
                switch (pathChildrenCacheEvent.getType())
                {
                    case CHILD_ADDED://创建
                        System.out.println(pathChildrenCacheEvent.getData().getPath());
                        break;
                    case CHILD_UPDATED://修改
                        System.out.println(pathChildrenCacheEvent.getData().getData());
                        break;
                    case CHILD_REMOVED://删除
                        System.out.println(pathChildrenCacheEvent.getData().toString());
                        break;
                    default:
                        break;

                }
            }
        });

        //便于观察，线程等待几秒
        Thread.sleep(10000);
    }


}
