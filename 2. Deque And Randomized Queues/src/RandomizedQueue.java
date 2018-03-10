import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item [] data;
    private int numItems;

    // construct an empty randomized queue
    public RandomizedQueue() {
        data = (Item[]) new Object[1];
        numItems = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return numItems == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (numItems == data.length) resize(2*data.length);
        data[numItems] = item;
        numItems++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (numItems == 0) throw new NoSuchElementException();

        int index = StdRandom.uniform(numItems);
        Item re = data[index];

        // If not removing the last one: move the last one to this index
        if (index != numItems-1) {
            data[index] = data[numItems-1];
        }
        data[numItems-1] = null;
        numItems--;

        if (numItems < data.length/4) {
            resize(data.length/2);
        }
        return re;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (numItems == 0) throw new NoSuchElementException();
        return data[StdRandom.uniform(numItems)];
    }

    private void resize(int newSize) {
        if (newSize < numItems) throw new UnsupportedOperationException();

        Item [] newData = (Item[]) new Object[newSize];
        for (int i = 0; i < numItems; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }


    // === ITERATOR ===

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    private class RQIterator implements Iterator<Item> {
        private final Item [] copiedData;
        private int index;

        public RQIterator() {
            copiedData = (Item[]) new Object[numItems];
            for (int i = 0; i < numItems; i++) {
                copiedData[i] = data[i];
            }
            StdRandom.shuffle(copiedData);
        }

        public boolean hasNext() {
            return index != copiedData.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            index++;
            return copiedData[index-1];
        }

    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue <Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(5);
        rq.enqueue(6);
        rq.enqueue(7);

        StdOut.println(rq.sample());
        rq.enqueue(8);
        rq.enqueue(9);
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();

        for (Object i: rq) {
            StdOut.println(i);
        }
    }
}