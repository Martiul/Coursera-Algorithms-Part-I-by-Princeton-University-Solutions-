import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public final class FastCollinearPoints {

    private final LineSegment [] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) if (points[i] == null) throw new IllegalArgumentException();

        int length = points.length;
        Point [] myPoints = points.clone();
        Point [] fixedOrder;
        int numSegments = 0;
        double testSlope;
        Point origin;
        LineSegment [] testSegments = new LineSegment[length*length];


        Arrays.sort(myPoints);
        fixedOrder = myPoints.clone(); // Keep original set of points because sorting scrambles points
        // Check for duplicate points
        for (int i = 0; i < length-1; i++) {
            if (myPoints[i].compareTo(myPoints[i+1]) == 0) throw new IllegalArgumentException();
        }


        // Treat each point as an origin and find slopes to every other point
        // Points with the same slope from the origin are collinear
        for (int i = 0; i < length; i++) {
            origin = fixedOrder[i];
            // StdOut.println("=== Origin: " + origin.toString() + " ===");

            // Sort by natural order and then by slope to find endpoints easily
            Arrays.sort(myPoints);
            Arrays.sort(myPoints, origin.slopeOrder()); // Mergesort used (stable)

            int j = 0;
            int additionalPoints = 0;

            while (j < length-2) {
                
                // Find all points with the same slope we are currently working with
                testSlope = origin.slopeTo(myPoints[j]);
                additionalPoints = 1;
                while (j + additionalPoints < length &&
                        testSlope == origin.slopeTo(myPoints[j+additionalPoints])) {
                    additionalPoints++;
                }
                additionalPoints -= 1;

                // Check if the line segment has at least 4 collinear points
                if (additionalPoints >= 2) {

                    // Only create line segment if first and last additional
                    // points are in the "forward" direction 
                    // (works because we sorted twice, so the points inbetween are sorted
                    // in an increasing order)
                    if (origin.compareTo(myPoints[j]) < 0 &&
                            origin.compareTo(myPoints[j+additionalPoints]) < 0) {
                        testSegments[numSegments] = new LineSegment(origin, myPoints[j+additionalPoints]);
                        numSegments++;
                    }
                    j = j + additionalPoints;
                }
                j++;
            }
        }
        segments = new LineSegment[numSegments];
        for (int i = 0; i < numSegments; i++) {
            segments[i] = testSegments[i];
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        int length = numberOfSegments();
        LineSegment [] re = new LineSegment[length];
        for (int i = 0; i < length; i++) re[i] = segments[i];
        return re;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}