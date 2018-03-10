import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node front;
    private Node back;
    private int size;

    public Deque() {
        front = null;
        back = null;
        size = 0;
    }

    // Determine if the Deque is empty
    public boolean isEmpty() {
        return (front == null && back == null);
    }

    // Return the number of items on the deque
    public int size() {
        return size;
    }

    // Add an item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node newNode = new Node();
        newNode.item = item;

        // Case: 0 items (set both front and back)
        // Case: 1 item (overwrite front where 'next' is back. Set back's left to be front)
        // Case: 2+ items (right is old front, set new front)

        if (size == 0) {
            back = newNode;
        } else if (size == 1) {
            newNode.right = back;
            back.left = newNode;
        } else {
            front.left = newNode;
            newNode.right = front;
        }

        front = newNode;
        size++;
    }

    // Add an item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node newNode = new Node();
        newNode.item = item;

        // Check if size is 1 (special case)
        // Case: 0 items (set both front and back)
        // Case: 1 item (overwrite back, left is front. Set front's righht to be back)
        // Case: 2+ items (left is old back, set new back)

        if (size == 0) {
            front = newNode;
        } else if (size == 1) {
            newNode.left = front;
            front.right = newNode;
        } else {
            back.right = newNode;
            newNode.left = back;
        }
        back = newNode;
        size++;
    }

    // Remove and return the item from the front
    public Item removeFirst() {
        // Case: 0 items - throw exception
        // Case: 1 item - make both null
        // Case: 2+ items - back's left's right is null
        if (size == 0) throw new NoSuchElementException();

        Item re = front.item;
        if (size == 1) {
            front = null;
            back = null;
        } else {
            front.right.left = null;
            front = front.right;
        }
        size--;
        return re;
    }

    // Remove and return the item from the end
    public Item removeLast() {
        // Case: 0 items - throw exception
        // Case: 1 item - make both null
        // Case: 2+ items - back's left's right is null
        if (size == 0) throw new NoSuchElementException();

        Item re = back.item;
        if (size == 1) {
            front = null;
            back = null;
        } else {
            back.left.right = null;
            back = back.left;
        }
        size--;
        return re;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        Item item;
        Node left;
        Node right;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = front;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item re = current.item;
            current = current.right;
            return re;
        }
    }


    // Unit testing
    public static void main(String[] args) {
        Deque <Integer> dq = new Deque <Integer>();
        StdOut.println(dq.isEmpty());
        dq.addLast(7);
        dq.addFirst(1);
        dq.addFirst(3);
        dq.addLast(8);
        for (int i: dq) {
            StdOut.println(i);
        }

        dq.removeFirst();
        dq.removeLast();
        dq.removeLast();
        StdOut.println();
        for (int i: dq) {
            StdOut.println(i);
        }

    }
}