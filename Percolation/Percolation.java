/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 2/27/2020
 *  Description: The percolation model is publicly represented as a n^n grid
 *               with (1,1) representing the top left of and (n,n)
 *               representing the bottom right of the grid.
 *
 *               Privately the model uses a 1-dimensional array:
 *               indices 1 through n^2 representing each site. The indices
 *               increment from left to right by column, and top to bottom
 *               by row.
 *
 *               Index 0 reperesents a virtual top that initializes open and
 *               connected to the entire top row. A full site is an open site
 *               that can be connected to the top row. Therefore index (n*n + 1)
 *               represents a full site. Percolation indicates that there is a
 *               full site in the bottom as well. Hence index (n*n + 2)
 *               represents a percolated site.
 *
 *               Openness is tracked by the isOpen array. When a site is open,
 *               it connects to adjacent open sites.
 *
 *               Fullness if tracked by isFull array.
 *
 *               The system is said to percolate when the virtual bottom site
 *               is full and there is a path to the virtual top.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // quickUnionStructure represents a percolated structure i.e.
    // connectivity between sites, and open sites percolate to
    // one another
    private final WeightedQuickUnionUF quickUnionStructure;

    // quickUnionStructureForFull tracks fullness without backwash
    private final WeightedQuickUnionUF quickUnionStructureForFull;

    // gridSize is the size of the square grid
    private final int gridSize;

    // boolean array isOpen monitors open/closed states in sites. Two sites can
    // percolate if they are open and connected.
    private boolean[] isOpen;


    // virtualTopIndex is connected to the top row. It is initialized to 0.
    private final int virtualTopIndex;

    // virtualBottomIndex is connected to the bottom row. It is initialized to
    // (n * n + 1)
    private final int virtualBottomIndex;

    private int countOpen;


    // Creates creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(" n must be at least 1. ");
        }
        gridSize = n;
        int arraySize = n * n + 2;
        isOpen = new boolean[arraySize];


        quickUnionStructure = new WeightedQuickUnionUF(n * n + 2);
        quickUnionStructureForFull = new WeightedQuickUnionUF(n * n + 1);

        virtualTopIndex = 0;
        virtualBottomIndex = n * n + 1;


        isOpen[virtualTopIndex] = true;
        isOpen[virtualBottomIndex] = true;

        // Connecting all top row sites to virtual top site
        for (int col = 1; col <= n; col++) {

            int row = 1;
            int topSiteIndex = siteIndex(row, col);
            quickUnionStructure.union(virtualTopIndex, topSiteIndex);
            quickUnionStructureForFull.union(virtualTopIndex, topSiteIndex);

            // Connecting all bottom row sites to virtual bottom site
            row = n;
            int bottomSiteIndex = siteIndex(row, col);
            quickUnionStructure.union(virtualBottomIndex, bottomSiteIndex);

        }
    }

    // Converting the 2-dimensional grid system into a 1-dimensional site array.
    // Valid indices are 1 to N^2. The row is indicate by i, and the column is
    // indicated by j. checkBounds throws exception for invalid boundaries

    private int siteIndex(int row, int col) {
        checkBounds(row, col);

        return col + ((row - 1) * gridSize);
    }


    // The indices i and j are integers between 1 and n, where (1,1) is the
    // topmost upper-left corner of the n-by-n grid. If either i or j is outside
    // of the range, checkBounds throws a java.lang.IndexOutOfBoundsException.
    private void checkBounds(int row, int col) {
        if (row < 1 || row > gridSize) {
            throw new IllegalArgumentException(" Row index row is out of bounds");

        }
        if (col < 1 || col > gridSize) {
            throw new IllegalArgumentException(" Column index col is out of bounds");
        }
    }


    // Opens the site (row, col) if it is not already open
    public void open(int row, int col) {
        // Obtain the 1-dimensional array index for a particular site
        int site = siteIndex(row, col);
        if (!isOpen(row, col)) {
            // To open that site, change the boolean value and union with any
            // adjacent open site.
            isOpen[site] = true;
            countOpen++;

            // Before connecting to a neighbor, check that the site is open, and
            // not on an edge.

            // Checking the adjacent site to the left of site
            if (col > 1 && isOpen(row, (col - 1))) {
                int indexToLeft = siteIndex(row, (col - 1));
                quickUnionStructure.union(site, indexToLeft);
                quickUnionStructureForFull.union(site, indexToLeft);
            }

            // Checking the adjacent site to the right of site
            if (col < gridSize && isOpen(row, (col + 1))) {
                int indexToRight = siteIndex(row, (col + 1));
                quickUnionStructure.union(site, indexToRight);
                quickUnionStructureForFull.union(site, indexToRight);
            }


            // Checking the adjacent site to the top of site
            if (row > 1 && isOpen((row - 1), col)) {
                int indexToTop = siteIndex((row - 1), col);
                quickUnionStructure.union(site, indexToTop);
                quickUnionStructureForFull.union(site, indexToTop);

            }

            // Checking the adjacent site to the bottom of site
            if (row < gridSize && isOpen((row + 1), col)) {
                int indexToBottom = siteIndex((row + 1), col);
                quickUnionStructure.union(site, indexToBottom);
                quickUnionStructureForFull.union(site, indexToBottom);

            }
        }
    }

    // Is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int site = siteIndex(row, col);
        return isOpen[site];

    }

    // Is the site (row, col) full?
    // Fullness is modeled by the top row unioning with the virtual top index.
    public boolean isFull(int row, int col) {

        int site = siteIndex(row, col);
        return (quickUnionStructureForFull.connected(virtualTopIndex, site) && isOpen[site]);

    }


    // Returns the number of open sites
    public int numberOfOpenSites() {
        //   System.out.print(" The value of open sites = " + countOpen);
        return countOpen;
    }

    // Does the system percolate?

    public boolean percolates() {
        if (gridSize > 1) {
            return quickUnionStructure.connected(virtualTopIndex, virtualBottomIndex);
        }
        else {
            return isOpen[siteIndex(1, 1)];
        }
    }

    public static void main(String[] args) {
        Percolation quickUnionStructure = new Percolation(1);
        StdOut.println(quickUnionStructure.percolates());
        quickUnionStructure.open(1, 1);
        StdOut.println(quickUnionStructure.percolates());


        Percolation quickUnionStructure2 = new Percolation(2);
        StdOut.println(quickUnionStructure2.percolates());
        quickUnionStructure2.open(1, 1);
        StdOut.println(quickUnionStructure2.percolates());

        quickUnionStructure2.open(2, 1);
        StdOut.println(quickUnionStructure2.percolates());

        Percolation quickUnionStructure3 = new Percolation(5);
        StdOut.println(quickUnionStructure3.percolates());
        quickUnionStructure3.open(5, 2);
        StdOut.println(quickUnionStructure3.percolates());
    }


}
