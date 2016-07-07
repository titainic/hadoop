package com.hadoop.dnn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.ha.HAServiceProtocol;
import org.apache.hadoop.ha.HAServiceStatus;
import org.apache.hadoop.ha.HAServiceTarget;
import org.apache.hadoop.hdfs.tools.DFSHAAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 动态获得Active NameNode
 * 针对HA集群
 * 未测试。后续如果使用，在测试
 */
public class HATest extends DFSHAAdmin
{


    private Logger log = LoggerFactory.getLogger(HATest.class);
    private int rpcTimeoutForChecks = -1;
    private String nnidStr;
    private static String activeNameNodeHostAndPort = null;

    public HATest()
    {
    }

    public HATest(Configuration conf, String nnidStr)
    {
        super.setConf(conf);
        if (conf != null)
        {
            rpcTimeoutForChecks = conf.getInt(
                    CommonConfigurationKeys.HA_FC_CLI_CHECK_TIMEOUT_KEY,
                    CommonConfigurationKeys.HA_FC_CLI_CHECK_TIMEOUT_DEFAULT);
        }
        this.nnidStr = nnidStr;
    }

    public String getActiveNameNode()
    {
        String[] nnids = nnidStr.split(",", -1);
        HAServiceProtocol proto = null;
        HAServiceTarget haServiceTarget = null;
        HAServiceStatus state = null;
        try
        {
            for (String nnid : nnids)
            {
                haServiceTarget = resolveTarget(nnid);
                proto = haServiceTarget.getProxy(
                        getConf(), rpcTimeoutForChecks);
                state = proto.getServiceStatus();
                if (state.getState().ordinal() == HAServiceProtocol.HAServiceState.ACTIVE.ordinal())
                {
                    log.info("Active NameNode:{}",
                            haServiceTarget.getAddress().getHostName() + ":" + haServiceTarget.getAddress().getPort());
                    return haServiceTarget.getAddress().getHostName() + ":" + haServiceTarget.getAddress().getPort();
                }
            }
        } catch (IOException e)
        {
            log.error("获取Active NameNode异常!");
        }
        return null;
    }

}
