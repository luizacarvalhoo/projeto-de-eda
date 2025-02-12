import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    public  final int capacidade;
 
    public LRUCache(int capacidade) {
	super(capacidade, 0.75f, true);
        this.capacidade = capacidade;
    }
 
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.capacidade; // Remove o elemento mais antigo
    }





}


