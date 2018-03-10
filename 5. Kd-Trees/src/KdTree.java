import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class KdTree {

    private Node root;
    private int size;

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (!contains(p)) {
            root = insert(root, new Node(p), true, 0, 0, 1, 1);
            size++;
        }
    }

    // Add the Node to the set and make its rectangle once it is placed
    private Node insert(Node cur, Node toPut, boolean compX, double xmin, double ymin, double xmax, double ymax) {
        if (cur == null) {
            toPut.makeRect(xmin, ymin, xmax, ymax);
            return toPut;
        }

        // Depending on the current dimension, change the appropriate x-y bound
        int cmp = compX ? toPut.compareX(cur) : toPut.compareY(cur);
        if (cmp < 0) {
            // Go left/down (lower xmax / lower ymax)
            if (compX) {
                cur.left = insert(cur.left, toPut, !compX, xmin, ymin, cur.point.x(), ymax);
            } else {
                cur.left = insert(cur.left, toPut, !compX, xmin, ymin, xmax, cur.point.y());
            }
        } else {
            // Go right/up (higher xmin / higher ymin)
            if (compX) {
                cur.right = insert(cur.right, toPut, !compX, cur.point.x(), ymin, xmax, ymax);
            } else {
                cur.right = insert(cur.right, toPut, !compX, xmin, cur.point.y(), xmax, ymax);
            }
        }
        return cur;
    }


    // Determine if the point is in the set
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, new Node(p), true);
    }

    private boolean contains(Node cur, Node test, boolean compX) {
        // Search for test wherever it would have been inserted

        if (cur == null) return false;

        int cmpX = test.compareX(cur);
        int cmpY = test.compareY(cur);
        if (cmpX == 0 && cmpY == 0) return true;

        int cmp = compX ? cmpX : cmpY;
        if (cmp < 0) return contains(cur.left, test, !compX);
        else return contains(cur.right, test, !compX);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        draw(root, true);
        StdDraw.show();
    }

    private void draw(Node cur, boolean compX) {
        if (cur != null) {
            draw(cur.left, !compX);
            // Horizontal Line
            if (compX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                cur.rect.draw();
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                cur.rect.draw();
             }
            StdDraw.setPenRadius(0.01);
            cur.point.draw();
            draw(cur.right, !compX);
        }
    }


    // Find all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> re = new Stack<Point2D>();
        Stack<Node> s = new Stack<Node>();
        Node cur;

        // Nodes in the stack are those with rectangles which intersect rect
        // (points going down the subtree could be contained inside rect)
        s.push(root);
        while (!s.empty()) {
            cur = s.pop();
            if (cur != null) {
                if (rect.contains(cur.point)) {
                    re.push(cur.point);
                }
                if (cur.rect.intersects(rect)) {
                    if (cur.left != null && cur.left.rect.intersects(rect)) s.push(cur.left);
                    if (cur.right != null && cur.right.rect.intersects(rect)) s.push(cur.right);
                }
            }
        }
        return re;
    }

    // Determine the nearest neighbor in the tree to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (size == 0) return null;

        Point2D re = null;
        Node cur;
        Stack<Node> s = new Stack<Node>();
        double curDistance;
        double smallestDistance = Integer.MAX_VALUE;
        double testDistA, testDistB;

        // Keep track of the closest point found so far to prune the tree
        // Stack allows for a depth-first approach
        s.push(root);
        while (!s.empty()) {
            cur = s.pop();

            if (cur != null) {

                // Update closest point
                curDistance = p.distanceSquaredTo(cur.point);
                if (curDistance < smallestDistance) {
                    smallestDistance = curDistance;
                    re = cur.point;
                }

                // This Node has a chance of containing the closest point
                // The subtree closer to the point will be checked first (goes on top of stack)
                if (cur.rect.distanceSquaredTo(p) < smallestDistance) {
                    testDistA = (cur.left == null) ? Integer.MAX_VALUE : cur.left.rect.distanceSquaredTo(p);
                    testDistB = (cur.right == null) ? Integer.MAX_VALUE : cur.right.rect.distanceSquaredTo(p);

                    if (testDistA < testDistB) {
                        if (testDistB < smallestDistance) s.push(cur.right);
                        if (testDistA < smallestDistance) s.push(cur.left);       // Left path taken first (stack)
                    } else {
                        if (testDistA < smallestDistance) s.push(cur.left);
                        if (testDistB < smallestDistance) s.push(cur.right);
                    }
                }
            }
        }
        return re;
    }


    private class Node {
        private final Point2D point;
        private Node left, right;
        private RectHV rect;

        private Node(Point2D p) {
            point = p;
        }

        private void makeRect(double xmin, double ymin, double xmax, double ymax) {
            rect = new RectHV(xmin, ymin, xmax, ymax);
        }

        private int compareX(Node that) {
            return Double.compare(this.point.x(), that.point.x());
        }

        private int compareY(Node that) {
            return Double.compare(this.point.y(), that.point.y());
        }
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}