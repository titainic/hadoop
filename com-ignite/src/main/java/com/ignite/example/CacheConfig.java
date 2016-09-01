package com.ignite.example;

import org.apache.ignite.cache.affinity.AffinityUuid;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by titanic on 16-8-17.
 */
public class CacheConfig
{
    public static CacheConfiguration<String, Long> wordCache()
    {
        CacheConfiguration<String, Long> cfg = new CacheConfiguration<String, Long>("words");
        // Index individual words.
        cfg.setIndexedTypes(AffinityUuid.class, /*word type*/String.class);
        // Sliding window of 1 seconds.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(new Duration(SECONDS, 1))));
        return cfg;
    }
}