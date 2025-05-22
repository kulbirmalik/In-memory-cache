package org.cache.registry;

import org.cache.model.enums.EvictionPolicyEnum;
import org.cache.service.EvictionPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvictionPolicyRegistry {

    private final Map<EvictionPolicyEnum, EvictionPolicy> registry = new HashMap<>();

    public EvictionPolicyRegistry(List<EvictionPolicy> evictionPolicies) {
        for(EvictionPolicy evictionPolicy : evictionPolicies){
            registry.put(evictionPolicy.getEvictionPolicyName(), evictionPolicy);
        }
    }

    public EvictionPolicy getEvictionPolicy(EvictionPolicyEnum evictionPolicyName) {
        return registry.get(evictionPolicyName);
    }
}
