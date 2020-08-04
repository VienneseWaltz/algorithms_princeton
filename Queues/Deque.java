/* *****************************************************************************
 *  Name: Stella Soh
 *  Date: 3/6/2020
 *  Description: Implementation of a double-ended queue or deque that supports
 *               adding and removing items from either the front or the back.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;

    // size of LinkedList
    private int count;

    // Initializes a deque
    public Deque() {
        first = null;
        last = null;
        count = 0;
    }

    // Initializes a node
    private class Node {
        private Node previous = null;
        private Node next = null;
        private Item item;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    // Returns the number of items in the deque
    public int size() {
        return count;
    }


    // Add the item to the front
    public void addFirst(Item item) {
        // Check if the item is null
        if (item == null)
            throw new IllegalArgumentException();

        // Adding the first item if LinkedList is empty
        if (isEmpty()) {
            Node curr = new Node();
            curr.item = item;
            first = curr;
            last = curr;
        }
        else {
            Node oldFirst = first; // Save a link to the list in oldFirst
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.previous = first;

        }

        /*
        if (count == 0) {
            last = first;
        }
        else {
            oldFirst.previous = first;
        } */

        count++;
    }


    // Add the item to the back of the deque
    public void addLast(Item item) {
        // Check if the item is null
        if (item == null)
            throw new IllegalArgumentException();

        // Check if LinkedList is empty
        if (isEmpty()) {
            Node curr = new Node();
            curr.item = item;
            first = curr;
            last = curr;

        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.previous = oldLast;
            oldLast.next = last;
        }



     /*   if (count == 0) {
            first = last;
        }
        else {
            oldLast.next = last;
        } */

        count++;

    }


    // Remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item item = first.item;
        Node oldFirst = first;
        first = first.next;

    /*  oldFirst.previous = null;
        oldFirst.next = null; */

        if (first != null) {
            first.previous = null;
        }
        else {
            last = null;
        }

        count--;
        item = oldFirst.item;
        return item;
    }

    // Remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty.");

        Item item = last.item;
        Node oldLast = last;
        last = oldLast.previous;


        if (last != null) {
            last.next = null;
        }
        else {
            first = null;
        }

        count--;
        /*  return oldLast.item; */

        item = oldLast.item;
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }


    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }
    }


    // Driver code
    public static void main(String[] args) {
        Deque<Integer> test = new Deque<Integer>();

    /*    test.addFirst(1);
        test.removeLast();
        System.out.println(test.isEmpty());

        test.addLast(3);
        System.out.println("\n");
        System.out.println(" **** Printing the removeLast() item **** ");
        System.out.println(test.removeLast());

        System.out.println(" *** Printing the size **** ");
        System.out.println(test.size());
        while (!test.isEmpty()) {
            System.out.println(test.removeFirst());
        } */

        test.addFirst(1);
        System.out.println(test.removeFirst());
        test.addFirst(2);
        System.out.println(test.removeFirst());

        test.addFirst(7);
        test.addFirst(8);
        System.out.println(" Testing the removeLast() after addFirst(8) *****");
        System.out.println(test.removeLast());
        System.out.println(" Testing the second removeLast() ***");
        System.out.println(test.removeLast());




     /*   for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) test.addFirst(i);
            else test.addLast(i);
        }
        System.out.println(test.isEmpty());

        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) test.removeLast();
            else test.removeFirst();
        }
        System.out.println(test.isEmpty());

        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) test.addLast(i);
            else test.addFirst(i);
        }
        System.out.println(test.isEmpty());

        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) test.removeFirst();
            else test.removeLast();
        } */
    }

}

