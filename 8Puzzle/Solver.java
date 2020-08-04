/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 4/3/2020
 *  Description: By using the Manhattan priority function, and invoking MinPQ data
 *               type for the priority queue, this program runs the A* search
 *               algorithm on a n-by-n puzzle. First, the initial search node (initial
 *               board, 0 moves and a null previous search node) is inserted into a
 *               priority queue. Then the search node with the lowest priority is deleted
 *               from the priority queue, and all neighboring search nodes (those that can
 *               reached in one move from the dequeued search node) are inserted into the
 *               priority queue. This process is repeated until the search node dequeued
 *               corresponds to the goal board. When the goal board is dequeued, the program
 *               has discovered not only a sequence of moves from the initial board to the
 *               goal board, but one that makes the fewest moves.
 *
 *               The program makes use of the fact that boards are divided into 2 equiavlence
 *               classes with respect to reachability: (a) Those that can lead to the goal
 *               board, (b) Those that can lead to the goal board if we modify the initial
 *               board by swapping any pair of tiles. To implement this, we run the program
 *               concurrently on 2 puzzle instances - one with the initial board and one
 *               with the initial board modified by swapping a pair of tiles. One of the two
 *               boards would lead to the goal board.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

    private ArrayList<Board> solutionPath;
    private boolean solvable = false;

    private int moves;

    public Solver(Board initial) {
        MinPQ<Node> searchQueue;
        MinPQ<Node> searchQueue2;


        // Find a solution to the initial board (using the A* algorithm)

        if (initial == null)
            throw new IllegalArgumentException(" Initial board cannot be null.");

        // Insert a initial minMode into searchQueue
        searchQueue = new MinPQ<Node>();
        searchQueue.insert(new Node(initial, 0, null));
        Node minNode = null;

        // Generate a twin board and insert into searchQueue2
        searchQueue2 = new MinPQ<Node>();
        searchQueue2.insert(new Node(initial.twin(), 0, null));
        Node minNode2;   // twin created to detect infeasible cases

        boolean alternate = false;
        while (true) { // alternating between minNode and minNode2 (the twin board)
            if (!alternate) {
                minNode = searchQueue.delMin(); // remove the lowest priority board from PQ
                if (minNode.board.isGoal()) {
                    solvable = true;
                    moves = minNode.move;  // number of moves
                    break;
                }
                // Generate neighbors
                Iterable<Board> neighbors = minNode.board.neighbors();
                for (Board each : neighbors) {
                    if (minNode.previous == null || !minNode.previous.board.equals(each)) {
                        searchQueue.insert(new Node(each, minNode.move + 1, minNode));
                    }
                }

                alternate = true;
            }
            else { // alternate search from the twinNode
                minNode2 = searchQueue2.delMin();
                if (minNode2.board.isGoal())
                    // problem solved from the twin board
                    break;

                Iterable<Board> neighbors = minNode2.board.neighbors();
                for (Board each : neighbors) {
                    if (minNode2.previous == null || !minNode2.previous.board.equals(each)) {
                        searchQueue2.insert(new Node(each, minNode2.move + 1, minNode2));
                    }
                }
                alternate = false;

            }

        }

        if (solvable) {
            solutionPath = new ArrayList<>();
            Node current = minNode;
            while (current != null) {
                solutionPath.add(current.board);
                current = current.previous;
            }
        }
    }


    private class Node implements Comparable<Node> {
        private final int move;  // the number of moves required
        /* private int hamming = -1;  // illegal value as hamCount has to be > 0 */
        private int manhattan = -1; // illegal value as manhDist has to be > 0
        private final Board board;
        private final Node previous;

        public Node(Board b, int m, Node p) {
            board = b;
            previous = p;  // previous
            move = m;
        }

        public int manhattan() {
            if (manhattan < 0) {
                manhattan = move + board.manhattan();
            }
            return manhattan;

        }

        /*
        public int hamming() {
            if (hamming < 0) {
                return hamming = move + board.hamming();
            }
            return hamming;
        } */

        public int compareTo(Node that) {
            // Comparing the priority
            return manhattan() - that.manhattan();
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!solvable)
            return -1;
        return moves;
    }


    public Iterable<Board> solution() {
        // If unsolvable, return null. Otherwise, return a sequence of
        // boards in shortest-path solution.
        if (!solvable) {
            return null;
        }
        else {
            return new SolutionBoards();
        }

    }

    private class SolutionBoards implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIter();
        }


        class SolutionIter implements Iterator<Board> {
            private int index = solutionPath.size() - 1;


            @Override
            public boolean hasNext() {
                return index > -1;
            }

            @Override
            public Board next() {
                if (solutionPath.isEmpty()) {
                    throw new NoSuchElementException(" No more elements to return");
                }
                return solutionPath.get(index--);
            }
        }
    }


    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Solve the puzzle
        Solver solver = new Solver(initial);

        // Print solution to standard output
        if (!solver.isSolvable())
            StdOut.println(" No solution possible ");
        else {
            StdOut.println(" Minimum number of moves = " + solver.moves());
            for (Board solutionboard : solver.solution())
                StdOut.println(solutionboard);
        }

    }
}
