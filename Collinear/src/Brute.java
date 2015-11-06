import java.util.Arrays;

public class Brute {
	public static void main(String args[]) {
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.setPenRadius(0.01);
		StdDraw.show(0);

		String filename = args[0];
		In file = new In(filename);
		int N = file.readInt();
		Point points[] = new Point[N];
		for (int i = 0; i < N; i++) {
			int x = file.readInt();
			int y = file.readInt();
			points[i] = new Point(x, y);
			points[i].draw();
		}

		Arrays.sort(points);

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				for (int m = j + 1; m < N; m++) {
					for (int n = m + 1; n < N; n++) {
						if (points[i].slopeTo(points[j]) == points[i]
								.slopeTo(points[m])
								&& points[i].slopeTo(points[j]) == points[i]
										.slopeTo(points[n])) {
							points[i].drawTo(points[n]);
							StdOut.print(points[i] + " -> " + points[j]
									+ " -> " + points[m] + " -> " + points[n]);
							StdOut.println();
						}

					}
				}
			}
		}
		StdDraw.show(0);
	}
}
