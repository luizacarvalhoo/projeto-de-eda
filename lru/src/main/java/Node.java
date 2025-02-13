/* 
O objetivo dessa classe é criar e armazenar informações importantes para a doubly linked list
 */

 public class Node {
    int key;  // identificador do dado armazenado, nesse contexto de implementação vai ser o dado propriamente dito
    int value;  // dado armazenado

    Node prev;  // informação sobre o nó anterior
    Node next;  // informação sobre o próximo nó

    public Node(int key, int value){
        this.key = key;
        this.value = value;
    }
}