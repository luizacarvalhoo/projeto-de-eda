import java.util.HashMap;

/*
 * Implementação do Least Recently Used Cache usando Doubly Linked List e HashMap.
*/

public class LRU<K, V> {

    private final int capacity; // capacidade do cache
    private final HashMap<K, Node<K, V>> cache; // mapa que armazena o dado (key) e o nó no qual esse dado se encontra (value)
    private final Node<K, V> head, tail;  // nós que fornecem a hierarquia de frequencia de uso dos dados armazenados no cache  
    
    public LRU(int capacity) {
        this.capacity = capacity;

        // inicialização do mapa de cache
        this.cache = new HashMap<K, Node<K, V>>();

        // inicialização dos nós de início e de fim da DLL
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    // método responsável por adicionar um nó à dll
    public void addNode(Node<K, V> node) {
        node.next = this.head.next;
        node.prev = this.head;
        this.head.next.prev = node;
        this.head.next = node;
    }

    // método responsável por remover um nó da dll
    public void removeNode(Node<K, V> node) {
        // é necessário melhorar esse código no quesito legibilidade?
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    // método responsável de mover o dado mais usado para frente
    public void moveMostRecentlyUsedNode(Node<K, V> node) {
        removeNode(node);
        addNode(node);
    }
    
    // verificar se isso pode ser mantido!!!!!!
    // verifica se o dado procurado está no cache
    public V get(K key) {
        if (this.cache.containsKey(key)) {
            // caso no qual houve um cache hit e temos que mover o nó para frente.
            Node<K, V> cacheHit = this.cache.get(key);
            moveMostRecentlyUsedNode(cacheHit); // move o nó para frente da dll
            return cacheHit.value;
        }
        // já nesse caso, houve um cache miss, pois não achamos aquela chave no cache.
        return null; 
    }
    
    // esse método é responsável por adicionar/remover ou mover nós baseado em cache hit or miss
    public void put(K key, V value) {

        if (this.cache.containsKey(key)) {
            // como o dado já se encontra no cache, temos que atualizar sua frequência
            Node<K, V> cacheHit = this.cache.get(key);
            cacheHit.value = value;  // atualiza o valor do nó
            moveMostRecentlyUsedNode(cacheHit); // move o nó para frente da dll
        } else {
            // verifica se a capacidade do cache foi atingida
            if ((cache.size() + 1) > capacity) {

                // capacidade máxima atingida e nó LRU é removido do cache para que o novo nó possa ser adicionado
                Node<K, V> leastRecentlyUsed = this.tail.prev;
                removeNode(leastRecentlyUsed);
                this.cache.remove(leastRecentlyUsed.key);
            }
            // criamos um novo nó e adicionamos-o ao cache, já que
            // se o cache estivesse cheio, o nó LFU teria sido removido e
            // caso tenha espaço, é possível só adicionar direto
            Node<K, V> newNode = new Node<>(key, value);
            this.cache.put(key, newNode);
            addNode(newNode);
        }
    }

    // apenas para melhor visualização do cache!!!
    public void printCache() {
        System.out.print("head -> ");
        Node<K, V> currentNode = this.head.next;
        while (currentNode != this.tail) {
            System.out.print(currentNode.key + ":" + currentNode.value + " -> ");
            currentNode = currentNode.next;
        }
        System.out.println("tail");
    }

}
