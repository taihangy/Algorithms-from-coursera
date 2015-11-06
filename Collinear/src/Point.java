/*************************************************************************
 * Name: Ye Taihang
 * Email: yetaihang.zju@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       // YOUR DEFINITION HERE
//    private static final Comparator<Point> POSITION_ORDER = new PositionOrder();

/*    public static Comparator<Point> getPositionOrder() {
		return POSITION_ORDER;
	}*/

	private final int x;                              // x coordinate
    private final int y;                              // y coordinate
    
    //
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point q, Point r) {
            double qSlope = slopeTo(q);
            double rSlope = slopeTo(r);
            if (qSlope < rSlope) return -1;
            if (qSlope > rSlope) return 1;
            return 0;
        }
    }
    
/*    private static class PositionOrder implements Comparator<Point> {
        public int compare(Point q, Point r) {
            if (q.y<r.y) 
            	return -1;
            else if(q.y==r.y&&q.x<r.x)
            	return -1;
            else 
            	return 1;
        }
    }
 */
    
    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;        
        if (compareTo(that) == 0) return Double.NEGATIVE_INFINITY;
        if (dy == 0) return 0.0;
        if (dx == 0) return Double.POSITIVE_INFINITY;
        return dy / dx;
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
        }

        // display to screen all at once
        StdDraw.show(0);
    }
}