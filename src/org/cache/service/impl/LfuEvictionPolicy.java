package org.cache.service.impl;
import org.cache.model.enums.EvictionPolicyEnum;
import org.cache.service.EvictionPolicy;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

public class LfuEvictionPolicy<K> implements EvictionPolicy<K> {

    private final HashMap<K, Integer> keyToFrequency;
    private final HashMap<Integer, LinkedHashSet<K>> freqToKeysMap;
    private int minFrequency;

    public LfuEvictionPolicy() {
        this.keyToFrequency = new HashMap<>();
        this.freqToKeysMap = new HashMap<>();
        this.minFrequency = 0;
    }


    @Override
    public EvictionPolicyEnum getEvictionPolicyName() {
        return EvictionPolicyEnum.LFU;
    }

    @Override
    public void keyAccessed(K key) {
        int freq = keyToFrequency.getOrDefault(key,0);
        keyToFrequency.put(key, freq + 1);
        if(freq == 0){
            LinkedHashSet<K> currFreq = freqToKeysMap.get(freq+1);
            if(currFreq == null){
                currFreq = new LinkedHashSet<>();
            }
            currFreq.add(key);
            freqToKeysMap.put(freq+1, currFreq);
            minFrequency = 1;
        }else{
            LinkedHashSet<K> olderFreq = freqToKeysMap.get(freq);
            olderFreq.remove(key);
            if(olderFreq.isEmpty()){
                freqToKeysMap.remove(freq);
                if(minFrequency == freq){
                    minFrequency = freq + 1;
                }
            }
            LinkedHashSet<K> currFreq = freqToKeysMap.get(freq+1);
            if(currFreq == null){
                currFreq = new LinkedHashSet<>();
            }
            currFreq.add(key);
            freqToKeysMap.put(freq, olderFreq);
            freqToKeysMap.put(freq+1, currFreq);
        }
    }

    @Override
    public K evictKey() {
        LinkedHashSet<K> setWithLowestFreq = freqToKeysMap.get(minFrequency);
        if(setWithLowestFreq.isEmpty()){
            return null;
        }

        K key = setWithLowestFreq.iterator().next();
        setWithLowestFreq.remove(key);
        if(setWithLowestFreq.isEmpty()){
            freqToKeysMap.remove(minFrequency);
        }
        keyToFrequency.remove(key);
        return key;
    }
}
