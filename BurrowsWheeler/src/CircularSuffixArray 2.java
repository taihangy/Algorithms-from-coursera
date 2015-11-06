public class CircularSuffixArray {
	
	private final int R = 256;
	private final String str;
	private int[] index;
	private int[] rank;
	
    public CircularSuffixArray(String s){
    	// circular suffix array of s
    	if(s == null)
    		throw new NullPointerException();
    	str = s;
    	index = new int[s.length()];
    	rank = new int[s.length()];
    	sort();
    }
    
    /**Get the char at a specific position in a specific CircularSuffix
     * @param strId the id of the CircularSuffix
     * @param index the postion in the CircularSuffix string
     */
    private char charAt(int strId, int index){
    	if(strId+index < str.length())
    		return str.charAt(strId + index);
    	else
    		return str.charAt(strId+index-str.length());
    }
    
    /**Sort the circular suffixs' first characters using the key index counting algorithm, updating the index and rank array
     * @param queue the queue which contains sub arrays with same prefix
     */
    private void keyIndexConunting(LinkedQueue<Integer> queue){
    	int[] count = new int[R+1];
    	for(int i=0; i<str.length(); i++)
    		count[str.charAt(i)+1]++;
    	for(int i=1; i<R+1; i++){
    		if(count[i] > 1){
    			queue.enqueue(count[i-1]);
    			queue.enqueue(count[i-1]+count[i]-1);
    		}
    		count[i] += count[i-1];
    	}
    	for(int i=0; i<str.length(); i++){
    		index[count[str.charAt(i)]] = i;
    		rank[i] = count[str.charAt(i)];
    		if(rank[i]>0 && str.charAt(index[rank[i]-1]) == str.charAt(i))
    			rank[i] = rank[index[rank[i]-1]];
    		count[str.charAt(i)]++;		
    	}
    }
    
    /**Compare the N ~ 2N-1 characters of two CircularSuffix strings.
     * @param id1 the id of the first CircularSuffix string
     * @param id2 the id of the second CircularSuffix string
     * @param N the number of characters to compare
     * @return 1 if first suffix is larger than the second one, 0 if equal, -1 if less
     */
    private int compareSuffix(int id1, int id2, int N){
    	int index1 = id1 + N;
    	int index2 = id2 + N;
    	if(index1 >= str.length())
    		index1 -= str.length();
    	if(index2 >= str.length())
    		index2 -= str.length();
    	if(rank[index1] > rank[index2])
    		return 1;
    	else if(rank[index1] == rank[index2])
    		return 0;
    	else
    		return -1;
    }
    
    /**Exchange two CircularSuffixes in the array
     * @param index1 the first index in the array
     * @param index2 the second index in the array
     */
    private void exchange(int index1, int index2){
    	int temp = index[index1];
    	index[index1] = index[index2];
    	index[index2] = temp;   	
    }

    
    /**Sort the N ~ 2N-1 characters of circular suffixes using three-way quick sort, while updating the rank
     * @param start the start position of array
     * @param end the end position of array
     * @param N indicate which charaters to sort
     * @param newRank the rank array to update
     * @param queue the queue which contains sub arrays with same prefix
     */
    private void threeWaySuffixQuickSort(int start, int end, int N, int[] newRank, LinkedQueue<Integer> queue){
    	if(start > end) return;
    	else if(start == end){
    		newRank[index[start]] = start;
    		return;
    	}
    	int lo = start;
    	int i = lo+1;
    	int hi = end;
    	while(hi >= i){
    		int comp = compareSuffix(index[i], index[lo], N);
    		if(comp < 0)
    			exchange(i++, lo++);
    		else if(comp == 0)
    			i++;
    		else
    			exchange(i, hi--);
    	}
    	for(int j=lo; j<=hi; j++)
    		newRank[index[j]] = lo;
    	if(hi > lo){
    		queue.enqueue(lo);
    		queue.enqueue(hi);
    	}
    	threeWaySuffixQuickSort(start, lo-1, N, newRank, queue);
    	threeWaySuffixQuickSort(hi+1, end, N, newRank, queue);
    }
    
    //Sort the circular suffix arrays
    private void sort(){
    	LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
    	keyIndexConunting(queue);
    	int[] newRank = new int[str.length()];
    	int N = 1;
    	while(N < str.length()){
    		LinkedQueue<Integer> newQueue = new LinkedQueue<Integer>();
    		for(int i=0; i<rank.length; i++)
        		newRank[i] = rank[i];
	    	while(!queue.isEmpty())
	    		threeWaySuffixQuickSort(queue.dequeue(), queue.dequeue(), N, newRank, newQueue);
	    	assert(queue.isEmpty());
	    	queue = newQueue;
	    	int[] temp = rank;
	    	rank = newRank;
	    	newRank = temp;
	    	N = N*2;
    	}
    	
    }
    
    public int length(){
    	// length of s
    	return str.length();
    }
    
    public int index(int i){
    	// returns index of ith sorted suffix
    	if(i<0 || i>=str.length())
    		throw new IndexOutOfBoundsException();
    	return index[i];
    }
   
    public String toString(){
    	String reault = "";
    	for(int i=0; i<str.length(); i++){
    		reault += '\n';
    		for(int j=0; j<str.length(); j++)
    			reault += charAt(index[i],j);
    	}
    	return reault;
    }
    
    public static void main(String[] args){
    	// unit testing of the methods (optional)
    	CircularSuffixArray csa = new CircularSuffixArray(args[0]);
    	System.out.println(csa);
    }
}