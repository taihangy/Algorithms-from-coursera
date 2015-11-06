import java.util.*;

public class BaseballElimination {
	private List<String> teams;
	private int[] w, l, r;
	private int[][] g;
	private final int tNum;
	private List<Integer> R;
	private boolean isEliminatedExec;

	public BaseballElimination(String filename) {
		// create a baseball division from given filename in format specified
		// below
		if (filename == null || filename.length() == 0)
			throw new IllegalArgumentException("please enter right filename");
		In in = new In(filename);
		this.tNum = in.readInt();
		this.teams = new ArrayList<String>();
		this.w = new int[tNum];
		this.l = new int[tNum];
		this.r = new int[tNum];
		this.g = new int[tNum][tNum];
		int i = 0;
		while (!in.isEmpty()) {
			teams.add(in.readString());
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < tNum; j++)
				g[i][j] = in.readInt();
			i++;
		}
	}

	public int numberOfTeams() {
		// number of teams
		return tNum;
	}

	public Iterable<String> teams() {
		// all teams
		return teams;
	}

	public int wins(String team) {
		// number of wins for given team
		int index = checkTeam(team);
		return w[index];
	}

	public int losses(String team) {
		// number of losses for given team
		int index = checkTeam(team);
		return l[index];
	}

	public int remaining(String team) {
		// number of remaining games for given team
		int index = checkTeam(team);
		return r[index];
	}

	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		int index1 = checkTeam(team1);
		int index2 = checkTeam(team2);
		return g[index1][index2];
	}

	public boolean isEliminated(String team) {
		// is given team eliminated?
		int index = checkTeam(team);
		isEliminatedExec = true;
		return computeR(index);
	}

	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		int index = checkTeam(team);
		if (!isEliminatedExec)
			computeR(index);

		isEliminatedExec = false;
		if (R.isEmpty())
			return null;
		ArrayList<String> res = new ArrayList<String>();
		for (int i : R)
			res.add(teams.get(i));
		return res;
	}

	/*
	 * helper function
	 */

	private boolean computeR(int x) {
		// TODO Auto-generated method stub
		R = new ArrayList<Integer>();
		// trivial elimination
		for (int i = 0; i < tNum; i++)
			if (i != x && w[x] + r[x] < w[i]) {
				R.add(i);
				return true;
			}

		// non-trivial elimination
		int count = tNum * tNum + tNum + 2;
		FlowNetwork fn = buildNetwork(x, count);
		FordFulkerson ff = new FordFulkerson(fn, count - 2, count - 1);

		for (int i = 0; i < tNum; i++) {
			if (ff.inCut(i))
				R.add(i);
		}
		return w[x] + r[x] < averageR(x);
	}

	private double averageR(int x) {
		int wSum = 0, gSum = 0;
		boolean[][] gMarked = new boolean[tNum][tNum];
		for (int i : R) {
			wSum += w[i];
			for (int j = 0; j < tNum; j++) {
				if (j != x && j != i && !gMarked[i][j]) {
					gSum += g[i][j];
					gMarked[j][i] = true;
				}
			}
		}
		return (double) (wSum + gSum) / R.size();
	}

	private FlowNetwork buildNetwork(int x, int count) {
		boolean gMarked[][] = new boolean[tNum][tNum];
		FlowNetwork fn = new FlowNetwork(count);
		// add teams to t's edges
		// count-1 represents t
		for (int i = 0; i < tNum; i++) {
			if (i != x)
				fn.addEdge(new FlowEdge(i, count - 1, w[x] + r[x] - w[i]));
		}
		// add s to vertices i-j and vertices i-j to teams
		// count-2 represents s
		for (int i = tNum; i < tNum * tNum + tNum; i++) {
			int m = (i - tNum) % tNum;
			int n = (i - tNum) / tNum;
			int cap = g[n][m];
			if (x != m && x != n && m != n && !gMarked[n][m]) {
				fn.addEdge(new FlowEdge(count - 2, i, cap));
				fn.addEdge(new FlowEdge(i, n, Double.POSITIVE_INFINITY));
				fn.addEdge(new FlowEdge(i, m, Double.POSITIVE_INFINITY));
				gMarked[m][n] = true;
			}
		}
		return fn;
	}

	// check if team is exist and return its index
	private int checkTeam(String team) {
		if (!teams.contains(team))
			throw new IllegalArgumentException("such team does not exist!");
		return teams.indexOf(team);
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
					StdOut.print(t + " ");
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
