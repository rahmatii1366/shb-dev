package com.shadbarg.secure.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author Mohammad Rahmati, 3/6/2017 7:49 AM
 */
public class ShbCacheProvider<K, V> {
    final static Logger logger =
            Logger.getLogger(ShbCacheProvider.class);

    private LoadingCache<K, V> loadingCache;

    public static ShbCacheProvider getInstance(
            ShbCacheLoader shbCacheLoader,
            int maxSize,
            int seconds) {
        return new ShbCacheProvider(shbCacheLoader, maxSize, seconds);
    }

    private ShbCacheProvider(
            ShbCacheLoader shbCacheLoader,
            int maxSize,
            int seconds) {
        loadingCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(seconds, TimeUnit.SECONDS)
                .build(new CacheLoader<K, V>(){
                    // build the cacheloader
                    @Override
                    public V load(K var1)
                            throws Exception {
                        logger.debug("create new session model " +
                                "and add to cache");
                        //make the expensive call
                        return (V) shbCacheLoader.load(var1);
                    }
                });
    }

    public V retrieve(K key)
            throws Exception {
        if(key == null)
            throw new Exception(
                    "key is null or empty.");
        return loadingCache.get(key);
    }

    public V remove(K key)
            throws Exception {
        if(key == null)
            throw new Exception(
                    "key is null or empty.");
        V v = loadingCache.get(key);
        loadingCache.invalidate(key);
        logger.debug("remove session model.");
        return v;
    }
}
