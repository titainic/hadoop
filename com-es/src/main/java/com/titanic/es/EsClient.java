package com.titanic.es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by titanic on 16-6-14.
 */
public class EsClient
{
    private static Client client;

    public static Client getEsClient()
    {
        if (client == null)
        {
            Settings settings = Settings.settingsBuilder().put("cluster.name", "tj").build();

            try
            {
                client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.9.7.4"), 9300));
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
        }
        return client;
    }

    public static void esClientClose()
    {
        if(client != null)
        {
            client.close();
        }

    }

}
