package com.shadbarg.secure.cache;

/**
 * @author Mohammad Rahmati, 3/6/2017 12:00 PM
 */
public interface ShbCacheLoader<K, V> {
    V load(K key) throws Exception;
}
