/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 4/1/2020
 *  Description: This program creates a data type that models an n-by-n board
 *               with sliding tiles. Assuming the constructore receives an
 *               n-by-n array where 0 represents the blank square and 2<= n < 128.
 *               To measure how close a board is to the goal board, we define 2
 *               notions of distance - the Hamming and Manhattan distances. We
 *               compare 2 boards for equality i.e. they have the same size and
 *               the corresponding tiles are in the same position.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private final int n;                        // dimension of the board
    private final int[][] board;                // the play board
    private ArrayList<Board> neighbors;   // neighboring boards within one move

    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // Return board dimensions
    public int dimension() {
        return n;
    }

    // Hamming distance between a board and a goal board is the number of tiles
    // in the wrong position.
    public int hamming() {
        int hamCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j + 1) {
                    hamCount++;
                }
            }
        }
        return hamCount;
    }

    // Manhattan distance beween a board and a goal board is the sum of the Manhattan
    // distances (sum of the vertical and horizontal distance) from the tiles to their
    // goal positions.
    public int manhattan() {
        int manhDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    int x = (board[i][j] - 1) / n;  // x-coordinate of expected position
                    int y = (board[i][j] - 1) % n;     // y-coordinate of expected position
                    manhDist += Math.abs(x - i) + Math.abs(y - j);

                }
            }
        }
        return manhDist;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // A duplicate board - junior is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1, y1;   // x-y coordinate of a tile
        int x2, y2;   // x-y coordinate of another tile

        do { // Generate random x-y coordinates for 2 tiles
            x1 = StdRandom.uniform(0, n);
            y1 = StdRandom.uniform(0, n);
            x2 = StdRandom.uniform(0, n);
            y2 = StdRandom.uniform(0, n);

        } while (board[x1][y1] == 0 || board[x2][y2] == 0 || board[x1][y1] == board[x2][y2]);

        int temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
        Board junior = new Board(board);

        // swap back to restore original board
        board[x2][y2] = board[x1][y1];
        board[x1][y1] = temp;

        return junior;
    }


    // Two boards are equal if they have the same size and their corresponding
    // tiles are in the same position.
    // @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("HE_EQUALS_NO_HASHCODE")
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass() || ((Board) y).dimension() != n)
            return false;

        if (y == this) return true;
        Board that = (Board) y;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }

        return true;
    }

    public Iterable<Board> neighbors() {
        int row = n, col = n;            // x-y coordinates of the blank tile
        neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        if (col < (n - 1)) {
            // blank tile moves rightward
            board[row][col] = board[row][col + 1];
            board[row][col + 1] = 0;
            neighbors.add(new Board(board));
            // move back the blank tile to restore original position
            board[row][col + 1] = board[row][col];
            board[row][col] = 0;
        }

        if (col > 0) {
            // blank tile moves leftward
            board[row][col] = board[row][col - 1];
            board[row][col - 1] = 0;
            neighbors.add(new Board(board));
            // move back the blank tile to restore original position
            board[row][col - 1] = board[row][col];
            board[row][col] = 0;
        }

        if (row < (n - 1)) {
            // blank tile moves downward
            board[row][col] = board[row + 1][col];
            board[row + 1][col] = 0;
            neighbors.add(new Board(board));
            // move back the blank tile to restore original position
            board[row + 1][col] = board[row][col];
            board[row][col] = 0;
        }

        if (row > 0) {
            // blank tile moves upward
            board[row][col] = board[row - 1][col];
            board[row - 1][col] = 0;
            neighbors.add(new Board(board));
            // move back the blank tile to restore original position
            board[row - 1][col] = board[row][col];
            board[row][col] = 0;

        }
        // return new BoardNeighbors();
        return neighbors;
    }
    /*
    private class BoardNeighbors implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new NeighborIterator();
        }

        class NeighborIterator implements Iterator<Board> {
            private int index = 0;
            private final int neighborCount = neighbors.size();


            @Override
            public boolean hasNext() {
                return index < neighborCount;
            }

            @Override
            public Board next() {
                if (neighbors.isEmpty()) { // if the neighbors arraylist is empty
                    throw new NoSuchElementException(" No more elements to return");
                }
                return neighbors.get(index++);
            }
        }
    } */

    public String toString() {
        String boardStr = String.format("%d\n", n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardStr = boardStr.concat(String.format(" %3d", board[i][j]));
            }
            boardStr = boardStr.concat(" \n");
        }
        return boardStr;
    }


    public static void main(String[] args) {

        // Create the initial board from an input file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        StdOut.println(initial.twin());

        for (Board b : initial.neighbors()) {
            StdOut.println(b);
          /* StdOut.println(b.manhattan());
            StdOut.println("\n *****"); */
        }
    }
}
