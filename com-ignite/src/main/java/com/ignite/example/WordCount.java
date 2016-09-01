package com.ignite.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.List;

/**
 * Created by titanic on 16-8-17.
 */
public class WordCount
{
    public static void main(String[] args)
    {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try
        {
            Ignite ignite = Ignition.start();
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // Query top 10 popular words every 5 seconds.
            while (true)
            {
                // Execute queries.
                List<List<?>> top10 = stmCache.query(top10Qry).getAll();

                // Print top 10 words.
//                ExamplesUtils.printQueryResults(top10);

                Thread.sleep(5000);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
