
public class Subset {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]); 
		// Uses randomized queue as the primary data structure for this program. 
		RandomizedQueue<String> rq = new RandomizedQueue<String>(); 
		while(!StdIn.isEmpty()) {
		    rq.enqueue(StdIn.readString()); 
		}
		for (int i = 0; i < k; i ++) {
			StdOut.println(rq.dequeue()); 
		}
	}
}
