public class Percolation {
	private boolean[] open;
	private WeightedQuickUnionUF grid;
	private WeightedQuickUnionUF fullness;
	private int N;
	private int virtualTop;
	private int virtualBottom;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) throws IllegalArgumentException {
		if (N <= 0)
			throw new IllegalArgumentException(
					"N cannot be less or equal to 0.");
		this.N = N;
		grid = new WeightedQuickUnionUF(N * N + 2);
		fullness = new WeightedQuickUnionUF(N * N + 1);
		open = new boolean[N * N];
		virtualTop = xyTo1D(N, N) + 1;
		virtualBottom = xyTo1D(N, N) + 2;
	}

	// open site (row i, column j) if it is not open already
	public void open(int i, int j) {
		if (isOpen(i, j))
			return;
		validateIndices(i, j);
		open[xyTo1D(i, j)] = true;
		connectWithSurroundingOpenSites(i, j);
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		validateIndices(i, j);
		return open[xyTo1D(i, j)] == true;
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		validateIndices(i, j);
		return fullness.connected(xyTo1D(i, j), virtualTop);
	}

	// does the system percolate?
	public boolean percolates() {
		return grid.connected(virtualTop, virtualBottom);
	}

	private int xyTo1D(int i, int j) {
		validateIndices(i, j);
		return (int)((N * (i - 1) + j) - 1);
	}

	private void validateIndices(int i, int j) throws IndexOutOfBoundsException {
		if (!isValid(i, j))
			throw new IndexOutOfBoundsException("Invalid indices!");
	}

	private boolean isValid(int i, int j) {
		i -= 1;
		j -= 1;
		return i < N && j < N && i >= 0 && j >= 0;
	}

	private void connectWithSurroundingOpenSites(int i, int j) {
		int index = xyTo1D(i, j);
		if (i == 1) {
			grid.union(virtualTop, index);
			fullness.union(virtualTop, index);
		}
		if (i == N) {
			grid.union(virtualBottom, index);
		}
		if (isValid(i, j - 1) && isOpen(i, j - 1)) {
			grid.union(xyTo1D(i, j - 1), index);
			fullness.union(xyTo1D(i, j - 1), index);
		}
		if (isValid(i, j + 1) && isOpen(i, j + 1)) {
			grid.union(xyTo1D(i, j + 1), index);
			fullness.union(xyTo1D(i, j + 1), index);
		}
		if (isValid(i - 1, j) && isOpen(i - 1, j)) {
			grid.union(xyTo1D(i - 1, j), index);
			fullness.union(xyTo1D(i - 1, j), index);
		}
		if (isValid(i + 1, j) && isOpen(i + 1, j)) {
			grid.union(xyTo1D(i + 1, j), index);
			fullness.union(xyTo1D(i + 1, j), index);
		}
	}
	
}
