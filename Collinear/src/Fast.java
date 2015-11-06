import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Fast {
	public static void main(String[] args) {
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.setPenRadius(0.005);

		In in = new In(args[0]);
		int N = in.readInt();
		Point points[] = new Point[N];
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
			points[i].draw();
		}

		ArrayList<ArrayList<Point>> seg = new ArrayList<ArrayList<Point>>();
		Arrays.sort(points);
		for (int i = 0; i < N; i++) {
			Point p=points[i];
			sort(points, p.SLOPE_ORDER);

			ArrayList<Point> collinear = new ArrayList<Point>();

			for (int j = 1; j < N; j++) {

				if (collinear.isEmpty())
					collinear.add(points[j]);
				else if (p.slopeTo(points[j - 1]) == p
						.slopeTo(points[j])) {
					collinear.add(points[j]);
					if (collinear.size() > 2
							&& (j == N - 1 || p.slopeTo(points[j]) != p
									.slopeTo(points[j + 1]))) {
						collinear.add(p);
						Collections.sort(collinear);
						int count = 0;
						for (int t = 0; t < collinear.size(); t++) {
							if (p.compareTo(collinear.get(t)) <= 0) {
								count++;
							}
						}
						if (count == collinear.size()) {
							ArrayList<Point> copy = new ArrayList<Point>();
							for (int k = 0; k < collinear.size(); k++)
								copy.add(collinear.get(k));
							seg.add(copy);
						}
						collinear.clear();
						collinear.add(points[j]);
						continue;
					}
				} else {
					collinear.clear();
					collinear.add(points[j]);
				}
			}
			Arrays.sort(points);
		}
		/*
		 * HashSet<ArrayList<Point>> hs = new HashSet<ArrayList<Point>>();
		 * hs.addAll(seg); seg.clear(); seg.addAll(hs);
		 */

		Iterator<ArrayList<Point>> i = seg.iterator();
		while (i.hasNext()) {
			ArrayList<Point> a = i.next();
			Collections.min(a).drawTo(Collections.max(a));
			StdOut.println(join(a, " -> "));
		}
	}

	private static void sort(Point[] a, Comparator<Point> comparator) {
		Point aux[] = new Point[a.length];
		sort(a, aux, 0, a.length - 1, comparator);

	}

	private static void sort(Point[] a, Point[] aux, int lo, int hi,
			Comparator<Point> comparator) {
		if (hi <= lo)
			return;
		int mid = lo + (hi - lo) / 2;
		sort(a, aux, lo, mid, comparator);
		sort(a, aux, mid + 1, hi, comparator);
		merge(a, aux, lo, mid, hi, comparator);
	}

	private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi,
			Comparator<Point> comparator) {
		for (int k = lo; k <= hi; k++)
			aux[k] = a[k];

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				a[k] = aux[j++];
			else if (j > hi)
				a[k] = aux[i++];
			else if (less(aux[j], aux[i], comparator))
				a[k] = aux[j++];
			else
				a[k] = aux[i++];
		}

	}

	private static boolean less(Point p1, Point p2, Comparator<Point> comparator) {
		return comparator.compare(p1, p2) < 0;
	}

	private static String join(ArrayList<Point> list, String delim) {
		StringBuilder sb = new StringBuilder();
		String loopDelim = "";

		for (Point s : list) {
			sb.append(loopDelim);
			sb.append(s);
			loopDelim = delim;
		}
		return sb.toString();
	}
}