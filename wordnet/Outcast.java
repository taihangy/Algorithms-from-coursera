public class Outcast {
	private WordNet wordNet;
	public Outcast(WordNet wordnet) {
		// constructor takes a WordNet object
		this.wordNet=wordnet;
	}

	public String outcast(String[] nouns) {
		// given an array of WordNet nouns, return an outcast
		int maxDistance=0;
		int distance,index=0;
		for(int i=0;i<nouns.length;i++){
			distance=0;
			for(int j=0;j<nouns.length;j++){
				if(j==i) continue;
				distance=distance+wordNet.distance(nouns[i], nouns[j]);
			}
			if(distance>maxDistance){
				maxDistance=distance;
				index=i;
			}
		}
		return nouns[index];
	}

	public static void main(String[] args) {
		// see test client below
	}
}
