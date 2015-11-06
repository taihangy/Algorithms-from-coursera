public class PercolationStats {
	private double[] num;
	
	private int T;
	private double confidenceLo;
	private double confidenceHi;

	public PercolationStats(int N, int T) {
		// perform T independent experiments on an N-by-N grid
		num = new double[T];
		this.T=T;
		for (int i = 0; i < T; i++) {
			Percolation perc = new Percolation(N);
			double row;
			double col;
			int count = 0;
			while (!perc.percolates()) {
				row = Math.floor(StdRandom.random() * N) + 1;
				col = Math.floor(StdRandom.random() * N) + 1;

				if (!perc.isOpen((int) row, (int) col)) {
					perc.open((int) row, (int) col);
					count++;
				}
			}
			num[i] = ((double) count) / (N * N);
		}
	}

	public double mean() {
		// sample mean of percolation threshold
		double sum = 0.0;
		for (int i = 0; i < T; i++) {
			sum += num[i];
		}
		double mean = sum / T;
		return mean;
	}

	public double stddev() {
		// sample standard deviation of percolation threshold
		double sum = 0.0;
		double mean = this.mean();
		for (int i = 0; i < T; i++) {
			sum += (num[i] - mean) * (num[i] - mean);
		}
		double sd = Math.sqrt(sum / (T - 1));
		return sd;

	}

	public double confidenceLo() {
		// low endpoint of 95% confidence interval
		confidenceLo = mean() - 1.96 * stddev()
				/ Math.sqrt(T*1.0);
		return confidenceLo;
	}

	public double confidenceHi() {
		// high endpoint of 95% confidence interval
		confidenceHi = this.mean() - 1.96 * this.stddev()
				/ Math.sqrt(T*1.0);
		return confidenceHi;
	}

	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		/*
		StdOut.println("mean=" + ps.mean());
		StdOut.println("stddev=" + ps.stddev());
		StdOut.println("95% confidence interval=" + ps.confidenceLo() + ","
				+ ps.confidenceHi());
				*/
		StdOut.println(Math.sqrt(10.0));

	}

}
