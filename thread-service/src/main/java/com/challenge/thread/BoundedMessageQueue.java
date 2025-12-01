package com.challenge.thread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A thread-safe bounded message queue implementation using the wait/notify mechanism.
 * This class demonstrates core concurrency concepts including synchronization,
 * blocking operations, and inter-thread communication.
 *
 * @param <T> The type of messages held in the queue.
 */
public class BoundedMessageQueue<T> {
    private final Queue<T> queue;
    private final int capacity;

    /**
     * Constructs a new BoundedMessageQueue with the specified capacity.
     *
     * @param capacity The maximum number of items the queue can hold.
     * @throws IllegalArgumentException if capacity is not positive.
     */
    public BoundedMessageQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    /**
     * Adds an item to the queue. This method blocks the calling thread if the queue is full
     * until space becomes available.
     * <p>
     * This demonstrates the Producer side of the Producer-Consumer pattern.
     *
     * @param item The item to add to the queue.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized void produce(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println(Thread.currentThread().getName() + " waiting to produce (Queue Full)");
            wait(); // Block until notified
        }
        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " produced: " + item);
        notifyAll(); // Notify waiting consumers that data is available
    }

    /**
     * Removes and returns an item from the queue. This method blocks the calling thread
     * if the queue is empty until an item becomes available.
     * <p>
     * This demonstrates the Consumer side of the Producer-Consumer pattern.
     *
     * @return The item removed from the queue.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized T consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " waiting to consume (Queue Empty)");
            wait(); // Block until notified
        }
        T item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " consumed: " + item);
        notifyAll(); // Notify waiting producers that space is available
        return item;
    }

    /**
     * Returns the current number of items in the queue.
     *
     * @return The size of the queue.
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Returns the maximum capacity of the queue.
     *
     * @return The capacity of the queue.
     */
    public int getCapacity() {
        return capacity;
    }
}
