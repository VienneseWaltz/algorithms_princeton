/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 3/13/2020
 *  Description: This program takes an integer k as a command-line argument;
 *               reads a sequence of strings from the standard input and then
 *               prints exactly k of them, uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++) {
            rq.enqueue(StdIn.readString());
        }

        // System.out.println("\nFinished testing enqueue ********");

        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
        //   System.out.println("\nFinished testing dequeue ********");


    }
}
