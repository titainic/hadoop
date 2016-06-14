package com.titanic.es;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * ES索引操作
 */
public class EsIndexBuilder
{

    /**
     * 构建ES索引
     * @param map
     * @param index
     * @param type
     */
    public void buliderEsIndex(Map<String, String> map, String index, String type)
    {

        if (checkIndexExists(index))
        {
            System.out.println("es index is Exists");
            System.exit(0);
        }


        buliderIndex(index);

        XContentBuilder esMapping = buliderEsMapping(map);

        //构建type
        PutMappingRequest putMappingRequest = Requests.putMappingRequest(index).type(type).source(esMapping);
        EsClient.getEsClient().admin().indices().putMapping(putMappingRequest).actionGet();

        //关闭ES client
        EsClient.getEsClient().close();
    }

    /**
     * 检查ES中索引是否存在
     *
     * @param index
     * @return
     */
    private boolean checkIndexExists(String index)
    {
        boolean indexExists = EsClient.getEsClient().admin().indices().prepareExists(index).execute().actionGet().isExists();
        return indexExists;
    }

    /**
     * 检查index索引中type是否存在
     *
     * @return
     */
    private boolean checkTypeExists(String index, String type)
    {
        boolean typeExists = EsClient.getEsClient().admin().indices().prepareTypesExists(index).setTypes(type).execute().actionGet().isExists();
        return typeExists;
    }

    /**
     * 构建索引
     *
     * @param index
     */
    private void buliderIndex(String index)
    {
        EsClient.getEsClient().admin().indices().create(new CreateIndexRequest(index));
    }

    /**
     * 构建索引列，和数据类型
     *
     * @param map
     * @return
     */
    private XContentBuilder buliderEsMapping(Map<String, String> map)
    {
        XContentBuilder mapBuilder = null;
        try
        {
            mapBuilder = jsonBuilder();
            mapBuilder.startObject().startObject("properties");

            if (map != null && map.size() > 0)
            {
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    //date类型数据，在es中构建索引时，要设置format和format格式
                    if ("date".equals(entry.getValue()))
                    {
                        mapBuilder.startObject(entry.getKey()).field("type", entry.getValue()).field("format", "yyyy-MM-dd HH:mm:ss").endObject();
                    } else
                    {
                        mapBuilder.startObject(entry.getKey()).field("type", entry.getValue()).field("store", true).endObject();
                    }

                }
            }
            mapBuilder.endObject().endObject();
            System.out.println(mapBuilder.string());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return mapBuilder;
    }

    /**
     * 删除索引
     *
     * @param index
     */
    public boolean deleteEsIndex(String index)
    {
        DeleteIndexResponse rep = EsClient.getEsClient().admin().indices().prepareDelete("myindexname").execute().actionGet();
        return rep.isAcknowledged();
    }
}
