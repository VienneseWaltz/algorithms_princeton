/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 3/8/2020
 *  Description: This program creates a randomized queue that is similar to a
 *               stack or queue, except that the item to be removed is chosen
 *               randomly among items in the data structure.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // size of the queue
    private int numelements;

    private Item[] data;

    // Construct an empty randomized queue
    // @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        numelements = 0;
        data = (Item[]) new Object[4];

    }

    // Is the queue empty?
    public boolean isEmpty() {
        return numelements == 0;

    }

    // Return the number of items on the queue
    public int size() {
        return numelements;

    }

    // Resize array as needed
    private void resize(int newSize) {
        // @SuppressWarnings("unchecked")
        Item[] newData = (Item[]) new Object[newSize];

        if (newSize < 1 || newSize < numelements)
            return;
        assert (newSize >= numelements);


        for (int i = 0; i < numelements; i++) {
            newData[i] = data[i];
            data[i] = null;
        }
        data = newData;

    }

    // Add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(" The item must not be null.");
        }
        if (numelements == data.length) {
            resize(2 * data.length);
        }
        data[numelements++] = item;

    }

    // Remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException(" The randomized queue is empty.");
        }
        // Generate a random item index
        int randomItemIndex = StdRandom.uniform(numelements);

        // Place the data from the randomItemIndex location into randval
        Item randval = data[randomItemIndex];


        if (randomItemIndex == (numelements - 1)) {
            // Checking for the case when the value of randomItemIndex is the
            // same index vale of the last element of array data
            data[randomItemIndex] = null;
        }
        else {
            // Placing the last value from data array to randomItemIndex location
            data[randomItemIndex] = data[numelements - 1];

            // Array no longer holds a reference to that object
            data[numelements - 1] = null;

        }
        numelements -= 1;


        // Shrink the size of array data if necessary
        if (numelements > 0 && numelements == data.length / 4) {
            resize(data.length / 2);
        }
        return randval;

    }

    // Return (but do not remove) a random item
    public Item sample() {
        if (numelements == 0) {
            throw new NoSuchElementException();
        }
        return data[StdRandom.uniform(numelements)];
    }


    // Return an independent iterator over items in a random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();

    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int current;

        private final Item[] iteratorItems;


        public RandomizedQueueIterator() {
            current = 0;    // first index
            iteratorItems = (Item[]) new Object[numelements];


            for (int j = 0; j < numelements; j++) {
                iteratorItems[j] = data[j];
            }

            // Doing random shuffling of iteratorItems array
            StdRandom.shuffle(iteratorItems, 0, numelements);

        }


        @Override
        public boolean hasNext() {
            return current < numelements;
        }

        @Override
        public Item next() {
            /*
            if (current == 0) {
                throw new NoSuchElementException(" The randomized queue is empty.");
            }
            else {

                int index = StdRandom.uniform(current);
                Item obj = iteratorItems[index];
                iteratorItems[index] = iteratorItems[--current];

                return obj;

                 */

            // Current index is at the end of the randomized queue
            if (current >= numelements) {
                throw new NoSuchElementException(" No more elements in the randomized queue.");
            }
            else {
                Item obj = iteratorItems[current++];
                return obj;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }
    }

    // Unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<Integer>();


        System.out.println(" ********** Testing enqueue ***************");
        for (int i = 0; i < 10; i++) {
            test.enqueue(i);
        }

        System.out.println(" The size of the randomized queue = " + test.size());


        // Testing the RandomizedQueueIterator
        Iterator<Integer> iter = test.iterator();
        while (iter.hasNext()) {
            System.out.println("Iterator returns " + iter.next());
        }


        System.out.println(" *********** Testing dequeue **********");

        for (int i = 0; i < 5; i++) {
            Integer n = test.dequeue();

            System.out.println("i = " + i + ",  " + "n = " + n);
        }

        System.out.println(" The size of the randomized queue = " + test.size());

        System.out.println(" ********** Testing enqueue with StdRandom *********");
        for (int i = 0; i < 20; i++) {
            System.out.println(" Loop = " + i);
            test.enqueue(StdRandom.uniform(20));
        }
        System.out.println(" ********** Testing dequeue with 20 items ");
        for (int i = 0; i < 20; i++) {
            System.out.println(test.dequeue());
        }
        System.out.println(" The size of the randomized queue = " + test.size());

    }
}
