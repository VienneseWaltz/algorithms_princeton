/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 4/26/2020
 *  Description: Implement a mutable datatype PointSET that represents a set of
 *               points in the unit square. Point2D is an immutable data type
 *               that represents points in the plane. RectHV is an immutable
 *               data type that represents axis-aligned rectangles.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> set;

    public PointSET() {
        // construct an empty set of points
        set = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return set.isEmpty();
    }

    public int size() {
        // return the number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException(" Point cannot be null.");
        }
        set.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException(" The set should not have null point.");
        }
        return set.contains((Object) p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        if (rect == null) {
            throw new IllegalArgumentException(" Rectangle should not be null.");
        }
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                queue.enqueue(p);
            }
        }
        return queue;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Point2D nearest = null;   // nearest neighbor
        double distance;
        double shortestDistance = Double.MAX_VALUE;  // shortest distance to nearest neighbor

        for (Point2D point : set) {
            distance = p.distanceSquaredTo(point);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET pointSet = new PointSET();

        StdOut.println(" Is empty: " + pointSet.isEmpty());
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        In file = new In(args[0]);
        while (!file.isEmpty()) {
            // read in the file
            Point2D point = new Point2D(file.readFloat(), file.readFloat());
            if (pointSet.contains(point)) {
                StdOut.println("Duplicate detected: " + point.toString());
            }
            pointSet.insert(point);

        }

        // print out the size of the pointSet
        StdOut.println(" Size: " + pointSet.size());
        StdOut.println(" Is empty: " + pointSet.isEmpty());


        // set 2 pen colors and draw
        StdDraw.setPenColor(StdDraw.RED);  // for vertical splits
        StdDraw.setPenColor(StdDraw.BLUE);  // for horizontal splits
        StdDraw.setPenRadius();


        // draw axis-aligned rectangle
        RectHV rect = new RectHV(0.1, 0.1, 0.9, 0.9);
        rect.draw();
        StdOut.println("Points in rectangle " + rect.toString() + ":");
        for (Point2D p : pointSet.range(rect)) {
            StdOut.println(p.toString());
        }

        StdOut.println(" ************************************\n ");

        // test out a random point such as (0.1, 0.1) and find the nearest point
        Point2D point = new Point2D(0.25, 0.3);
        point.draw();
        StdOut.println(" The nearest point to point" + point.toString() + " is :");
        StdOut.println(pointSet.nearest(point).toString());
        StdOut.println(" ***********************************\n");
    }
}
