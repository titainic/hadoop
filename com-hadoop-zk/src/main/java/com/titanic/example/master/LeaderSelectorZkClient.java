package com.titanic.example.master;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 作为调度器，启动和停止WorkServer
 */
public class LeaderSelectorZkClient
{
    /**
     * 启动的服务器个数
     */
    private static final int CLIENT_INT = 10;

    private static final String ZK_IP = "127.0.0.1:2181";

    public static void main(String[] args) throws Exception
    {
        //保存所有zkclient的列表
        List<ZkClient> zkClientsList = new ArrayList<ZkClient>();

        List<WorkServer> workServersList = new ArrayList<WorkServer>();

        try
        {
            for (int i = 0; i < CLIENT_INT; i++)
            {
                ZkClient zkclient = new ZkClient(ZK_IP, 5000, 5000, new SerializableSerializer());
                zkClientsList.add(zkclient);

                RunningData runningData = new RunningData();
                runningData.setCid(Long.valueOf(i));
                runningData.setName("Client#" + i);

                //创建服务
                WorkServer ws = new WorkServer(runningData);
                ws.setZkClient(zkclient);
                ws.start();
            }
            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } finally
        {
            System.out.println("Shutting down...");

            for ( WorkServer workServer : workServersList )
            {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for ( ZkClient client : zkClientsList )
            {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
