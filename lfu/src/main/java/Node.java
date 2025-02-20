package main.java;

public class Node<K, V> {
    K key;  // identificador do dado armazenado
    V value;  // dado armazenado
    int freq;  // frequencia de uso do nó

    Node<K, V> prev;  // informação sobre o nó anterior
    Node<K, V> next;  // informação sobre o próximo nó

    public Node(K key, V value){
        this.key = key;
        this.value = value;
        this.freq = 1;
    }
}