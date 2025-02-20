package main.java;

import java.util.HashMap;

/*
 * Implementação do Least Frequently Used Cache usando uma DLL e dois HashMap, um para armazenar os dados e outro para armazenar a frequência.
 */
public class LFU<K, V> {

    private final int capacity;  // capacidade do cache
    private int minFreq;  // mantém a menor frequência de uso 
    private HashMap<K, Node<K, V>> cache;  // mapeia a chave ao nó que contem a chave e o valor
    private HashMap<Integer, DoublyLinkedList<K, V>> freqMap;  // mapeia a frêquencia de uso as suas respectivas listas

    public LFU(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;

        // inicialização dos mapas de cache e frequência
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public V get(K key) {
        if (this.cache.containsKey(key)) {
            Node<K, V> cacheHit = this.cache.get(key); 
            updateFreq(cacheHit);
            return cacheHit.value;  // o elemento requisitado está no cache, cache hit
        }

        return null;  // o elemento buscado não está no cache, cache miss
    }

    public void put(K key, V value) {
        // atualiza o valor e a frequencia do nó
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            updateFreq(node);
        } else {
            // se o cache tiver cheio, remove-se o elemento de menor frequência 
            if (this.cache.size() == this.capacity) {
                removeLFU();
            }

            // cria-se um novo nó para adicionar no cache
            Node<K, V> newNode = new Node<>(key, value);
            this.cache.put(key, newNode);

            this.minFreq = 1; // atualiza-se o valor da frequencia mínima

            if (!this.freqMap.containsKey(minFreq)) {
                // se o mapa de frequencia não contém uma lista de freq min, cria-se 
                this.freqMap.put(minFreq, new DoublyLinkedList<>());
            }

            this.freqMap.get(minFreq).addToFront(newNode);
        }
    }

    
    private void removeLFU() {
        DoublyLinkedList<K, V> list = this.freqMap.get(this.minFreq);

        // Remove the least recently used node from the list (tail's previous node)
        Node<K, V> node = list.removeLast();
        
        if (node != null) {
            this.cache.remove(node.key);
        }
    }

    private void updateFreq(Node<K, V> node) {
        int freq = node.freq;
        this.freqMap.get(freq).remove(node);

        // se esse nó era o último da lista e sua frequencia era a mínima, atualiza-se o valor da freq min
        DoublyLinkedList<K, V> freqList = this.freqMap.get(freq);
        if ((freq == this.minFreq) && freqList.head.next == freqList.tail) {
            this.minFreq++;
        }

        node.freq++; // atualiza a freq do nó

        // adiciona o nó a sua respectiva lista de frequência
        if (!this.freqMap.containsKey(node.freq)) {
            this.freqMap.put(node.freq, new DoublyLinkedList<>());
        }
        this.freqMap.get(node.freq).addToFront(node);
    }

}