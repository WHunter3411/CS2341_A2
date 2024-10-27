import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
//In my methedology I used static classes, found this online since I am nesting the static classes inside the actual usage classes
//Wade Hunter and Julia Bonsack First PQ
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq; // Store items at indices 1 to n
    private int n;    // Number of items on priority queue
    private Comparator<Key> comparator;  // Optional comparator

    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        n = 0;
    }

    public MinPQ() {
        this(1, null);
    }

    public MinPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    public void insert(Key x) {
        if (n == pq.length - 1) resize(2 * pq.length);
        pq[++n] = x;
        swim(n);
    }

    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n + 1] = null;
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
        return min;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        } else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    private void resize(int capacity) {
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    @Override
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private MinPQ<Key> copy;

        public HeapIterator() {
            copy = new MinPQ<>(size(), comparator);
            for (int i = 1; i <= n; i++) {
                copy.insert(pq[i]);
            }
        }

        @Override
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        @Override
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

    static class Job implements Comparable<Job> {
        int id;
        int processingTime;

        public Job(int id, int processingTime) {
            this.id = id;
            this.processingTime = processingTime;
        }

        @Override
        public int compareTo(Job other) {
            return Integer.compare(this.processingTime, other.processingTime);
        }

        @Override
        public String toString() {
            return "Job ID: " + id + ", Time: " + processingTime;
        }
    }

    public static void main(String[] args) {
        MinPQ<Job> pq = new MinPQ<>();

        try {
            while (!StdIn.isEmpty()) {
                int id = StdIn.readInt();
                int processingTime = StdIn.readInt();
                pq.insert(new Job(id, processingTime));
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error reading input. Ensure proper formatting.");
            return;
        }

        int totalJobs = pq.size();
        int currentTime = 0;
        int totalCompletionTime = 0;
        StringBuilder executionOrder = new StringBuilder("Execution order: [");

        while (!pq.isEmpty()) {
            Job job = pq.delMin();
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.append(job.id).append(", ");
        }

        executionOrder.setLength(executionOrder.length() - 2);
        executionOrder.append("]");

        double averageCompletionTime = (double) totalCompletionTime / totalJobs;

        StdOut.println(executionOrder);
        StdOut.println("Average completion time: " + averageCompletionTime);
    }
}
