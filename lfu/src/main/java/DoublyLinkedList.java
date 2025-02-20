package main.java;

public class DoublyLinkedList<K, V> {

    Node<K, V> head, tail;
    
    public DoublyLinkedList() {
        this.head = new Node<>(null, null);  // Dummy head
        this.tail = new Node<>(null, null);  // Dummy tail
        this.head.next = tail;
        this.tail.prev = head;
    }
    
    public void addToFront(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public void remove(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Remove the least recently used node (tail's previous node)
    public Node<K, V> removeLast() {
        if (this.head.next == this.tail) {
            return null;
        }
        
        Node<K, V> last = this.tail.prev;
        remove(last);
        return last;
    }
}