/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 3/21/2020
 *  Description: This program examines 4 points at a time and checks whether they
 *               all lie on the same line segment, returning all such line segments.
 *               To check whether the 4 points p, q, r, and s are collinear, check
 *               whether the three slopes between p and q, between p and r and between
 *               p and s are all equal.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {


    private final LineSegment[] segments;  // array LineSegment
    private int segmentSize = 0;           // number of line segments

    // Find all the line segments containing 4 poiints
    public BruteCollinearPoints(Point[] points) {


        if (points == null) {
            throw new IllegalArgumentException(" The array \"Points\" is null ");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException(" The array \"Points\" contains null elements ");
            }
        }

        checkDuplicate(points);

        Point[] p = new Point[points.length];
        System.arraycopy(points, 0, p, 0, points.length);
        segments = analyzeSegments(p);

    }

    // Analyze and sort the points p using MergeX
    private LineSegment[] analyzeSegments(Point[] points) {
        LineSegment[] tempSegments = new LineSegment[points.length * 4];
        MergeX.sort(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[m];

                        if (Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0
                                && Double.compare(p.slopeTo(q), p.slopeTo(s)) == 0) {

                            tempSegments[segmentSize++] = new LineSegment(p, s);
                        }

                    }
                }
            }
        }
        LineSegment[] resultSegments = new LineSegment[segmentSize];
        for (int i = 0; i < segmentSize; i++) {
            resultSegments[i] = tempSegments[i];
        }
        return resultSegments;
    }

    // Returns the number of line segments
    public int numberOfSegments() {
        return segmentSize;
    }


    public LineSegment[] segments() {
        LineSegment[] returnSegments = new LineSegment[segments.length];
        System.arraycopy(segments, 0, returnSegments, 0, segments.length);
        return returnSegments;
    }

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(" Duplicate(s) found! ");
                }
            }
        }
    }

    public static void main(String[] args) {
        // Read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            String s = in.readString();
            if ("null".equals(s)) {
                points[i] = null;
            }
            else {
                int x = Integer.parseInt(s);
                int y = in.readInt();
                points[i] = new Point(x, y);
            }
        }

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        // Drawing the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }

        StdDraw.show();

        // Print and draw the line segments
        for (LineSegment segment : collinear.segments) {
            StdOut.println(segment);
            segment.draw();

        }

        // Display
        StdDraw.show();

    }


}
