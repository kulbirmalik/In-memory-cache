package org.cache.service;

import org.cache.model.enums.EvictionPolicyEnum;

public interface EvictionPolicy<K> {

    EvictionPolicyEnum getEvictionPolicyName();

    void keyAccessed(K key);

    K evictKey();
}
