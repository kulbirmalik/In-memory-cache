package org.cache;

import org.cache.model.enums.EvictionPolicyEnum;
import org.cache.registry.EvictionPolicyRegistry;
import org.cache.service.EvictionPolicy;
import org.cache.service.impl.CacheImpl;
import org.cache.service.impl.LfuEvictionPolicy;
import org.cache.service.impl.LruEvictionPolicy;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EvictionPolicyRegistry evictionPolicyRegistry = initializeEvictionPolicyRegistry();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Policy Type for your cache (LRU | LFU).");
        String policyName = scanner.nextLine();
        try {
            EvictionPolicyEnum evictionPolicyName = EvictionPolicyEnum.valueOf(policyName);
            EvictionPolicy evictionPolicy = evictionPolicyRegistry.getEvictionPolicy(evictionPolicyName);
            CacheImpl<String,String> cache = new CacheImpl<>(2,evictionPolicy);
            cache.put("1","first");
            cache.put("2","second");
            cache.get("1");
        } catch (Exception e){
            System.out.println("Not available in allowed list of eviction policy " + policyName);
            throw new RuntimeException(e);
        }
    }

    private static EvictionPolicyRegistry initializeEvictionPolicyRegistry() {
        LruEvictionPolicy lruEvictionPolicy = new LruEvictionPolicy();
        LfuEvictionPolicy lfuEvictionPolicy = new LfuEvictionPolicy();
        return new EvictionPolicyRegistry(Arrays.asList(lruEvictionPolicy, lfuEvictionPolicy));
    }
}