import java.util.HashMap;

// implementação usando Doubly Linked List e HashMap
// essa implementação é preparada para receber dados inteiros apenas

public class LRU {

    private final int capacity; // capacidade do cache
    private final HashMap<Integer, Node> cache; // mapa que armazena o dado (key) e o nó no qual esse dado se encontra (value)
    private final Node head, tail;  // nós que fornecem a hierarquia de frequencia de uso dos dados armazenados no cache  
    
    public LRU(int capacity) {
        this.capacity = capacity;

        // inicialização do mapa de cache
        this.cache = new HashMap<Integer, Node>();

        // inicialização dos nós de início e de fim da DLL
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    // método responsável por adicionar um nó à dll
    public void addNode(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    // método responsável por remover um nó da dll
    public void removeNode(Node node) {
        // é necessário melhorar esse código no quesito legibilidade?
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    // método responsável de mover o dado mais usado para frente
    public void moveMostRecentlyUsedNode(Node node) {
        removeNode(node);
        addNode(node);
    }
    
    // verifica se o dado procurado está no cache
    public int get(int key) {
        if (cache.containsKey(key)) {
            // caso no qual houve um cache hit e temos que mover o nó para frente.
            Node cacheHit = cache.get(key);
            moveMostRecentlyUsedNode(cacheHit); // move o nó para frente da dll
            return cacheHit.value;
        }
        // já nesse caso, houve um cache miss, pois não achamos aquela chave no cache.
        return -1; 
    }
    
    // esse método é responsável por adicionar/remover ou mover nós baseado em cache hit or miss
    public void put(int key, int value) {

        if (cache.containsKey(key)) {
            // como o dado já se encontra no cache, temos que atualizar sua frequência
            Node cacheHit = cache.get(key);
            moveMostRecentlyUsedNode(cacheHit); // move o nó para frente da dll
        } else {
            // verifica se a capacidade do cache foi atingida
            if ((cache.size() + 1) > capacity) {

                // capacidade máxima atingida e nó LRU é removido do cache para que o novo nó possa ser adicionado
                Node leastRecentlyUsed = tail.prev;
                removeNode(leastRecentlyUsed);
                cache.remove(leastRecentlyUsed.key);
            }
            // criamos um novo nó e adicionamos-o ao cache, já que
            // se o cache estivesse cheio, o nó LFU teria sido removido e
            // caso tenha espaço, é possível só adicionar direto
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addNode(newNode);
        }
    }

    // apenas para melhor visualização do cache!!!
    public void printCache() {
        System.out.print("head -> ");
        Node noAtual = head.next;
        while (noAtual != tail) {
            System.out.print(noAtual.key + ":" + noAtual.value + " -> ");
            noAtual = noAtual.next;
        }
        System.out.println("tail");
    }

}