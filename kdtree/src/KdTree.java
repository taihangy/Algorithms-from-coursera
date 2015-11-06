public class KdTree {
	private Node root;
	private int count;
	private static final int K = 2;

	private class Node {
		private Point2D point;
		private RectHV rect;
		private int level;
		private Node lb;
		private Node rt;

		public Node(Point2D point, RectHV rect, int level) {
			this.point = point;
			this.rect = rect;
			this.level = level % K;
		}
	}

	public KdTree() {
		this.count = 0;
		this.root = null;
	}

	public boolean isEmpty() {
		// is the set empty?
		return root == null;
	}

	public int size() {
		// number of points in the set
		return count;
	}

	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		root = insert(root, null, p);
	}

	private Node insert(Node x, Node parent, Point2D point) {
		if (x == null) {
			count++;
			if (parent == null)
				return new Node(point, new RectHV(0.0, 0.0, 1.0, 1.0), 0);
			else
				return new Node(point, getRect(point, parent), parent.level + 1);
		}
		int cmp = compare(x, point);
		if (cmp < 0)
			x.lb = insert(x.lb, x, point);
		else if (cmp > 0)
			x.rt = insert(x.rt, x, point);
		else
			x.point = point;

		return x;
	}

	private RectHV getRect(Point2D point, Node parent) {
		int cmp = compare(parent, point);
		if (cmp < 0) {
			if (parent.level == 0) {
				return new RectHV(parent.rect.xmin(), parent.rect.ymin(),
						parent.point.x(), parent.rect.ymax());
			} else {
				return new RectHV(parent.rect.xmin(), parent.rect.ymin(),
						parent.rect.xmax(), parent.point.y());
			}
		} else if (cmp > 0) {
			if (parent.level == 0) {
				return new RectHV(parent.point.x(), parent.rect.ymin(),
						parent.rect.xmax(), parent.rect.ymax());
			} else {
				return new RectHV(parent.rect.xmin(), parent.point.y(),
						parent.rect.xmax(), parent.rect.ymax());
			}
		} else {
			return parent.rect;
		}
	}

	private int compare(Node x, Point2D point) {
		int level = x.level;
		if (level == 0) {
			if (x.point.x() > point.x())
				return -1;
			if (x.point.x() < point.x())
				return +1;
			if (x.point.y() > point.y())
				return -1;
			if (x.point.y() < point.y())
				return +1;
		}
		if (level == 1) {
			if (x.point.y() > point.y())
				return -1;
			if (x.point.y() < point.y())
				return +1;
			if (x.point.x() > point.x())
				return -1;
			if (x.point.x() < point.x())
				return +1;
		}
		return 0;
	}

	public boolean contains(Point2D p) {
		// does the set contain point p?
		return contains(root,p);
	}

	private boolean contains(Node x, Point2D point) {
		if(x==null) return false;
		int cmp=compare(x,point);
		if(cmp==0) return true;
		else if(cmp<0) return contains(x.lb,point);
		else return contains(x.rt,point);
	}

	public void draw() {
		// draw all points to standard draw
		draw(root);
	}

	private void draw(Node x) {
		StdDraw.setPenRadius(0.001);
		if(x.level==0){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
		} else if(x.level==1){
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());
		}
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
		x.point.draw();
		if(x.lb!=null) draw(x.lb);
		if(x.rt!=null) draw(x.rt);
	}

	public Iterable<Point2D> range(RectHV rect) {
		// all points that are inside the rectangle
		Queue<Point2D> q=new Queue<Point2D>();
		range(root,rect,q);
		return q;
	}

	private void range(Node x, RectHV rect, Queue<Point2D> q) {
		if(x==null) return;
		if(rect.contains(x.point)) q.enqueue(x.point);
		if(x.lb!=null&&rect.intersects(x.lb.rect)) range(x.lb,rect,q);
		if(x.rt!=null&&rect.intersects(x.rt.rect)) range(x.rt,rect,q);
	}

	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty
		if(this.isEmpty()) return null;
		return nearest(root,p,root.point);
	}


	private Point2D nearest(Node x, Point2D p,Point2D q) {
		Point2D champion=q;
		if(x==null) return champion;
		
		if(p.distanceSquaredTo(x.point)<p.distanceSquaredTo(champion))
			champion=x.point;
		
		int cmp=compare(x,p);
		if(cmp<0){
			champion=nearest(x.lb,p,champion);
			if(x.rt!=null&&p.distanceSquaredTo(champion)>x.rt.rect.distanceSquaredTo(p))
				champion=nearest(x.rt,p,champion);
		}
		else if(cmp>0){
			champion=nearest(x.rt,p,champion);
			if(x.lb!=null&&p.distanceSquaredTo(champion)>x.lb.rect.distanceSquaredTo(p))
				champion=nearest(x.lb,p,champion);
		}
		return champion;
	}

	public static void main(String[] args) {
		// unit testing of the methods (optional)
		
	}

}
