/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 3/20/2020
 *  Description: Given a set of n distinct points in a plane, find every (maximal)
 *               line segment that connects a subset of 4 or omre of the points.
 *               The compareTo() method compares points by their y-coordinates,
 *               breaking ties by their x-coordinates. The slopeTo() method returns
 *               the slope between the invoking point(x0, y0) and the argument
 *               point(x1, y1). The slopeOrder() method returns a comparator that
 *               compares its two argument points by the slopes they make with the
 *               invoking point(x0, y0).
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {


    private final int x;  // x-coordinate of this point
    private final int y;  // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns a string representation of this point. This method is provide for debugging; your
     * program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Comparing points by their y-coordinates. The invoking point (x0, y0) is less than the
     * argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that
     * @return -1 if and only if y0 < y1 or if y0 = y1 and x0 < x1
     */
    public int compareTo(Point that) {
        if ((this.y < that.y) || (this.y == that.y && this.x < that.x))
            return -1;
        else if (this.y == that.y && this.x == that.x)
            return 0;
        else return 1;

    }

    /**
     * Returns the slope between the invoking point (x0, y0) and the argument point(x1, y1) which is
     * given by the formula (y1 - y0)/(x1 - x0). The slope of a horizontal line is treated as
     * positive zero, the slope of a vertical line as positive infinity, and the slope of a
     * degenerative line segment (between a point and itself) as negative infinity.
     *
     * @param that
     * @return
     */

    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        else if (this.y == that.y) {
            return +0.0;
        }
        else return (double) (that.y - this.y) / (that.x - this.x);
    }


    public Comparator<Point> slopeOrder() {
        return new slopeOrder();
    }


    /**
     * Class SlopeOrder compares two argument points by the slopes they make with the invoking point
     * (x0, y0). Formally, the point (x1, y1) is less than the point (x2, y2) if and only if the
     * slope (y1 - y0)/(x1 - x0) is less than the slope (y2 - y0)/(x2 - x0). Treat horizontal,
     * vertical and degenerate line segments as in the slopeTo() method.
     */

    private class slopeOrder implements Comparator<Point> {
        public int compare(Point p, Point q) {
            double slope1 = slopeTo(p);
            double slope2 = slopeTo(q);

            if (slope1 < slope2)
                return -1;
            else if (slope1 > slope2)
                return 1;
            else return 0;
        }
    }


    // Unit testing the Point data type
    public static void main(String[] args) {


        int x0 = Integer.parseInt(args[0]); // invoking point's x-coordinate
        int y0 = Integer.parseInt(args[1]); // invoking point's y-coordinate
        int n = Integer.parseInt(args[2]);  // number of iterations to run

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 50);
        StdDraw.setYscale(0, 50);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {

            // generate (n+1) random points
            int x = StdRandom.uniform(50);  // generate a random x
            int y = StdRandom.uniform(50);  // generate a random y
            points[i] = new Point(x, y);
            points[i].draw();

        }

        // drawing the point (x0, y0) in cyan
        Point p = new Point(x0, y0);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.setPenRadius(0.002);
        p.draw();

        // draw line segments in green from p to each point, one at a time
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.setPenRadius(0.005);
        Arrays.sort(points, p.slopeOrder()); // sort the points using slopeOrder
        for (int i = 0; i < n; i++) {
            p.drawTo(points[i]);
            StdDraw.show();
            StdDraw.pause(80);


        }

    }

}
