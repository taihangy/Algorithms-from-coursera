
public class Solver {
	private SearchNode result;
	
	public Solver(Board initial) throws NullPointerException{
		// find a solution to the initial board (using the A* algorithm)
		if(initial==null) throw new NullPointerException();
		result=solveIt(new SearchNode(initial,null));
	}
	
	private SearchNode solveIt(SearchNode searchNode) {
		SearchNode sn=searchNode; 
		MinPQ<SearchNode> pq=new MinPQ<SearchNode>();
		pq.insert(sn);
		
		SearchNode twinsn=searchNode.twin();
		MinPQ<SearchNode> twinpq=new MinPQ<SearchNode>();
		twinpq.insert(twinsn);
		
		while(true){
			sn=calculateOneStep(pq);
			if(sn.isGoal()) return sn;
			if(calculateOneStep(twinpq).isGoal()) return null;
		}
	}

	private SearchNode calculateOneStep(MinPQ<SearchNode> pq) {
		SearchNode result=pq.delMin();
		for(Board neighbor:result.board.neighbors()){
			if(result.prev==null||!neighbor.equals(result.prev.board)){
				pq.insert(new SearchNode(neighbor,result));
			}
		}
		return result;
	}

	private class SearchNode implements Comparable<SearchNode>{
		private final Board board;
		private SearchNode prev;
		private final int move, priority;
		
		public SearchNode(Board board,SearchNode prev){
			this.board=board;
			this.prev=prev;
			if(prev==null) this.move=0;
			else	this.move=prev.move+1;
			this.priority=this.board.manhattan()+this.move;
		}
		
		public int compareTo(SearchNode that) {
			if(this.priority<that.priority)
				return -1;
			else if(this.priority>that.priority)
				return 1;
			else
				return 0;
		}
		
		public boolean isGoal(){
			return this.board.isGoal();
		}
		
		public SearchNode twin(){
			return new SearchNode(this.board.twin(),this.prev);
		}
		
	}

	public boolean isSolvable() {
		// is the initial board solvable?
		return result!=null;
	}

	public int moves() {
		// min number of moves to solve initial board; -1 if unsolvable
		if(this.isSolvable())
			return result.move;
		else
			return -1;
	}

	public Iterable<Board> solution() {
		// sequence of boards in a shortest solution; null if unsolvable
		if(!this.isSolvable()) return null;
		Stack<Board> s=new Stack<Board>();
		for(SearchNode sn=result;sn!=null;sn=sn.prev){
			s.push(sn.board);
		}
		return s;
	}

	public static void main(String[] args) {
		// solve a slider puzzle (given below)
		// create initial board from file
        In in = new In(args[0]);
        //String path = "E:/Coursera/Algorithms, Part I/Week 4/Assignment/8puzzle/src/puzzle34.txt";
        //In in = new In(path);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        Stopwatch time1 = new Stopwatch();

        // solve the puzzle
        Solver solver = new Solver(initial);        

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        StdOut.println("Time escape:   " + time1.elapsedTime());
	}
}