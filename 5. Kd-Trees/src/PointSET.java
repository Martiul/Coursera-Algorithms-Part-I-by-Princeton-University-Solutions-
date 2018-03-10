import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : points) {
            p.draw();
        }
        StdDraw.show();
    }

    // Find all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> re = new Stack<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                re.push(p);
            }
        }
        return re;
    }

    // Find the nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (points.isEmpty()) return null;

        Point2D re = null;
        double testDist;
        double minDist = Integer.MAX_VALUE;
        for (Point2D myPoint : points) {
            testDist = p.distanceSquaredTo(myPoint);
            if (testDist < minDist) {
                re = myPoint;
                minDist = testDist;
            }
        }
        return re;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}