package org.cache.service.impl;
import org.cache.service.Cache;
import org.cache.service.EvictionPolicy;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CacheImpl<K,V> implements Cache<K,V> {

    private final int cacheCapacity;
    private final HashMap<K,V> store;
    private final EvictionPolicy<K> evictionPolicy;

    public CacheImpl(int cacheCapacity, EvictionPolicy<K> evictionPolicy){
        this.cacheCapacity = cacheCapacity;
        this.store = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public synchronized V get(K key) {
        if(!store.containsKey(key)){
            return null;
        }else{
            evictionPolicy.keyAccessed(key);
            return store.get(key);
        }
    }

    @Override
    public synchronized void put(K key, V value) {
        if(store.containsKey(key)){
            store.put(key, value);
            evictionPolicy.keyAccessed(key);
        }

        if(store.size() >= cacheCapacity){
            K removedKey = evictionPolicy.evictKey();
            if(removedKey != null){
                store.remove(removedKey);
            }
        }

        store.put(key, value);
        evictionPolicy.keyAccessed(key);
    }
}
