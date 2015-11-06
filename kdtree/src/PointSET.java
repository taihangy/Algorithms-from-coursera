import java.util.Iterator;


public class PointSET {
	private final RedBlackBST<Point2D,Byte> point;
	private static final double UNIT=1.0;
	public PointSET() {
		// construct an empty set of points
		point=new RedBlackBST<Point2D, Byte>();
	}

	public boolean isEmpty() {
		// is the set empty?
		return point.isEmpty();
	}

	public int size() {
		// number of points in the set
		return point.size();
	}

	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		point.put(p, (byte)0);
	}

	public boolean contains(Point2D p) {
		// does the set contain point p?
		return point.contains(p);
	}

	public void draw() {
		// draw all points to standard draw
		Iterable<Point2D> queue=point.keys();
		Iterator<Point2D> iter=queue.iterator();
		while(iter.hasNext())
			iter.next().draw();
	}

	public Iterable<Point2D> range(RectHV rect) {
		// all points that are inside the rectangle
		Queue<Point2D> q=new Queue<Point2D>();
		Point2D lo=new Point2D(rect.xmin(),rect.ymin());
		Point2D hi=new Point2D(rect.xmax(),rect.ymax());
		Iterable<Point2D> queue=point.keys(lo, hi);
		Iterator<Point2D> iter=queue.iterator();
		while(iter.hasNext()){
			Point2D p=iter.next();
			if(p.x()>=rect.xmin()&&p.x()<=rect.xmax())
				q.enqueue(p);
		}
		return q;
	}

	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty
		if(this.isEmpty()) return null;
		
		Point2D nearestPoint=new Point2D(0.0,0.0);
		double nearestDist=2*UNIT*UNIT;
		Iterable<Point2D> queue=point.keys();
		Iterator<Point2D> iter=queue.iterator();
		while(iter.hasNext()){
			Point2D curPoint=iter.next();
			double curDist=curPoint.distanceSquaredTo(p);
			if(curDist<nearestDist){
				nearestDist=curDist;
				nearestPoint=curPoint;
			}
		}
		return nearestPoint;
	}

	public static void main(String[] args) {
		// unit testing of the methods (optional)
	}
}