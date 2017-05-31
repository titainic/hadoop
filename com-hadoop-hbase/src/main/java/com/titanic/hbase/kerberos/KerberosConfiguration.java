package com.titanic.hbase.kerberos;

import com.titanic.hbase.utils.TykyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.security.User;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by titanic on 17-5-18.
 */
public class KerberosConfiguration
{

    private String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
    private String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
    private String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop";
    private Configuration conf = null;

    public KerberosConfiguration(Configuration conf)
    {
        this.conf = conf;
    }


    /**
     * 设置用户文件下kerberos.properties文件
     * path=keytab和krb5.conf文件所在的位置
     * user=tj02。kerber机机交互用户名
     * @return
     * @throws IOException
     */
    public Configuration login() throws IOException
    {
        String path = TykyUtils.getConfigurationFilePath()+"kerberos.properties";
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        Properties p = new Properties();
        p.load(in);
        String confPath = p.getProperty("path");
        String user = p.getProperty("user");
        String userKeytabFile = confPath + "user.keytab";
        String krb5File = confPath + "krb5.conf";

        System.setProperty("sun.security.krb5.debug", "true");
        conf.set("hbase.security.authentication", "kerberos");
        if (User.isHBaseSecurityEnabled(conf))
        {
            LoginUtil.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, user, userKeytabFile);
            LoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY, ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
            LoginUtil.login(user, userKeytabFile, krb5File, conf);
        }
        return conf;
    }
}
