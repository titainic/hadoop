package com.titanic.hive;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hive.ql.CommandNeedRetryException;

import java.io.IOException;

/**
 * Created by titanic on 16-6-15.
 */
public class HqlExecutable
{
    public static void main(String[] args)
    {
//        Configuration configuration = new Configuration();
//        configuration.addResource("/home/titanic/soft/core-site.xml");

        HiveCmdBulider hiveCmdBulider = new HiveCmdBulider("BEELINE");
        hiveCmdBulider.addStatement("show databases;");

        System.out.println(hiveCmdBulider.build());

        HiveClient hiveClient = new HiveClient("/home/titanic/soft/hive-site.xml");
        try
        {
            hiveClient.executeHQL(hiveCmdBulider.build());
        } catch (CommandNeedRetryException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
