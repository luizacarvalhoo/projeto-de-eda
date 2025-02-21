public class FIFOCache<T> {
    private T[] cache;  
    private int capacity; 
    private int head;       
    private int tail;       
    private int size;    
      

	
    public FIFOCache(int capacity) {
        this.capacity = capacity;
        this.cache = new T[capacity];
        this.head = -1;
        this.tail = -1;
        this.size = 0;

    }
    

    public boolean isEmpty() {
        return head == -1 && tail == -1;
    }


    public boolean isFull() {
    return ((this.tail + 1) % this.capacity) == this.head;
    }
    
    
    public void add(T item) {
        if (isFull()) {
        
            removeFirst();
        }

        if (isEmpty()) {
            
            this.head = 0;
            this.tail = 0;
        }

        
        this.tail = (this.tail + 1) % this.capacity;
        this.cache[tail] = item;
		this.size++;

    }
        
    
    public int indexOf(String element) {
        for (int i = head; i <= tail; i += 1 % this.fila.length) {
            if (this.fila[i].equals(element))
                return i;
        }
        return -1;
    }

 
    public boolean contains(T item) {
        if (isEmpty()) {
            return false;
        }

        int i = this.head;
        while (true) {
            if (cache[i].equals(item)) {
             
                return true;
            }
            if (i == this.tail) break;
            i = (i + 1) % this.capacity;
        }

         
        return false;
    }
    
    
    public String toString() {
        String output ;

        if (isEmpty()) {
            output = "Cache vazio.";
            return output ;
        }
				
        output = "Cache -  \n";
        
        int i = this.head;
        while (true) {
            output +=" " + this.cache[i] + " \n";
            
            if (i == this.tail) break;
            
            i = (i + 1) % this.capacity;
        }
        
    }
    
    
    public T removeFirst() {
        String retorno = "";
        if (isEmpty()) throw new IllegalArgumentException("Cache vazio.");
		       
		T removedItem = this.cache[this.head];
        if (this.head == this.tail) {
            
            this.head = -1;
            this.tail = -1;
        } else {
            
            this.head = (this.head + 1) % this.capacity;
        }

        this.size--;
        return removedItem;
    }
  
}


//explicacao no notion/DOCS
