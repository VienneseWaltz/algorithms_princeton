/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 05/02/2020
 *  Description: This mutable data type, KdTree uses a 2d-tree to implement
 *               APIS's such as size(), insert(), isEmpty(), contains() that
 *               are found in PointSET. The idea is to build a BST with points
 *               in the nodes, using the x- and y-coordinates of the points as
 *               keys in strictly alternating sequence.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node left;
        private Node right;
        private final boolean vertical;

        public Node(Point2D p, boolean vertical, RectHV rect) {
            this.p = p;
            this.vertical = vertical;
            this.rect = rect;
        }
    }

    private Node root = null;
    private final RectHV vanilla;
    private int count;

    public KdTree() {
        // construct an empty set of points
        vanilla = new RectHV(0, 0, 1, 1);
    }

    public boolean isEmpty() {
        // is the set empty?
        return size() == 0;
    }

    public int size() {
        // the number of points in the set
        return count;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it isn't already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, p, true, vanilla);
    }

    private Node insert(Node x, Point2D p, boolean vertical, RectHV rect) {
        if (x == null) {
            count++;
            return new Node(p, vertical, rect);
        }
        if (x.p.equals(p)) {
            return x;
        }
        boolean cmp;
        if (vertical) {
            cmp = p.x() < x.p.x();
        }
        else {
            cmp = p.y() < x.p.y();
        }
        RectHV nextRect;

        if (cmp) {
            if (x.left == null) {
                double x1 = rect.xmin();
                double y1 = rect.ymin();
                double x2, y2;
                if (vertical) {
                    x2 = x.p.x();
                    y2 = rect.ymax();
                }
                else {
                    x2 = rect.xmax();
                    y2 = x.p.y();
                }
                nextRect = new RectHV(x1, y1, x2, y2);
            }
            else {
                nextRect = x.left.rect;
            }
            x.left = insert(x.left, p, !vertical, nextRect);
        }
        else {
            if (x.right == null) {
                double x1, y1;
                if (vertical) {
                    x1 = x.p.x();
                    y1 = rect.ymin();
                }
                else {
                    x1 = rect.xmin();
                    //y1 = rect.ymin();
                    y1 = x.p.y();
                }
                double x2 = rect.xmax();
                double y2 = rect.ymax();
                nextRect = new RectHV(x1, y1, x2, y2);

            }
            else {
                nextRect = x.right.rect;
            }
            x.right = insert(x.right, p, !vertical, nextRect);
        }
        return x;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) {
            return false;
        }
        if (x.p.equals((Object) p)) {
            return true;
        }
        boolean cmp;
        if (x.vertical) {
            cmp = p.x() < x.p.x();
        }
        else {
            cmp = p.y() < x.p.y();
        }

        if (cmp) {
            return contains(x.left, p);
        }
        else {
            return contains(x.right, p);
        }
    }

    public void draw() {
        // draw all points to a standard draw
        draw(root, true);
    }

    private void draw(Node x, boolean vertical) {
        if (x == null) {
            throw new IllegalArgumentException();
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());

        }
        if (x.left != null) {
            draw(x.left, !vertical);
        }
        if (x.right != null) {
            draw(x.right, !vertical);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> points = new Queue<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) {
                continue;
            }

            if (rect.contains(x.p)) {
                points.enqueue(x.p);
            }

            if (x.left != null && (x.rect).intersects(rect)) {
                // if left subtree is not null and the rectangle that contains x intersects
                // with the query rectangle, put that point into the queue for traversal
                queue.enqueue(x.left);
            }

            if (x.right != null && (x.rect).intersects(rect)) {
                // if right subtree is not null and the rectangle that contains x intersects
                // with the query rectangle, put that point into the queue for traversal
                queue.enqueue(x.right);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point to p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D champion = null;    // nearest neighbor
        double championDistance = Double.MAX_VALUE;  // shortest distance to nearest neighbor
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) {
                continue;
            }
            double distance = p.distanceSquaredTo(x.p);
            if (distance < championDistance) {
                champion = x.p;
                championDistance = distance;
            }
            // For the vertical-aligned direction
            if (x.vertical) { // query point is on the left of the cut-line
                if (p.x() < x.p.x()) {
                    if (Math.pow((p.x() - x.p.x()), 2) < championDistance) {
                        queue.enqueue(x.right); // add the right subtree
                    }
                }
                else { // query point is on the right of the cut-line
                    if (Math.pow((p.x() - x.p.x()), 2) < championDistance) {
                        queue.enqueue(x.left); // add the left subtree
                    }
                }
            }
            else { // For the horizontal-aligned direction
                if (p.y() < x.p.y()) { // query point is below the cut-line
                    if (Math.pow((p.y() - x.p.y()), 2) < championDistance) {
                        queue.enqueue(x.left); // add the left subtree
                    }
                }
                else { // query point is above the cut-line
                    if (Math.pow((p.y() - x.p.y()), 2) < championDistance) {
                        queue.enqueue(x.right); // add the right subtree
                    }
                }
            }
        }
        return champion;  // return the nearest neighbor
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();

        StdOut.println("Is empty: " + tree.isEmpty());

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        In file = new In(args[0]);
        while (!file.isEmpty()) {
            // read in the file
            Point2D point = new Point2D(file.readDouble(), file.readDouble());
            if (tree.contains(point)) {
                StdOut.println("Duplicate detected: " + point.toString());
            }
            tree.insert(point);
        }
        StdOut.println(" Size: " + tree.size());
        StdOut.println(" Is empty: " + tree.isEmpty());
        tree.draw();

        //  Point2D point = new Point2D(0.5, 1.0);
        //  StdOut.println(" The tree contains " + point.toString() + " : "
        //                          + tree.contains(point));

        // Setting pen colors and pen radius?
        //    StdDraw.setPenColor(StdDraw.RED);  // for vertical splits
        //    StdDraw.setPenColor(StdDraw.BLUE);  // for horizontal splits
        //    StdDraw.setPenRadius();

        RectHV rect = new RectHV(0.58, 0.72, 0.85, 0.96);
        // rect.draw();
        StdOut.println(" Points in rectangle " + rect.toString() + ":");
        for (Point2D p : tree.range(rect)) {
            StdOut.println(p.toString());
        }
        StdOut.println(" *******************************************\n ");

        Point2D nearPoint = new Point2D(0.1, 0.1);
        // point.draw();
        StdOut.println(" The nearest point to point" + nearPoint.toString() + " : ");
        StdOut.println(tree.nearest(nearPoint).toString());
        StdOut.println(" ********************************************\n ");
    }
}
