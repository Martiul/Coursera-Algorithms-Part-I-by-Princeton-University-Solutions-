import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class BruteCollinearPoints {
    
    private final LineSegment [] segments;

    // Finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        int length = points.length;
        Point [] myPoints = points.clone();
        LineSegment [] tempSegments = new LineSegment[length*length];
        int count = 0;
        Point pa, pb, pc, pd;


        Arrays.sort(myPoints);
        // Check for duplicate points
        for (int i = 0; i < length-1; i++) {
            if (myPoints[i].compareTo(myPoints[i+1]) == 0) throw new IllegalArgumentException();
        }

        // Since array is sorted, pa will always be the least of the 4 points
        // and be one end point, and pd will always be the other
        for (int a = 0; a < length; a++) {
            for (int b = a+1; b < length; b++) {
                for (int c = b+1; c < length; c++) {
                    for (int d = c+1; d < length; d++) {
                        pa = myPoints[a];
                        pb = myPoints[b];
                        pc = myPoints[c];
                        pd = myPoints[d];
                        if (pa.slopeTo(pb) == pa.slopeTo(pc) && pa.slopeTo(pc) == pa.slopeTo(pd)) {
                            tempSegments[count++] = new LineSegment(pa, pd);
                        }
                    }
                }
            }
        }

        // Save segments found into the actual segments array
        segments = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            segments[i] = tempSegments[i];
        }
    }

    // Return the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // Return a copy of the line segments
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}