import java.util.*;


public class BoggleSolver
{
	private TrieSET dict;
	private Set<String> validWords;
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
    	dict=new TrieSET();
    	for(String s:dictionary)
    		dict.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
    	validWords=new HashSet<String>();
    	int M=board.rows();
    	int N=board.cols();
    	
    	Graph G=toGraph(board);
    	
    	for(int v=0;v<G.V();v++){
    		dict.resetDict();
    		dfs(board,G,v,new boolean[M*N]);
    	}
    	return validWords;
    }

    

	private void dfs(BoggleBoard board, Graph G, int v, boolean[] marked) {
		int N=board.cols();
		char c=board.getLetter(v/N, v%N);
		
		if(!dict.addOneChar(c)) return;
		if(c=='Q'){
			if(!dict.addOneChar('U')){
				dict.removeOneChar();
				return;
			}
		}
		marked[v]=true;
		String word=dict.pathToString();
		if(word.length()>2&&dict.isWord()) validWords.add(word);
		
		for(int w:G.adj(v)){
			if(!marked[w])
				dfs(board,G,w,marked);
		}
		
		marked[v]=false;
		dict.removeOneChar();
		if(c=='Q') dict.removeOneChar();
	}

	// Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
    	if (!dict.contains(word)) return 0;
        if (word.length() <= 2) return 0;
        if (word.length() == 3 || word.length() == 4) return 1;
        if (word.length() == 5) return 2;
        if (word.length() == 6) return 3;
        if (word.length() == 7) return 5;
        return 11;
    }
    
    /*
     * helper 
     */
    
    private Graph toGraph(BoggleBoard board) {
    	int M=board.rows();
    	int N=board.cols();
    	Graph G=new Graph(M*N);
    	boolean[] marked=new boolean[M*N];
    	for(int i=0;i<M*N;i++){
    		marked[i]=true;
    		for(int adj:getAdjecent(board,i,M,N))
    			if(!marked[adj]) G.addEdge(i, adj);
    	}
    	return G;
	}
    
    private Bag<Integer> getAdjecent(BoggleBoard board, int i,int M,int N) {
		Bag<Integer> neighbors=new Bag<Integer>();
		int x=i%N,y=i/N;
		for(int p=x-1;p<=x+1;p++){
			for(int q=y-1;q<=y+1;q++){
				if(p>=0&&p<N&&q>=0&&q<M){
					if(p==x&&q==y) continue;
					neighbors.add(xyTo1D(p,q,N));
				}
			}
		}
		return neighbors;
	}

	private int xyTo1D(int x, int y,int N) {
		// TODO Auto-generated method stub
		return y*N+x;
	}

	public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}