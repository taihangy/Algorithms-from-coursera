import java.util.Iterator; 

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] a;  // Array of items. 
	private int N;    // Size of randomized queue. 
	
    // Initializes an empty queue.
	public RandomizedQueue() { 
		a = (Item[]) new Object[2];  // Casting used since generic arrays aren't allowed in java.
	}
	
	// Checks if deque is empty. 
	public boolean isEmpty() { 
		return N == 0; 
	}
	
	// Returns the size of queue. 
	public int size() { 
		return N; 
	}
	
	// Resizes the array dynamically to make or reduce space 
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity]; 
		for (int i=0; i < N; i++) {
			copy[i] = a[i]; 
		}
		a = copy; 		
	}
	
	// Adds an item to the front of the queue. 
	public void enqueue(Item item) {
		if (item == null) throw new java.lang.NullPointerException("Cannot add null items.");
		// Resizes array by doubling when the current array is full.
		if (N == a.length) resize(2*a.length);   
		a[N++] = item; 
	}
	
	// Removes a random item from the queue.  
	public Item dequeue() {
		if (isEmpty()) throw new java.util.NoSuchElementException("Randomized queue is empty.");
		int randomIndex = StdRandom.uniform(N); 
		Item item = a[randomIndex];
		a[randomIndex] = a[N-1];  // Replaces the item to be removed with the last item in array to fill the null hole. 
		a[N-1] = null;   // Last item in array becomes null instead. 
		N--; 
		// Resizes array by halving it when number of items in array is 1/4 of array length.
		if (N > 0 && N == a.length/4) resize(a.length/2);  
		return item; 
	}
	
	// Returns a random item without deleting it. 
	public Item sample() { 
		if (isEmpty()) throw new java.util.NoSuchElementException("Randomized queue is empty."); 
		int randomIndex = StdRandom.uniform(N); 
		return a[randomIndex];  		
	}
	
	// Returns an iterator object used for iteration through elements in queue. 
	public Iterator<Item> iterator() { 
		return new RQIterator(); 
	}
	
	// Implementation of iterator object. 
	private class RQIterator implements Iterator<Item> {
		private int i = N; 
		private Item[] copy; 
		
		// Constructor creates a copy with items from the original array and randomizes items in it by shuffling.  
		public RQIterator() {
			copy = (Item[]) new Object[N]; 
			for (int i=0; i < N; i++) {
				copy[i] = a[i]; 
			}
			StdRandom.shuffle(copy); 
		}
		
		public boolean hasNext() {
			return i > 0; 
		}
		
		public Item next() { 
			if (!hasNext()) throw new java.util.NoSuchElementException("End of randomized queue reached.");
			return copy[--i]; 
		}
		
		public void remove() { 
			throw new java.lang.UnsupportedOperationException("Remove operation is not suppored."); 
		}						
	}
	
}