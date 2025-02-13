/* 
O objetivo dessa classe é criar e armazenar informações importantes para a doubly linked list
 */

 public class Node<K, V> {
    K key;  // identificador do dado armazenado
    V value;  // dado armazenado

    Node<K, V> prev;  // informação sobre o nó anterior
    Node<K, V> next;  // informação sobre o próximo nó

    public Node(K key, V value){
        this.key = key;
        this.value = value;
    }
}