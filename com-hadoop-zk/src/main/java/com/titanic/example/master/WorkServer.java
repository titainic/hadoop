package com.titanic.example.master;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务运行主工作类
 */
public class WorkServer
{

    /**
     * 服务器状态，是否在运行
     */
    private volatile boolean running = false;

    private ZkClient zkClient;

    /**
     * master节点中的路径
     */
    private static final String MASTER_PATH = "/master";

    /**
     * 监听master节点删除事件
     */
    private IZkDataListener dataListener;

    /**
     * 记录当前节点的基本信息
     */
    private RunningData serverData;

    /**
     * 记录集群中master节点的信息
     */
    private RunningData masterData;


    ScheduledExecutorService duledExecutorService = Executors.newScheduledThreadPool(1);
    private int delayTime = 5;

    public WorkServer(RunningData runningData)
    {
        serverData = runningData;
        dataListener = new IZkDataListener()
        {
            //当检测到master下面数据被删除的时候，竞选争抢master
            public void handleDataDeleted(String s) throws Exception
            {
                /**
                 *  如果发生网络抖动，上一轮为master的节点，继续为master，其他节点延迟5秒
                 */
                if (masterData != null && masterData.getName().equals(serverData.getName()))
                {
                    takeMater();
                }
                else
                {
                    duledExecutorService.schedule(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                Thread.sleep(5000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }

            public void handleDataChange(String s, Object o) throws Exception
            {

            }
        };
    }

    /**
     * 服务启动
     */
    public void start() throws Exception
    {
        // 启动时判断本节点是否已经运行
        if (running)
        {
            throw new Exception("server is startup...");
        }
        running = true;
        //订阅master节点的删除事件
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);

        takeMater();
    }

    /**
     * 服务停止
     */
    public void stop() throws Exception
    {
        if (!running)
        {
            throw new Exception("server is stop...");
        }
        running = false;

        //取消master节点的订阅
        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);

        releaseMaster();
    }

    /**
     * 竞选争抢master为主工作节点
     */
    public void takeMater()
    {
        if (!running)
        {
            return;
        }
        try
        {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);

            //如果创建成功，当前节点等于主节点
            masterData = serverData;

            //-------------  演示使用 -----------------------------
//            System.out.println(serverData.getName()+" is master");
//            duledExecutorService.schedule(new Runnable() {
//                public void run() {
//                    // TODO Auto-generated method stub
//                    if (checkMaster()){
//                        releaseMaster();
//                    }
//                }
//            }, 5, TimeUnit.SECONDS);
            //---------------------------------------------------

        } catch (ZkNodeExistsException e)
        {
            //如果创建master节点失败。则读取master节点信息，放入masterserver
            RunningData runningData = zkClient.readData(MASTER_PATH, true);

            //如果runnData等于空，说明在读取的瞬间，宕机。则在竞选为master
            if (runningData == null)
            {
                takeMater();
            }
            else
            {
                masterData = runningData;
            }
            e.printStackTrace();
        }

    }

    /**
     * 释放master
     */
    public void releaseMaster()
    {
        if (checkMaster())
        {
            zkClient.delete(MASTER_PATH);
        }
    }

    /**
     * 检查自己是否是master
     */
    public boolean checkMaster()
    {
        //获取zk节点master节点信息，与本机信息对比
        try
        {
            RunningData enevtData = zkClient.readData(MASTER_PATH);
            masterData = enevtData;
            if (masterData.getName().equals(serverData.getName()))
            {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e)
        {
            return false;
        } catch (ZkInterruptedException e)
        {
            return checkMaster();
        } catch (ZkException e)
        {
            return false;
        }
    }


    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

}
