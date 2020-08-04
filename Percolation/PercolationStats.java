/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: February 28, 2020
 *  Description: PercolationStats class performs multiple computational
 *               experiments. By repeating computational experiments a certain
 *               number of times, and then averaging the results, we obtain a
 *               more accurate estimate of the percolation threshold.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // Holds each experiment's percolation threshold
    private final double[] percThreshold;

    // Tracking the number of independent computational experiments in a n-by-n grid
    private final int experiments;

    // A constant for the 95% confidence interval
    private static final double CONFIDENCE_95 = 1.96;

    // Constructor that would throw an IllegalArgumentException if n or trials is
    // not greater than 1.
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException(" Both n and trials must be greater than 1");
        }
        experiments = trials;
        percThreshold = new double[experiments];

        for (int t = 0; t < trials; t++) {
            int numOfOpens = 0;
            Percolation pc = new Percolation(n);

            while (!pc.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (!pc.isOpen(i, j) && !pc.isFull(i, j)) {
                    pc.open(i, j);
                }
            }

            // Invoking numberOfOpenSites from Percolation class to give the
            // the number of open sites.
            numOfOpens = pc.numberOfOpenSites();
            percThreshold[t] = (double) numOfOpens / (n * n);
        }
    }

    // Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThreshold);
    }

    // Sample standard deviation
    public double stddev() {
        return StdStats.stddev(percThreshold);
    }

    // Low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(experiments));

    }

    // High endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(experiments));

    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println(" mean                    = " + stats.mean());
        StdOut.println(" standard deviation      = " + stats.stddev());
        StdOut.println(" 95% confidence interval = " + stats.confidenceLo() +
                               " , " + stats.confidenceHi());


    }
}
