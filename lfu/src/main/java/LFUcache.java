import java.util.*;

public class LFUcache < K,V >{


    // Limita o tamanho do cache.
    private int capacity;

    // Armazenamento e a recuperação dos dados reais associados a uma chave.
    private Map <K, V> cache;

    // Quantas vezes cada chave foi acessada, objeto associado a frequencia.
    private Map <K, Integer> frequency;

    // frequencia associada ao linked que preserva a ordem de inserção, é possível verificar entre as frequencias, qual foi a ultima chave inserida 
    private Map <Integer, LinkedHashSet<K> > frequencyGroups;

    // a menor frequencia atual
    private int minFrequency;


    public LFUcache(int capacity){
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequency = new HashMap<>();
        this.frequencyGroups = new HashMap<>();
        this.minFrequency = 0;
    }

    public V get(K key) {

        // verifica se o objeto existe no cache
        if (!cache.containsKey(key)){
            return null;
        }
        atualizaFrequencia(key);

        return cache.get(key);

    }

    public void put(K key,V Value){
        if(capacity == 0) return;

        // se a chave existe eu preciso adicionar o novo valor associado a ela no cache.
        if(cache.containsKey(key)){
            cache.put(key, Value);
            atualizaFrequencia(key);
            return;
        }
        if (cache.size() >= capacity){
            excluirItem();
        }
        // adiciona o k,v no cache 
        cache.put(key,Value);

        // adiciona dentro das frequencia a quantas vezes a chave foi acessada
        frequency.put(key,1);


        frequencyGroups.existeFrequencia1(key);

    }

    private void existeFrequencia1(K key){
        
        if(!frequencyGroups.containsKey(1)){
            frequencyGroups.put(1, new LinkedHashSet<K>());
            minFrequency = 1;
        }
        frequencyGroups.get(1).add(key);
    }

    private void atualizaFrequencia(K key) {
        int frequencia =  frequency.get(key);
        
        frequency.put(key, frequencia + 1);

        frequencyGroups.get(frequencia).remove(key);

        // preciso verificar se a frequencia 
        if (frequencia == minFrequency && frequencyGroups.get(frequencia).isEmpty()) {
            minFrequency++;
            frequencyGroups.remove(frequencia); 

        }
        
        frequencyGroups.computeIfAbsent(frequencia + 1, k -> new LinkedHashSet<>()).add(key);

    }

    private void excluirItem() {

        // pega o linkedHashSet associado a menor frequencia:
        LinkedHashSet<K> grupoMenorFrequencia = frequencyGroups.get(minFrequency); 

        // utiliza o linkedHashSet anterior para verificar qual foi o primeiro adicionado, não aceita acesso indexado
        K chaveRemovida = grupoMenorFrequencia.iterator().next();

        grupoMenorFrequencia.remove(chaveRemovida);

        if (grupoMenorFrequencia.isEmpty()){
            // eu preciso tratar alguma coisa aqui ?
            frequencyGroups.remove(minFrequency);

        }
        cache.remove(chaveRemovida);
        frequency.remove(chaveRemovida);
    }
}