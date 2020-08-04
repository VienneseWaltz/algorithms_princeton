/* *************************************************************************************
 *  Name: Stella Soh
 *  Date: 3/21/2020
 *  Description: Given a point p (think of p as the origin) this program determines
 *               whether p participates in a set of 4 or more collinear points. For
 *               each other point q, the program determine the slope q makes with p
 *               and using slopeOrder(), it compares the these 2 points. Then
 *               MergeX.sort() is called upon to sort the points according to the
 *               slopes they make with p. The program proceeds to check if any 3 (or more)
 *               adjacent points in the sorted order have equal slopes with respect to p.
 *               If so, these points, together with p, are collinear.
 *
 ****************************************************************************************/


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class FastCollinearPoints {

    private int segmentsCount;  // number of line segments
    private final LineSegment[] segments;


    public FastCollinearPoints(Point[] points) {


        // If the array Points is null
        if (points == null) {
            throw new IllegalArgumentException(" The array \"Points\" is null ");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException(" The array \"Points\" contains null elements ");
            }
        }


        this.segmentsCount = 0;                 // Initialize number of line segments
        checkDuplicate(points);                 // Checking for duplicate points
        Point[] copyOfPoints = points;          // A copy of each element of points array
        System.arraycopy(points, 0, copyOfPoints, 0, points.length);
        this.segments = this.analyze(copyOfPoints);
    }

    // Analyze and sort the points array in ascending order
    private LineSegment[] analyze(Point[] points) {
        List<LineSegment> tempSegments = new ArrayList<>();

        // LineSegment[] tempSegments = new LineSegment[5 * points.length];
        MergeX.sort(points);

        // For each other point q, determine the slope it makes with p. Sort
        // the points according to the slopes they make with p.

        for (int i = 0; i < points.length; i++) {

            Point currentPoint = points[i]; // copy each element of points into currentPoint

            int numOfCollinear;  // number of collinear points

            int copySize = points.length - 1;
            Point[] slopeSorted = new Point[copySize];


            // Check elements backwards.Copy all elements that are  -
            // before p (i.e. from 0 index) to just before i - to slopeSorted array. Length (or
            // the total number of components to be copied) is i.
            System.arraycopy(points, 0, slopeSorted, 0, i);

            // Copy elements after the i-th element (above) to the (points.length - i - 1)th
            // element of points array to slopeSorted array.
            System.arraycopy(points, i + 1, slopeSorted, i, points.length - i - 1);

            // Sort the points according to the slopes they make with point p
            MergeX.sort(slopeSorted, currentPoint.slopeOrder());

            // counter to traverse amongst points that are of the same slope in slopeSorted array
            int j = 0;

            while (j < copySize) {
                numOfCollinear = 1;
                double currentSlope = currentPoint.slopeTo(slopeSorted[j]);
                int k = j + 1;
                while (k < copySize) {
                    if (Double.compare(currentPoint.slopeTo(slopeSorted[k]),
                                       currentSlope) == 0) {
                        numOfCollinear++;
                        k++;
                    }
                    else
                        break;
                }

                // Check if any 3 (or more) adjacent points in the sorted order have
                // equal slopes with respect to the current point.

                if (numOfCollinear >= 3) {
                    //  Check for duplicate line segments
                    Point startPoint = currentPoint;
                    if (slopeSorted[j].compareTo(startPoint) < 0) {
                        startPoint = slopeSorted[j];
                    }
                    Point endPoint = slopeSorted[k - 1];
                    if (currentPoint.compareTo(endPoint) > 0) {
                        endPoint = currentPoint;
                    }
                    LineSegment line = new LineSegment(startPoint, endPoint);
                    boolean existed = false;

                    for (LineSegment segment : tempSegments) {
                        //      for (int n = 0; n < segmentsCount; n++) {

                        if (segment.toString().equals(line.toString())) {
                            existed = true;
                            break;
                        }
                    }
                    if (!existed) {
                        tempSegments.add(line);
                        // tempSegments[segmentsCount++] = line;
                    }
                }
                //  System.out.println(" Number of line segments = " + segmentsCount);

                j = k;
            }

        }
        segmentsCount = tempSegments.size();
        return tempSegments.toArray(new LineSegment[tempSegments.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
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


        // Drawing the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }

        StdDraw.show();

        // Print and draw each of the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
