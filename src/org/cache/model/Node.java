package org.cache.model;

public class Node<K> {
    public K key;
    public Node<K> prev;
    public Node<K> next;

    public Node(K key){
        this.key = key;
    }
}
