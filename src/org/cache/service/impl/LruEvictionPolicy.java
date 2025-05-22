package org.cache.service.impl;
import org.cache.model.Node;
import org.cache.model.enums.EvictionPolicyEnum;
import org.cache.service.EvictionPolicy;
import java.util.concurrent.ConcurrentHashMap;

public class LruEvictionPolicy<K> implements EvictionPolicy<K> {

    private final ConcurrentHashMap<K, Node<K>> map;
    Node<K> head = new Node<>(null);
    Node<K> tail = new Node<>(null);

    public LruEvictionPolicy(){
        this.map = new ConcurrentHashMap<>();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public EvictionPolicyEnum getEvictionPolicyName() {
        return EvictionPolicyEnum.LRU;
    }

    @Override
    public void keyAccessed(K key) {
        if(map.containsKey(key)){
            Node<K> node = map.get(key);
            removeNode(node);
            addInFront(node);
        }else{
            Node<K> newNode = new Node<>(key);
            addInFront(newNode);
            map.put(key, newNode);
        }
    }

    @Override
    public K evictKey() {
        if(map.isEmpty()){
            return null;
        }else{
            Node<K> lastNode = tail.prev;
            removeNode(lastNode);
            map.remove(lastNode.key);
            return lastNode.key;
        }
    }

    private void removeNode(Node<K> node){
        node.next.prev = node.prev;
        node.prev.next = node.next;
        node.prev = null;
        node.next = null;
    }

    private void addInFront(Node<K> node){
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }
}
