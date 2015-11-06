import java.util.*;

public class WordNet {
	private ArrayList<Queue<String>> nouns=null;
	private Digraph G;
	private HashMap<String,Bag<Integer>> hashmap;
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) throws NullPointerException, IllegalArgumentException{
		if(synsets==null||hypernyms==null) throw new NullPointerException();
		nouns=new ArrayList<Queue<String>>();
		In in=new In(synsets);
		while(in.hasNextLine()){
			String s=in.readLine();
			String[] tokens=s.split(",");
			String[] noun=tokens[1].split(" ");
			Queue<String> q=new Queue<String>();
			for(String item:noun){
				q.enqueue(item);
			}
			nouns.add(q);
		}
		G=new Digraph(nouns.size());
		In in1=new In(hypernyms);
		while(!in1.isEmpty()){
			String s=in1.readLine();
			String tokens[]=s.split(",");
			int root=Integer.parseInt(tokens[0]);
			int i=0;
			for(String item:tokens){
				if(i==0){
					i++;
					continue;
				}
				G.addEdge(root, Integer.parseInt(item));
			}
		}
		
		DirectedCycle dc=new DirectedCycle(G);
		if(dc.hasCycle()) throw new IllegalArgumentException();
		
		int rootCount=0;
		for(int v=0;v<G.V();v++){
			int adjacent=0;
			for(int w:G.adj(v))
				adjacent++;
			if(adjacent==0)
				rootCount++;
		}
		if(rootCount!=1) throw new IllegalArgumentException();
		findNounsAndTheirVertex();
		sap=new SAP(G);
	}

	private void findNounsAndTheirVertex() {
		hashmap=new HashMap<String,Bag<Integer>>();
		int i=0;
		for(Queue<String> verticeNouns:nouns){
			for(String verticeNoun:verticeNouns){
				Bag<Integer> verticeBag=(Bag<Integer>)hashmap.get(verticeNoun);
				if(verticeBag!=null){
					verticeBag.add(i);
				}else{
					verticeBag=new Bag<Integer>();
					verticeBag.add(i);
					hashmap.put(verticeNoun,verticeBag);
				}
			}
			i++;
		}
		
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return hashmap.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) throws NullPointerException{
		if(word==null) throw new NullPointerException();
		return hashmap.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) throws IllegalArgumentException{
		if(!this.isNoun(nounA)||!this.isNoun(nounB)) throw new IllegalArgumentException();
		Bag<Integer> nounAVertices=(Bag<Integer>)hashmap.get(nounA);
		Bag<Integer> nounBVertices=(Bag<Integer>)hashmap.get(nounB);
		return sap.length(nounAVertices, nounBVertices);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) throws IllegalArgumentException{
		if(!this.isNoun(nounA)||!this.isNoun(nounB)) throw new IllegalArgumentException();
		Bag<Integer> v=(Bag<Integer>)hashmap.get(nounA);
		Bag<Integer> w=(Bag<Integer>)hashmap.get(nounB);
		int ancestor=sap.ancestor(v, w);
		return join(nouns.get(ancestor)," ");
	}

	private String join(Queue<String> queue, String delimiter) {
		StringBuilder sb=new StringBuilder();
		for(String str:queue){
			sb.append(str);
			sb.append(delimiter);
		}
		return sb.toString();
	}

	// do unit testing of this class
	public static void main(String[] args) {

	}
}
