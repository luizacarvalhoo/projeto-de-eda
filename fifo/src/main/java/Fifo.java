public class FIFOCache {
    private String[] cache;  
    private int capacidade; 
    private int head;       
    private int tail;       
    private int tamanho;    
    private int hit;        
    private int miss;       

    
    public FIFOCache(int capacidade) {
        this.capacidade = capacidade;
        this.cache = new String[capacidade];
        this.head = -1;
        this.tail = -1;
        this.tamanho = 0;
        this.hit = 0;
        this.miss = 0;
    }
    
    //recebe string pois podemos recebemos do scanner
    // tem dois metodos auxiliares de verificacao
    public void adicionar(String item) {
        if (estaCheio()) {
            // Se o cache está cheio, remove o item mais antigo (head indicado pelo indice)
            removerPrimeiro();
        }

        if (estaVazio()) {
            // Se o cache está vazio, inicializa head e tail
            head = 0;
        }

        // Adiciona o novo item no final do cache  utilizando resto
        tail = (tail + 1) % capacidade;
        cache[tail] = item;
				

    }
        
    public boolean estaVazio() {
			  return head == -1 && tail == -1;
    }

    // pega a posicao do tail 
    public boolean estaCheio() {
        return ((tail + 1) % capacidade) == head;
    }
    
    //busca 
    public boolean contem(String item) {
        if (estaVazio()) {
            return false;
        }

        int i = head;
        while (true) {
            if (cache[i].equals(item)) {
                hit++; // Incrementa o contador de hits
                return true;
            }
            if (i == tail) break;
            i = (i + 1) % capacidade;
        }

        miss++; // Incrementa o contador de misses
        return false;
    }
    
    
    public String exibir() {
        String retorno = "";
        if (estaVazio()) {
		        retorno = "Cache vazio."
            return retorno ;
        }
				
       retorno = "Cache -  \n";
        int i = head;
        while (true) {
            retorno +=" " + cache[i] + " \n";
            if (i == tail) break;
            i = (i + 1) % capacidade;
        }
        
    }
    
    
    private String removerPrimeiro() {
        String retorno = "";
        if (estaVazio()) {
		        retorno = "Cache vazio."
            return retorno ;
        }
				String itemRemovido = cache[head];
        if (head == tail) {
            // Se tem apenas um item no cache, reseta head e tail
            head = -1;
            tail = -1;
        } else {
            // Move head para o próximo item ( fila circular )
            head = (head + 1) % capacidade;
        }

        tamanho--;
        return itemRemovido;
    }

        
}

public static void main(String[] args) {
        FIFOCache cache = new FIFOCache(3); // Cria um cache com capacidade para 3 itens

        cache.adicionar("A");
        cache.adicionar("B");
        cache.adicionar("C");
        cache.exibir(); // Cache: A B C

        cache.adicionar("D"); // Substitui o item mais antigo (A)
        cache.exibir(); // Cache: B C D

        System.out.println("Removido: " + cache.removerPrimeiro()); // Remove B
        System.out.println(cache.exibir()); // Cache: C D
    }


//explicacao no notion/DOCS
